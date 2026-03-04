package org.lotus.trialmod.spell.spells;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.Spell;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends Spell {

	public FireballSpell() {
		super(ResourceLocation.fromNamespaceAndPath(TrialMod.MODID, "fireball"), 
				"Test fireball spell", 20 * 5);
	}

	@Override
	public void cast(ServerPlayer player) {
		Vec3 look = player.getLookAngle();
		
		LargeFireball fireball = new LargeFireball(
				player.level(),
				player,
				look.x,
				look.y,
				look.z,
				1
		);
		
		Vec3 pos = player.getEyePosition().add(look.scale(1.5));
		
		fireball.setDeltaMovement(look.scale(5.5));
		fireball.setPos(pos);
		
		player.level().addFreshEntity(fireball);
	}

	@Override
	public void effect(LocalPlayer player) {
	}

}
