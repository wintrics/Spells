package org.lotus.trialmod.spell.packet;

import org.lotus.trialmod.client.spell.ClientSpellCooldowns;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@MainThreaded
public class PacketSyncSpellCooldown implements INBTPacket {
	private ResourceLocation spellId;
	private long endTick;
	
	public PacketSyncSpellCooldown() {}
	
	public PacketSyncSpellCooldown(ResourceLocation spellId, long endTick) {
		this.spellId = spellId;
		this.endTick = endTick;
	}
	
	@Override
	public void write(CompoundTag tag) {
		tag.putString("spell", spellId.toString());
		tag.putLong("endTick", endTick);
	}
	
	@Override
	public void read(CompoundTag tag) {
		this.spellId = ResourceLocation.tryParse(tag.getString("spell"));
		this.endTick = tag.getLong("endTick");
	}
	
	@Override
	public void clientExecute(PacketContext ctx) {
		if (spellId == null) return;
		ClientSpellCooldowns.setEndTick(spellId, endTick);
	}
}
