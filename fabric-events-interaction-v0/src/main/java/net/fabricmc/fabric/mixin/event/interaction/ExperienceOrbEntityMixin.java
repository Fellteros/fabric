/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.event.interaction;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.event.player.ItemDurabilityEvents;
import net.fabricmc.fabric.api.event.player.PlayerXpEvents;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void fabric_onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
		boolean processFurther = PlayerXpEvents.PICKUP_XP.invoker().onPickup(player, (ExperienceOrbEntity) (Object) this);

		if (!processFurther) {
			ci.cancel();
		}
	}

	@Inject(method = "repairPlayerGears", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), cancellable = true)
	private void fabric_repairPlayerGears(ServerPlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir, @Local ItemStack itemStack) {
		boolean processFurther = ItemDurabilityEvents.REPAIR.invoker().onRepair((ExperienceOrbEntity) (Object) this, itemStack, player, amount);

		if (!processFurther) {
			cir.setReturnValue(0);
		}
	}
}
