package org.lotus.trialmod.spell.capability;

import net.minecraft.resources.ResourceLocation;
import org.lotus.trialmod.spell.api.SpellStage;

public class PlayerSpellData {
	private ResourceLocation id;
	private SpellStage stage;
	private long startTick;
	private long startCooldownTick;
	
	public PlayerSpellData(ResourceLocation id, SpellStage stage, long startTick, long startCooldownTick) {
		this.id = id;
		this.stage = stage;
		this.startTick = startTick;
		this.startCooldownTick = startCooldownTick;
	}
	
	public ResourceLocation id() {
        return id;
    }

    public SpellStage stage() {
        return stage;
    }

    public long startTick() {
        return startTick;
    }

    public long startCooldownTick() {
        return startCooldownTick;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public void setStage(SpellStage stage) {
        this.stage = stage;
    }

    public void setStartTick(long startTick) {
        this.startTick = startTick;
    }

    public void setStartCooldownTick(long startCooldownTick) {
        this.startCooldownTick = startCooldownTick;
    }
}
