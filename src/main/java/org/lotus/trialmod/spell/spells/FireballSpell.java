package org.lotus.trialmod.spell.spells;

import org.lotus.trialmod.TrialMod;
import org.lotus.trialmod.spell.api.Spell;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends Spell {

	public FireballSpell() {
		super(ResourceLocation.fromNamespaceAndPath(TrialMod.MODID, "fireball"), 
				"Test fireball spell", 20 * 5, 20 * 10);
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
		player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1, 1);
	}
	
	@Override
	public void serverEnd(ServerPlayer player) {
		player.displayClientMessage(Component.translatable("end spell!! on server"), false);
	}
	
	@Override
	public void clientEnd(LocalPlayer player) {
		player.displayClientMessage(Component.translatable("end spell!! on client"), false);
	}
	
	@Override
	public void serverTick(ServerPlayer player, int activeTick) {
		if (activeTick % 10 == 0) {
			var level = player.level();
			
			long current = level.getGameTime();
			long t = (current - activeTick) + this.getDurationTicks();
			
			if (t <= current) return;
			
			float progress = 1f - ((t - current) / (float)getDurationTicks());
			double speed = 0.2 + progress * 2.5;
			
			var tnt = new PrimedTnt(
				level,
				player.getX(),
				player.getY() + 5 + progress * 10,
				player.getZ(),
				player
			);
			tnt.setFuse((int)(t - current));
			tnt.setDeltaMovement(0, -speed, 0);
			
			level.addFreshEntity(tnt);
			
			
			var anvil = FallingBlockEntity.fall(
		            level,
		            BlockPos.containing(player.getX() + (Math.sin(current) * 7), player.getY() + 10 + progress * 10, player.getZ() + (Math.cos(current) * 7)),
		            Blocks.ANVIL.defaultBlockState()
		    );
			
			anvil.setDeltaMovement(0, -speed * 4, 0);
			level.addFreshEntity(anvil);
		}
		
	}
}
