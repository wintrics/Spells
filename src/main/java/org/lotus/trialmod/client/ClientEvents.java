package org.lotus.trialmod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.ClientSpellRegister;
import org.lotus.trialmod.client.spell.ClientSpellTicker;
import org.lotus.trialmod.spell.packet.PacketCastSpell;
import org.zeith.hammerlib.net.Network;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        ClientSpellTicker.tick();
        for (var spell : ClientSpellRegister.CLIENT_SPELLS) {
            while (spell.getKey().consumeClick()) {
                TrialMod.LOGGER.debug("casting spell {}", spell.getSpell().getId());
                Network.sendToServer(new PacketCastSpell(spell.getSpell().getId()));
            }
        }
    }
}
