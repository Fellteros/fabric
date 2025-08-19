package net.fabricmc.fabric.mixin.event.interaction;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.player.PlayerPickUpItemCallback;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

	@Inject(method = "onPlayerCollision", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 0))
	private void fabric_onPlayerCollisionBefore(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack, @Share("itemStack") LocalRef<ItemStack> localRef) {
		localRef.set(itemStack.copy());
	}

	@Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"), cancellable = true)
	private void fabric_onPlayerCollisionAfter(PlayerEntity player, CallbackInfo ci, @Share("itemStack") LocalRef<ItemStack> localRef) {
		boolean processFurther = PlayerPickUpItemCallback.EVENT.invoker().onSuccessfulPickup(player, (ItemEntity) (Object) this, localRef.get());

		if (!processFurther) {
			ci.cancel();
		}
	}
}
