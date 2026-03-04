package org.lotus.trialmod.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.capability.PacketSpellEnd;
import org.lotus.trialmod.spell.packet.PacketSpellStart;
import org.lotus.trialmod.spell.registry.Spells;
import org.zeith.hammerlib.net.Network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerActiveSpells {
	private static final Map<UUID, List<Active>> ACTIVE = new HashMap<>();
	
	private record Active(ResourceLocation id, long startTick, int duration) {}
	
	public static void start(ServerPlayer player, Spell spell) {
        int dur = spell.getDurationTicks();
        if (dur <= 0) return;

        ACTIVE.computeIfAbsent(player.getUUID(), u -> new ArrayList<>())
              .add(new Active(spell.getId(), player.serverLevel().getGameTime(), dur));

        Network.sendTo(new PacketSpellStart(spell.getId(), dur, player.serverLevel().getGameTime()), player);
    }
	

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (!(e.player instanceof ServerPlayer player)) return;
        if (e.phase != TickEvent.Phase.END) return;

        var list = ACTIVE.get(player.getUUID());
        if (list == null || list.isEmpty()) return;

        long current = player.serverLevel().getGameTime();

        list.removeIf(action -> {
            Spell spell = Spells.get(action.id());
            if (spell == null) return true;

            int time = (int)(current - action.startTick());
            if (time >= action.duration()) {
                spell.serverEnd(player);
                Network.sendTo(new PacketSpellEnd(action.id()), player);
                return true;
            }

            spell.serverTick(player, time);
            return false;
        });
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent e) {
        ACTIVE.remove(e.getEntity().getUUID());
    }
}
