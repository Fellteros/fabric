package net.fabricmc.fabric.api.event.player;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Events related to experience changes
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
