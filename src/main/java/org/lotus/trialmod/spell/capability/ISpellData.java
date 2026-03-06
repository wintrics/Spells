package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ISpellData {
    List<PlayerSpellData> getAll();

    void add(PlayerSpellData data);
    PlayerSpellData get(ResourceLocation id);
    PlayerSpellData getOrDefault(ResourceLocation id);
    void remove(ResourceLocation id);
    boolean has(ResourceLocation id);
    void clear();
}
