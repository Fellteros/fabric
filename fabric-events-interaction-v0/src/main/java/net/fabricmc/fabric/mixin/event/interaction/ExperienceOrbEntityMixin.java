package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.player.PlayerXpEvents;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void fabric_onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
		boolean	processFurther = PlayerXpEvents.PICKUP_XP.invoker().onPickup(player, (ExperienceOrbEntity) (Object) this);

		if (!processFurther) {
			ci.cancel();
		}
	}
}
