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

package net.fabricmc.fabric.api.event.player;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Callback for <b>successfully</b> picking up an item by a player.
 *
 * <p><i>This implementation is heavily inspired by NeoForge's <a href="https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/event/entity/player/ItemEntityPickupEvent.java">ItemEntityPickupEvent</a></i>
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
			listeners -> ((player, item) -> {
				for (PlayerPickUpItemCallback event : listeners) {
					return event.onSuccessfulPickup(player, item);
				}

				return true;
			}));

	/**
	 * Determines whether to call the vanilla logic after executing this method call.
	 *
	 * @param player the player
	 * @param item the picked up item
	 * @return {@code true} to allow further processing, {@code false} to cancel any other action
	 */
	boolean onSuccessfulPickup(PlayerEntity player, ItemEntity item);
}
