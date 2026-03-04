package org.lotus.trialmod.client;

import org.lotus.trialmod.TrialMod;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeybinds {
    public static final KeyMapping CAST_FIREBALL = new KeyMapping(
            "key.trialmod.cast_fireball",
            GLFW.GLFW_KEY_R,
            "key.categories.trialmod"
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(CAST_FIREBALL);
    }
}
