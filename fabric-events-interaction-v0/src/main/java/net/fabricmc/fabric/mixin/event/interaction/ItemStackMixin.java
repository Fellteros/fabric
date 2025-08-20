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

import java.util.function.Consumer;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.event.player.ItemDurabilityEvents;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	//by non-entity
	@Inject(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private void fabric_damageByNonEntity(int amount, ServerWorld world, ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci) {
		if (player == null) {
			boolean processFurther = ItemDurabilityEvents.DAMAGE_NON_ENTITY.invoker().onDamageByNonEntity((ItemStack) (Object) this, amount, world, breakCallback);

			if (!processFurther) {
				ci.cancel();
			}
		}
	}

	//by entity
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V", at = @At("HEAD"), cancellable = true)
	private void fabric_damageByEntity(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
		if (!ItemDurabilityEvents.DAMAGE_ENTITY.invoker().onDamageByEntity((ItemStack) (Object) this, amount, entity, slot)) {
			ci.cancel();
		}
	}

	//on change
	@Inject(method = "setDamage", at = @At("HEAD"), cancellable = true)
	private void fabric_setDamage(int damage, CallbackInfo ci) {
		ItemStack stack = (ItemStack) (Object) this;

		if (!ItemDurabilityEvents.CHANGE.invoker().onDurabilityChange(stack, stack.getDamage(), damage - stack.getDamage())) {
			ci.cancel();
		}
	}

	//on break
	@Inject(method = "onDurabilityChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
	private void fabric_getDamageBeforeBreak(int damage, ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci, @Share("damageBeforeBreak") LocalIntRef ref) {
		ref.set(((ItemStack) (Object) this).getDamage());
	}

	//on break
	@Inject(method = "onDurabilityChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void fabric_onBreak(int damage, ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci, @Share("damageBeforeBreak") LocalIntRef ref) {
		if (!ItemDurabilityEvents.BREAK.invoker().onItemBreak((ItemStack) (Object) this, ref.get(), damage - ref.get(), player, breakCallback)) {
			ci.cancel();
		}
	}
}
