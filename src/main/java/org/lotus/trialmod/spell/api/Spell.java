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
    private final int durationTicks;
    private final Component name;
    
    public Spell(ResourceLocation id, String description, int cooldown) {
		this(id, description, cooldown, 0);
	}

    public Spell(ResourceLocation id, String description, int cooldown, int durationTicks) {
        this.id= id;
        this.description = description;
        this.cooldown = cooldown;
        this.durationTicks = durationTicks;
        this.name = Component.translatable("spell." + id.getNamespace() + "." + id.getPath());
    }
    
    public Component getName() {
        return name;
    }

    public abstract void cast(ServerPlayer player);

    public void effect(LocalPlayer player) {};
    
    public void serverTick(ServerPlayer player, int activeTick) {}
    public void serverEnd(ServerPlayer player) {}
    
    public void clientTick(LocalPlayer player, int activeTick) {}
    public void clientEnd(LocalPlayer player) {}

    public boolean canCast(ServerPlayer player) {
        return player.getCapability(ModCapabilities.SPELL_DATA)
        		.map(data -> !data.isOnCooldown(id, player))
				.orElse(true);
    }
    
    public final boolean castWithCooldown(ServerPlayer player) {
    	return player.getCapability(ModCapabilities.SPELL_DATA).map(data -> {
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
    			return false;
    		}
    		
    		cast(player);
    		
    		data.startCooldown(id, player, cooldown);
    		
    		return true;
    	}).orElse(false);
	}
}
