package org.lotus.trialmod.spell.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.SpellStage;

import java.util.*;

public class SpellData implements ISpellData {
    private final List<PlayerSpellData> dataList = new ArrayList<>();

    @Override
    public List<PlayerSpellData> getAll() {
        return dataList;
    }

    @Override
    public void add(PlayerSpellData data) {
        remove(data.id());
        dataList.add(data);
    }

    @Override
    public PlayerSpellData get(ResourceLocation id) {
        return dataList.stream()
                .filter(i -> i.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remove(ResourceLocation id) {
        dataList.removeIf(i -> i.id().equals(id));
    }

    @Override
    public boolean has(ResourceLocation id) {
        return dataList.stream().anyMatch(i -> i.id().equals(id));
    }

    @Override
    public void clear() {
        dataList.clear();
    }

    public CompoundTag serializeNBT() {
        CompoundTag root = new CompoundTag();
        ListTag list = new ListTag();

        for (PlayerSpellData data : dataList) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", data.id().toString());
            tag.putInt("stage", data.stage().ordinal());
            tag.putLong("start_tick", data.startTick());
            tag.putLong("start_cooldown", data.startCooldownTick());
            list.add(tag);
        }

        root.put("spell_list", list);
        return root;
    }

    public void deserializeNBT(CompoundTag root) {
        dataList.clear();
        ListTag list = root.getList("spell_list", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            String idStr = tag.getString("id");
            ResourceLocation id = ResourceLocation.tryParse(idStr);
            if (id == null) {
                TrialMod.LOGGER.error("id {} not found", idStr);
                continue;
            }

            int stageId = tag.getInt("stage");
            SpellStage stage = SpellStage.getById(stageId);
            if (stage == null) {
                TrialMod.LOGGER.error("invalid spell stage {} for spell {}", stageId, id);
                continue;
            }

            long startTick = tag.getLong("start_tick");
            long startCooldownTick = tag.getLong("start_cooldown");
            this.add(new PlayerSpellData(id, stage, startTick, startCooldownTick));
        }
    }
}
