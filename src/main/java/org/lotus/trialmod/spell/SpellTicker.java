package org.lotus.trialmod.spell;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.core.capability.ModCapabilities;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.api.SpellStage;
import org.lotus.trialmod.spell.capability.PlayerSpellData;
import org.lotus.trialmod.spell.packet.PacketSpellStage;
import org.lotus.trialmod.spell.registry.SpellRegister;
import org.zeith.hammerlib.net.Network;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellTicker {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if(!(e.player instanceof ServerPlayer player)) return;
        if (e.phase != TickEvent.Phase.END) return;

        player.getCapability(ModCapabilities.SPELL_DATA).ifPresent(spellData -> {
            long currentTick = player.level().getGameTime();
            var list = spellData.getAll();

            for (int i = list.size() - 1; i >= 0; i--) {
                PlayerSpellData data = list.get(i);
                Spell spell = SpellRegister.get(data.id());
                // сколько тиков длится текущий стейдж
                int executeTick =(int)(currentTick - data.startTick());
                if (spell == null) {
                    TrialMod.LOGGER.error("spell {} not found", data.id());
                    spellData.remove(data.id());
                    continue;
                }
                
                PlayerSpellData newData = data;
                PacketSpellStage packet = new PacketSpellStage(data.id(), data.stage(), currentTick, currentTick);
                
                if (data.stage().equals(SpellStage.CASTING)) {
                	if (executeTick >= spell.getCastTicks()) {
                		spell.serverCastingEnd(player);
                		spell.casting(player);
                		
                		newData = new PlayerSpellData(data.id(), SpellStage.ACTIVE, currentTick, currentTick);
                		packet = new PacketSpellStage(newData.id(), newData.stage(), currentTick, currentTick);
                	} else {
                		spell.serverCastingTick(player, executeTick);
                	}
                } else if(data.stage().equals(SpellStage.ACTIVE)) {
                	if (executeTick >= spell.getDurationTicks()) {
                		newData = new PlayerSpellData(data.id(), SpellStage.IDLE, currentTick, data.startCooldownTick());
                		packet = new PacketSpellStage(newData.id(), newData.stage(), currentTick, data.startCooldownTick());
                	} else {
                		spell.serverTick(player, executeTick);
                	}
                }
                
                if (newData != data) {
	                list.set(i, newData);
	                Network.sendTo(player, packet);
                }
            }
        });
    }
}
