package org.lotus.trialmod.spell.packet;

import org.lotus.trialmod.client.spell.ClientActiveSpells;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@MainThreaded
public class PacketSpellStart implements INBTPacket {
    private ResourceLocation id;
    private int duration;
    private long startTick;

    public PacketSpellStart() {}
    public PacketSpellStart(ResourceLocation id, int duration, long startTick) {
        this.id = id; this.duration = duration; this.startTick = startTick;
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putString("id", id.toString());
        tag.putInt("dur", duration);
        tag.putLong("st", startTick);
    }

    @Override
    public void read(CompoundTag tag) {
        id = ResourceLocation.tryParse(tag.getString("id"));
        duration = tag.getInt("dur");
        startTick = tag.getLong("st");
    }

    @Override
    public void clientExecute(PacketContext ctx) {
        if (id == null) return;
        ClientActiveSpells.start(id, duration, startTick);
    }
}
