package net.fabricmc.fabric.mixin.event.interaction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.player.PlayerXpEvents;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "addExperience", at = @At("HEAD"), cancellable = true)
	private void fabric_addExperience(int experience, CallbackInfo ci) {
		boolean	processFurther = PlayerXpEvents.XP_CHANGE.invoker().onXpChange((PlayerEntity) (Object) this, experience);

		if (!processFurther) {
			ci.cancel();
		}
	}

	@Inject(method = "addExperienceLevels", at = @At("HEAD"), cancellable = true)
	private void fabric_addExperienceLevels(int levels, CallbackInfo ci) {
		boolean	processFurther = PlayerXpEvents.LEVEL_CHANGE.invoker().onLevelChange((PlayerEntity) (Object) this, levels);

		if (!processFurther) {
			ci.cancel();
		}
	}
}
