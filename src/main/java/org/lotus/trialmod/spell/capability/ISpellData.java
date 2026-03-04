package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface ISpellData {
    long getCooldownEndTick(ResourceLocation spellId);

    default boolean isOnCooldown(ResourceLocation spellId, ServerPlayer player) {
    	long current = player.serverLevel().getGameTime();
        return getCooldownEndTick(spellId) > current;
    }

    void setCooldownEndTick(ResourceLocation spellId, long endTick);

    default void startCooldown(ResourceLocation spellId, ServerPlayer player, long duration) {
    	long current = player.serverLevel().getGameTime();
        setCooldownEndTick(spellId, current + duration);
    }

    void clear();
}
