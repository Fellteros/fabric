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
