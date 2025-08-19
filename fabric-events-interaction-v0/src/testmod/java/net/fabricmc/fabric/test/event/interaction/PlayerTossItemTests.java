package net.fabricmc.fabric.test.event.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerTossItemCallback;

public class PlayerTossItemTests implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(PlayerTossItemTests.class);

	@Override
	public void onInitialize() {
		PlayerTossItemCallback.EVENT.register((stack, retainOwnership, player) -> {
			LOGGER.info("Tossed item: {}", stack.getItem().getTranslationKey());
			return true;
		});
	}
}
