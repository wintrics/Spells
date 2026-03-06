package org.lotus.trialmod.spell.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.core.capability.ModCapabilities;
import org.lotus.trialmod.spell.api.SpellStage;
import org.lotus.trialmod.spell.capability.PlayerSpellData;
import org.lotus.trialmod.spell.registry.SpellRegister;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketCastSpell implements INBTPacket {
    private ResourceLocation spellId;

    public PacketCastSpell() {}

    public PacketCastSpell(ResourceLocation spellId) {
        this.spellId = spellId;
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putString("spellId", spellId.toString());
    }

    @Override
    public void read(CompoundTag tag) {
        this.spellId = ResourceLocation.tryParse(tag.getString("spellId"));
    }

    @Override
    public void serverExecute(PacketContext ctx) {
        if (!ctx.hasSender()) return;
        if (spellId == null) return;

        TrialMod.LOGGER.debug("player {} cast spell {}", ctx.getSender().getName().getString(), spellId);

        var player = ctx.getSender();
        var spell = SpellRegister.get(spellId);
        if (spell == null) {
            TrialMod.LOGGER.warn("Player {} use unknown spell: {}", player.getName().getString(), spellId);
            return;
        }

        player.getCapability(ModCapabilities.SPELL_DATA).ifPresent(dataList -> {
            PlayerSpellData spellData = dataList.getOrDefault(spellId);
            PlayerSpellData newData = spellData;
            SpellStage stage = spellData.stage();
            long currentTick = player.level().getGameTime();
            PacketSpellStage packet = new PacketSpellStage(spellId, stage, currentTick, spellData.startCooldownTick());

            int executeTick = (int)(currentTick - spellData.startCooldownTick());
            
            if (executeTick < spell.getCooldownTicks()) {
              long endTick = spellData.startCooldownTick()
              + SpellRegister.get(spellId).getCooldownTicks();
		      double seconds = (endTick - currentTick) / 20.0;
		
		      player.displayClientMessage(
		              Component.translatable(
		                      "spell.on_cooldown",
		                      spell.getName(),
		                      String.format("%.1f", seconds)
		              ),
		              true
		      );
		      
		      TrialMod.LOGGER.debug("send to client sync cooldown {} {}",
                    spellId, spellData.startCooldownTick());
		      packet.setStage(SpellStage.COOLDOWN);
            } else {
              if (stage.equals(SpellStage.CASTING) || stage.equals(SpellStage.ACTIVE)) {
	              player.displayClientMessage(
	                      Component.translatable(
	                              "spell.already_active",
	                              spell.getName()
	                      ),
	                      true
	              );
	              return;
	          }
              
              newData.setStage(SpellStage.CASTING);
              packet.setStage(SpellStage.CASTING);
            }
            
            dataList.add(newData);
            Network.sendTo(player, packet);
        });
    }
}
