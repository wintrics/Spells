package org.lotus.trialmod.client.key;

import org.lotus.trialmod.spell.api.Spell;

import net.minecraft.client.KeyMapping;

public record SpellKeybind(KeyMapping key, Spell spell) {}
