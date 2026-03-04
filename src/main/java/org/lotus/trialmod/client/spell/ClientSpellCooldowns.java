package org.lotus.trialmod.client.spell;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ClientSpellCooldowns {
	private ClientSpellCooldowns() {}

	private static final Map<ResourceLocation, Long> END_TICKS = new HashMap<>();
	
	public static long setEndTick(ResourceLocation spellId, long endTick) {
		if (endTick <= 0) return END_TICKS.remove(spellId);
		else return END_TICKS.put(spellId, endTick);
	}
	
	public static long getEndTick(ResourceLocation spellId) {
		return END_TICKS.getOrDefault(spellId, 0L);
	}
	
	public static boolean isOnCooldown(ResourceLocation spellId) {
		var mc = Minecraft.getInstance();
		if (mc.player == null || mc.level == null) return false;
		long current = mc.level.getGameTime();
		return getEndTick(spellId) > current;
	}
	
	public static void clear() {
		END_TICKS.clear();
	}
}
