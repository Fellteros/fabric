package net.fabricmc.fabric.api.event.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Callback for tossing (pressing Q with an item) or drag-n-dropping an item out of player's inventory.
 *
 * <p><i>This implementation is heavily inspired by NeoForge's <a href="https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/event/entity/item/ItemTossEvent.java">ItemTossEvent</a>.
 */
public interface PlayerTossItemCallback {
	/**
	 * Called upon tossing an item out of player's inventory.
	 *
	 * <p>Also gets called when drag-n-dropping an item/item stack outside inventory screens.
	 *
	 * @see PlayerEntity#dropItem
	 */
	Event<PlayerTossItemCallback> EVENT = EventFactory.createArrayBacked(PlayerTossItemCallback.class,
			listeners -> ((stack, retainOwnership, player) -> {
				for (PlayerTossItemCallback event : listeners) {
					return event.dropItem(stack, retainOwnership, player);
				}

				return true;
			}));

	/**
	 * Determines whether to call the vanilla logic after executing.
	 *
	 * @param stack the stack that gets tossed out of player's inventory
	 * @param retainOwnership whether to set the player as the item's owner
	 * @param player the player tossing the item/item stack out of their inventory
	 * @return {@code true} to allow further processing, {@code false} to cancel any other action
	 */
	boolean dropItem(ItemStack stack, boolean retainOwnership, PlayerEntity player);
}
