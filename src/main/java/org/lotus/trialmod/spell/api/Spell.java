package org.lotus.trialmod.spell.api;

import lombok.Getter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@Getter
public abstract class Spell {
    private final ResourceLocation id;
    private final String description;
    private final int castTicks;
    private final int durationTicks;
    private final int cooldownTicks;
    private final Component name;

    /**
     * @param id айди спелла
     * @param description описание спелла
     * @param castTicks время кастования спелла
     * @param durationTicks продолжительность кастования спелла
     * @param cooldownTicks перезарядка спелла
     **/
    public Spell(ResourceLocation id, String description, int castTicks,
                    int durationTicks, int cooldownTicks) {
        this.id = id;
        this.description = description;
        this.castTicks = castTicks;
        this.durationTicks = durationTicks;
        this.cooldownTicks = cooldownTicks;
        this.name = Component.translatable(String.format("spell.%s.%s", id.getNamespace(), id.getPath()));
    }

    public void serverCastingTick(ServerPlayer player, int castTick) {}
    public void clientCastingTick(LocalPlayer player, int castTick) {}
    public void serverCastingEnd(ServerPlayer player) {}
    public void clientCastingEnd(LocalPlayer player) {}

    public abstract void casting(ServerPlayer player);
    public void effect(LocalPlayer player) {}

    public void serverTick(ServerPlayer player, int activeTicks) {}
    public void clientTick(LocalPlayer player, int activeTicks) {}
    public void serverEnd(ServerPlayer player) {}
    public void clientEnd(LocalPlayer player) {}

    public void serverCancel(ServerPlayer player) {}
    public void clientCancel(LocalPlayer player) {}
    
}
