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

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events related to experience changes.
 *
 * <p><i>This implementation is heavily inspired by NeoForge's <a href="https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/event/entity/player/PlayerXpEvent.java">PlayerXpEvent</a>.
 */
public final class PlayerXpEvents {
	/**
	 * Called just before picking up an experience orb by a player.
	 *
	 * @see ExperienceOrbEntity#onPlayerCollision
	 */
	public static final Event<PickupXp> PICKUP_XP = EventFactory.createArrayBacked(PickupXp.class,
			listeners -> (player, orb) -> {
				for (PickupXp event : listeners) {
					return event.onPickup(player, orb);
				}

				return true;
			});

	/**
	 * Called just before changing the amount of player's experience.
	 *
	 * @see PlayerEntity#addExperience
	 */
	public static final Event<XpChange> XP_CHANGE = EventFactory.createArrayBacked(XpChange.class,
			listeners -> (player, amount) -> {
				for (XpChange event : listeners) {
					return event.onXpChange(player, amount);
				}

				return true;
			});

	/**
	 * Called just before changing the amount of player's experience <b>levels</b>.
	 *
	 * @see PlayerEntity#addExperienceLevels
	 */
	public static final Event<LevelChange> LEVEL_CHANGE = EventFactory.createArrayBacked(LevelChange.class,
			listeners -> (player, levels) -> {
				for (LevelChange event : listeners) {
					return event.onLevelChange(player, levels);
				}

				return true;
			});

	@FunctionalInterface
	public interface PickupXp {
		/**
		 * Determines whether to call the vanilla logic when picking up an experience orb.
		 *
		 * @param player the player that picks up the orb
		 * @param orb the experience orb that gets picked up
		 * @return {@code true} to allow further processing, {@code false} to cancel any other action
		 */
		boolean onPickup(PlayerEntity player, ExperienceOrbEntity orb);
	}

	@FunctionalInterface
	public interface XpChange {
		/**
		 * Determines whether to call the vanilla logic when the amount of player's XP changes.
		 *
		 * @param player the player to whom should the XP be added
		 * @param amount the amount of XP to be added if the return value is {@code true}
		 * @return {@code true} to allow further processing, {@code false} to cancel any other action
		 */
		boolean onXpChange(PlayerEntity player, int amount);
	}

	@FunctionalInterface
	public interface LevelChange {
		/**
		 * Determines whether to call the vanilla logic when the amount of player's XP levels changes.
		 *
		 * @param player the player to whom should the levels be added
		 * @param levels the amount of experience levels to be added if the return value is {@code true}
		 * @return {@code true} to allow further processing, {@code false} to cancel any other action
		 */
		boolean onLevelChange(PlayerEntity player, int levels);
	}
}
