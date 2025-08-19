package net.fabricmc.fabric.test.event.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerPickUpItemCallback;

public class PlayerPickUpItemTests implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(PlayerPickUpItemTests.class);

	@Override
	public void onInitialize() {
		PlayerPickUpItemCallback.EVENT.register((player, item, originalStack) -> {
			LOGGER.info(originalStack.toString());
			LOGGER.info(item.getStack().toString());
			LOGGER.info("Player picked up an item; amount changed from {} to {}", originalStack.getCount(), item.getStack().getCount());
			return false;
		});
	}
}
