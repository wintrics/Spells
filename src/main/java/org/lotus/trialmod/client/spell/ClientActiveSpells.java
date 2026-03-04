package org.lotus.trialmod.client.spell;

import java.util.HashMap;
import java.util.Map;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.registry.Spells;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientActiveSpells {

    private static final Map<ResourceLocation, Active> ACTIVE = new HashMap<>();

    private record Active(long startTick, int duration) {}

    public static void start(ResourceLocation id, int duration, long serverStartTick) {
        ACTIVE.put(id, new Active(serverStartTick, duration));
    }

    public static void end(ResourceLocation id) {
        ACTIVE.remove(id);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        long current = mc.level.getGameTime();

        ACTIVE.entrySet().removeIf(en -> {
            ResourceLocation id = en.getKey();
            Active active = en.getValue();

            Spell spell = Spells.get(id);
            if (spell == null) return true;

            int time = (int)(current - active.startTick());
            if (time >= active.duration()) {
                spell.clientEnd(mc.player);
                return true;
            }

            spell.clientTick(mc.player, time);
            return false;
        });
    }
}