package org.lotus.trialmod.client.hud;


import java.util.Comparator;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.ClientSpellCooldowns;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.registry.Spells;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
        int y = mc.getWindow().getGuiScaledHeight() - 10;

        long current = mc.level.getGameTime();
        
        var entries = Spells.all().stream()
				.map(spell -> new Entry(spell, ClientSpellCooldowns.getEndTick(spell.getId())))
				.filter(entry -> entry.endTick > current)
				.sorted(Comparator.comparingLong(Entry::endTick))
				.toList();
        

        for (Entry en : entries) {
        	long seconds = (en.endTick() - current) / 20;
        	
        	Component line = Component.literal("")
                    .append(en.spell().getName())
                    .append(Component.literal(": " + seconds + "s"));
        	
        	e.getGuiGraphics().drawString(mc.font, line, x, y, 0xFFFFFF);
        	y -= mc.font.lineHeight + 2;
        }
    }
	
	private record Entry(Spell spell, long endTick) {}
}
