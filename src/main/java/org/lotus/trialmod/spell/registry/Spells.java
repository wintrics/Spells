package org.lotus.trialmod.spell.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.lotus.trialmod.spell.api.Spell;
import org.lotus.trialmod.spell.spells.FireballSpell;

import net.minecraft.resources.ResourceLocation;

public final class Spells {
    private Spells(){}

    private static final Map<ResourceLocation, Spell> SPELLS = new HashMap<>();

    public static final Spell FIREBALL = register(new FireballSpell());

    private static <T extends Spell> T register(T spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static Spell get(ResourceLocation id) {
        return SPELLS.get(id);
    }
    
    public static Collection<Spell> all() {
        return SPELLS.values();
    }
}
