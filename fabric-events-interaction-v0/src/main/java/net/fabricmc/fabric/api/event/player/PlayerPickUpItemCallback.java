package net.fabricmc.fabric.api.event.player;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Callback for <b>successfully</b> picking up an item by a player.
 *
 * <p><i>This implementation is heavily inspired by NeoForge's <a href="https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/event/entity/player/ItemEntityPickupEvent.java">ItemEntityPickupEvent</a>.
 */
public interface PlayerPickUpItemCallback {
	/**
	 * Called after the player successfully picks up the item.
	 *
	 * <p>If {@link #onSuccessfulPickup} returns {@code false}, the amount of times this item has been picked up is not incremented in player's statistics.
	 *
	 * <p>Called only on logical server side.
	 */
	Event<PlayerPickUpItemCallback> EVENT = EventFactory.createArrayBacked(PlayerPickUpItemCallback.class,
			listeners -> ((player, item, originalStack) -> {
				for (PlayerPickUpItemCallback event : listeners) {
					return event.onSuccessfulPickup(player, item, originalStack);
				}

				return true;
			}));

	/**
	 * Determines whether to call the vanilla logic after executing this method call.
	 *
	 * @param player the player
	 * @param item the picked up item
	 * @param originalStack copy of the original ItemStack before it was added to the player's inventory
	 * @return {@code true} to allow further processing, {@code false} to cancel any other action
	 */
	boolean onSuccessfulPickup(PlayerEntity player, ItemEntity item, ItemStack originalStack);
}
