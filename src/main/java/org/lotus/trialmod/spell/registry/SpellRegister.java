package org.lotus.trialmod.spell.registry;

import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.spells.FireballSpell;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellRegister {
    private static final Map<ResourceLocation, Spell> SPELL_MAP = new HashMap<>();

    public static final FireballSpell FIREBALL = register(new FireballSpell());

    private static <T extends Spell> T register(T spell) {
        SPELL_MAP.put(spell.getId(), spell);
        return spell;
    }

    public static Spell get(ResourceLocation id) {
        return SPELL_MAP.get(id);
    }

    public static Collection<Spell> all() {
        return SPELL_MAP.values();
    }
}
