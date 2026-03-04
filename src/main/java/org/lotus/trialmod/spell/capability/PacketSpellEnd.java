package org.lotus.trialmod.spell.capability;

import org.lotus.trialmod.client.spell.ClientActiveSpells;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@MainThreaded
public class PacketSpellEnd implements INBTPacket {
    private ResourceLocation id;

    public PacketSpellEnd() {}
    public PacketSpellEnd(ResourceLocation id) {
    	this.id = id;
    }

    @Override
    public void write(CompoundTag tag) {
    	tag.putString("id", id.toString());
    }
    
    @Override
    public void read(CompoundTag tag) {
    	id = ResourceLocation.tryParse(tag.getString("id"));
    }

    @Override public void clientExecute(PacketContext ctx) {
        if (id == null) return;
        ClientActiveSpells.end(id);
    }
}
