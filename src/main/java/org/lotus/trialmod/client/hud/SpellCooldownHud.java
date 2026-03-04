package org.lotus.trialmod.client.hud;

import java.util.Comparator;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.ClientSpellCooldowns;
import org.lotus.trialmod.client.spell.SpellKeybindRegistry;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.registry.Spells;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TrialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SpellCooldownHud {
	
	@SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        int x = 5;
        int y = mc.getWindow().getGuiScaledHeight() - 5;

        long current = mc.level.getGameTime();

        var entries = SpellKeybindRegistry.SPELL_BINDS.stream()
                .map(b -> b.spell().getId())
                .distinct()
                .map(id -> new Entry(id, ClientSpellCooldowns.getEndTick(id)))
                .filter(en -> en.endTick() > current)
                .sorted(Comparator.comparingLong(Entry::endTick))
                .toList();

        if (entries.isEmpty()) return;

        for (Entry en : entries) {
            long seconds = (en.endTick() - current) / 20;

            Spell spell = Spells.get(en.id());
            if (spell == null) continue;

            Component text = Component.literal("")
                    .append(spell.getName())
                    .append(Component.literal(": " + seconds + "s"));

            y -= mc.font.lineHeight + 2;
            e.getGuiGraphics().drawString(mc.font, text, x, y, 0xFFFFFF, false);
        }
    }
	
	private record Entry(ResourceLocation id, long endTick) {}
}
