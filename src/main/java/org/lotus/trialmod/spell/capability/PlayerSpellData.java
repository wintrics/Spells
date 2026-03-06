package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.spell.api.SpellStage;

public record PlayerSpellData(
        ResourceLocation id,
        SpellStage stage,
        long startTick,
        long startCooldownTick
) {}
