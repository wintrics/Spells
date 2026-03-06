package org.lotus.trialmod.client.spell;

import lombok.Getter;
import net.minecraft.client.KeyMapping;
import org.lotus.trialmod.spell.api.Spell;

@Getter
public class ClientSpell {
    private final Spell spell;
    private final KeyMapping key;

    public ClientSpell(Spell spell, KeyMapping key) {
        this.spell = spell;
        this.key = key;
    }
}
