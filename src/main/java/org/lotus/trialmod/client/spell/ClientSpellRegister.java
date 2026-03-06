package org.lotus.trialmod.client.spell;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.registry.SpellRegister;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSpellRegister {
    public static final KeyMapping CAST_FIREBALL = new KeyMapping(
            "key.trialmod.cast_fireball",
            GLFW.GLFW_KEY_R,
            "key.categories.trialmod"
    );


    public static final List<ClientSpell> CLIENT_SPELLS = List.of(
            new ClientSpell(SpellRegister.FIREBALL, CAST_FIREBALL)
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent e) {
        e.register(CAST_FIREBALL);
    }
}
