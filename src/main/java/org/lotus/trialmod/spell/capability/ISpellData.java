package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ISpellData {
    List<PlayerSpellData> getAll();

    void add(PlayerSpellData data);
    PlayerSpellData get(ResourceLocation id);
    void remove(ResourceLocation id);
    boolean has(ResourceLocation id);
    void clear();
}
