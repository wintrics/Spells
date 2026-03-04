package org.lotus.trialmod.client.spell;


import java.util.List;

import org.lotus.trialmod.client.ModKeybinds;
import org.lotus.trialmod.client.key.SpellKeybind;
import org.lotus.trialmod.spell.registry.Spells;

public class SpellKeybindRegistry {
    public static final List<SpellKeybind> SPELL_BINDS = List.of(
            new SpellKeybind(ModKeybinds.CAST_FIREBALL, Spells.FIREBALL)
    );
}
