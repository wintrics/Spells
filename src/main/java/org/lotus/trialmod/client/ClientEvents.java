package org.lotus.trialmod.client;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.SpellKeybindRegistry;
import org.lotus.trialmod.spell.packet.PacketCastSpell;
import org.zeith.hammerlib.net.Network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
    public static void onClientTick(ClientTickEvent e) {
		for (var bind : SpellKeybindRegistry.SPELL_BINDS) {
		    while (bind.key().consumeClick()) {
		        Network.sendToServer(new PacketCastSpell(bind.spell().getId()));
		    }
		}
    }
}
