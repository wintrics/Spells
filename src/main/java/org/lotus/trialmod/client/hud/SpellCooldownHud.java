package org.lotus.trialmod.client.hud;

import java.util.Comparator;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.ClientSpellTicker;
import org.lotus.trialmod.spell.api.SpellStage;
import org.lotus.trialmod.spell.registry.SpellRegister;

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
		
		var spellDataList = ClientSpellTicker.getSpellData().stream()
				.filter(data -> {
					var spell = SpellRegister.get(data.id());
					if (spell == null) return false;
					return data.startCooldownTick() + spell.getCooldownTicks() > current;
				})
				.sorted(Comparator.comparingLong(data -> data.startCooldownTick()))
				.toList();
		
		for (var data : spellDataList) {
			var spell = SpellRegister.get(data.id());
			long endTick = data.startCooldownTick() + spell.getCooldownTicks();
			long seconds = ((endTick - current) / 20) + 1;
			
			Component line = Component.literal("")
                    .append(spell.getName())
                    .append(Component.literal(": " + seconds + "s"));
			
			e.getGuiGraphics().drawString(mc.font, line, x, y, 0xFFFFFF);
			y -= mc.font.lineHeight + 2;
		}
	}
}
