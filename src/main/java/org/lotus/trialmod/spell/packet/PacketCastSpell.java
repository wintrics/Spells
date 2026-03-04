package org.lotus.trialmod.spell.packet;

import org.lotus.trialmod.core.capability.ModCapabilities;
import org.lotus.trialmod.spell.registry.Spells;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@MainThreaded
public class PacketCastSpell implements INBTPacket {
	private ResourceLocation spellId;

    public PacketCastSpell() {}

    public PacketCastSpell(ResourceLocation spellId) {
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
    public void serverExecute(PacketContext ctx) {
        if (!ctx.hasSender()) return;
        if (spellId == null) return;

        var player = ctx.getSender();

        var spell = Spells.get(spellId);
        if (spell == null) return;

        spell.castWithCooldown(player);
        
        player.getCapability(ModCapabilities.SPELL_DATA).ifPresent(data -> {
        	long endTick = data.getCooldownEndTick(spellId);
			Network.sendTo(new PacketSyncSpellCooldown(spellId, endTick), player);
        });
    }
}
