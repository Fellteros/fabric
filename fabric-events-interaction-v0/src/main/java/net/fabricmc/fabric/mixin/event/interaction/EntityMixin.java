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
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.player.EntityMountCallback;

@Mixin(Entity.class)
@Debug(export = true)
public abstract class EntityMixin {
	@Shadow
	public abstract World getWorld();

	@Shadow
	public abstract void updatePositionAndAngles(double x, double y, double z, float yaw, float pitch);

	@Shadow
	public abstract double getX();

	@Shadow
	public abstract double getY();

	@Shadow
	public abstract double getZ();

	@Shadow
	public float lastYaw;

	@Shadow
	public float lastPitch;

	@SuppressWarnings("DiscouragedInjectionPoint")
	@Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 3), cancellable = true)
	private void fabric_startRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
		boolean processFurther = EntityMountCallback.EVENT.invoker().onMount((Entity) (Object) this, entity, this.getWorld(), true);

		if (!processFurther) {
			this.updatePositionAndAngles(this.getX(), this.getY(), this.getZ(), this.lastYaw, this.lastPitch);
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "dismountVehicle", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;vehicle:Lnet/minecraft/entity/Entity;", ordinal = 2), cancellable = true)
	private void fabric_dismountVehicle(CallbackInfo ci, @Local Entity entity) {
		if (!EntityMountCallback.EVENT.invoker().onMount((Entity) (Object) this, entity, this.getWorld(), false)) {
			ci.cancel();
		}
	}
}
