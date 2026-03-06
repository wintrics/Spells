package org.lotus.trialmod.spell.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.client.spell.ClientSpellTicker;
import org.lotus.trialmod.spell.api.SpellStage;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import lombok.Setter;

@MainThreaded
@Setter
public class PacketSpellStage implements INBTPacket {
    private ResourceLocation id;
    private SpellStage stage;
    private long startStageTick;
    private long startCooldownTick;

    public PacketSpellStage() {}

    public PacketSpellStage(ResourceLocation id, SpellStage stage, long startStageTick, long startCooldownTick) {
        this.id = id;
        this.stage = stage;
        this.startStageTick = startStageTick;
        this.startCooldownTick = startCooldownTick;
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("id", id.toString());
        nbt.putInt("stage", stage.ordinal());
        nbt.putLong("start_stage_tick", startStageTick);
        nbt.putLong("start_cooldown_tick", startCooldownTick);
    }

    @Override
    public void read(CompoundTag nbt) {
        id = ResourceLocation.tryParse(nbt.getString("id"));
        stage = SpellStage.getById(nbt.getInt("stage"));
        startStageTick = nbt.getLong("start_stage_tick");
        startCooldownTick = nbt.getLong("start_cooldown_tick");
    }

    @Override
    public void clientExecute(PacketContext ctx) {
        if (id == null) {
            TrialMod.LOGGER.error("player {} send packet null spell id",
                    ctx.getSender().getName().getString());
            return;
        }
        ClientSpellTicker.onStage(id, stage, startStageTick, startCooldownTick);
    }
}
