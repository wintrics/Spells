package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.core.capability.ModCapabilities;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellCapabilityEvents {
    private static final ResourceLocation KEY =
            ResourceLocation.fromNamespaceAndPath(TrialMod.MODID, "spell_data");

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
			SpellDataProvider provider = new SpellDataProvider();
			event.addCapability(KEY, provider);
			event.addListener(provider::invalidate);
		}
    }
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
    	var oldPlayer = event.getOriginal();
    	var newPlayer = event.getEntity();
    	
    	oldPlayer.reviveCaps();
    	
    	oldPlayer.getCapability(ModCapabilities.SPELL_DATA).ifPresent(oldData -> {
			newPlayer.getCapability(ModCapabilities.SPELL_DATA).ifPresent(newData -> {
				if (oldData instanceof SpellData od && newData instanceof SpellData nd) {
					nd.deserializeNBT(od.serializeNBT());
				}
			});
		});
    	
    	oldPlayer.invalidateCaps();
    }
    
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ISpellData.class);
    }
}
