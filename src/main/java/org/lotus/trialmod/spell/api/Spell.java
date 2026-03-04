package org.lotus.trialmod.spell.api;

import org.lotus.trialmod.core.capability.ModCapabilities;

import lombok.Getter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@Getter
public abstract class Spell {
    private final ResourceLocation id;
    private final String description;
    private final int cooldown;
    private final Component name;

    public Spell(ResourceLocation id, String description, int cooldown) {
        this.id= id;
        this.description = description;
        this.cooldown = cooldown;
        this.name = Component.translatable("spell." + id.getNamespace() + "." + id.getPath());
    }
    
    public Component getName() {
        return name;
    }

    public abstract void cast(ServerPlayer player);

    public abstract void effect(LocalPlayer player);

    public boolean canCast(ServerPlayer player) {
        return player.getCapability(ModCapabilities.SPELL_DATA)
        		.map(data -> !data.isOnCooldown(id, player))
				.orElse(true);
    }
    
    public final void castWithCooldown(ServerPlayer player) {
    	player.getCapability(ModCapabilities.SPELL_DATA).ifPresent(data -> {
    		if (!canCast(player)) {		
    			long endTick = data.getCooldownEndTick(id);
    			long current = player.serverLevel().getGameTime();
    			
    			double seconds = (endTick - current) / 20.0;
    			
    			player.displayClientMessage(
						Component.translatable(
								"spell.on_cooldown",
								this.getName(),
								String.format("%.1f", seconds)
						),
						true
				);
    			return;
    		}
    		
    		cast(player);
    		
    		data.startCooldown(id, player, cooldown);
    	});
	}
}
