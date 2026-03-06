package org.lotus.trialmod.spell.api;

public enum SpellStage {
    IDLE,
    CASTING,
    ACTIVE,
    COOLDOWN,
    END,
    CANCEL;

    public static SpellStage getById(int id) {
        if (id < 0 || id >= values().length) {
            return null;
        }
        return values()[id];
    }
}
