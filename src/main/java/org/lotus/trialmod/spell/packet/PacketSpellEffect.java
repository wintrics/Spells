package org.lotus.trialmod.spell.packet;

import org.lotus.trialmod.spell.registry.Spells;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@MainThreaded
public class PacketSpellEffect implements INBTPacket {
    private ResourceLocation spellId;

    public PacketSpellEffect() {}

    public PacketSpellEffect(ResourceLocation spellId) {
        this.spellId = spellId;
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putString("spell", spellId.toString());
    }

    @Override
    public void read(CompoundTag tag) {
        this.spellId = ResourceLocation.tryParse(tag.getString("spell"));
    }

    @Override
    public void clientExecute(PacketContext ctx) {
        if (spellId == null) return;

        var mc = Minecraft.getInstance();
    
        if (mc.player == null || mc.level == null) return;
        

        var spell = Spells.get(spellId);
        if (spell == null) return;

        spell.effect(mc.player);
    }
}
