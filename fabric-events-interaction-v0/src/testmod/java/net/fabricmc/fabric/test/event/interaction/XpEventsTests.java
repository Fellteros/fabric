package net.fabricmc.fabric.test.event.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerXpEvents;

public class XpEventsTests implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(XpEventsTests.class);

	@Override
	public void onInitialize() {
		PlayerXpEvents.PICKUP_XP.register((player, orb) -> {
			LOGGER.info("Picked up an experience orb containing {} xp", orb.getValue());
			return true;
		});

		PlayerXpEvents.XP_CHANGE.register((player, amount) -> {
			LOGGER.info("Changed amount of player's experience from {} to {}", player.totalExperience, player.totalExperience + amount);
			return true;
		});

		PlayerXpEvents.LEVEL_CHANGE.register((player, levels) -> {
			LOGGER.info("Changed amount of player's experience levels from {} to {}", player.experienceLevel, player.experienceLevel + levels);
			return true;
		});
	}
}
