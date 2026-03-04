package org.lotus.trialmod.spell.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SpellData implements ISpellData {
    private static final String SPELL_ID = "spellId";
    private static final String END_TICK = "endTick";
    private static final String COOLDOWNS = "cooldowns";

    private final Map<ResourceLocation, Long> cooldowns = new HashMap<>();

    @Override
    public long getCooldownEndTick(ResourceLocation spellId) {
        return cooldowns.getOrDefault(spellId, 0L);
    }

    @Override
    public void setCooldownEndTick(ResourceLocation spellId, long endTick) {
        if (endTick <= 0) cooldowns.remove(spellId);
        else cooldowns.put(spellId, endTick);
    }

    @Override
    public void clear() {
        cooldowns.clear();
    }

    public CompoundTag serializeNBT() {
        CompoundTag root = new CompoundTag();
        ListTag list = new ListTag();

        for (var e : cooldowns.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString(SPELL_ID, e.getKey().toString());
            tag.putLong(END_TICK, e.getValue());
            list.add(tag);
        }

        root.put(COOLDOWNS, list);
        return root;
    }

    public void deserializeNBT(CompoundTag root) {
        cooldowns.clear();
        ListTag list = root.getList(COOLDOWNS, Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            ResourceLocation spellId = ResourceLocation.tryParse(tag.getString(SPELL_ID));

            if (spellId != null) {
                long endTick = tag.getLong(END_TICK);
                cooldowns.put(spellId, endTick);
            }
        }
    }
}
