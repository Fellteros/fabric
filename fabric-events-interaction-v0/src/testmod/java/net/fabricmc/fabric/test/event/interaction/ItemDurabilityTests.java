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
import net.fabricmc.fabric.api.event.player.ItemDurabilityEvents;

public class ItemDurabilityTests implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(ItemDurabilityTests.class);

	@Override
	public void onInitialize() {
		ItemDurabilityEvents.BREAK.register((stack, totalDamageBefore, damage, player, breakCallback) -> {
			LOGGER.info("Item stack '{}' received {} damage ({} -> {}) and broke", stack, damage, totalDamageBefore, totalDamageBefore + damage);
			return true;
		});

		ItemDurabilityEvents.DAMAGE_ENTITY.register((stack, damage, entity, slot) -> {
			LOGGER.info("Entity '{}' damaged stack '{}' by {} in slot '{}'", entity, stack, damage, slot.getName());
			return true;
		});

		ItemDurabilityEvents.DAMAGE_NON_ENTITY.register((stack, damage, world, breakCallback) -> {
			LOGGER.info("Non-entity damaged stack '{}' by {}; breakCallback = {}", stack, damage, breakCallback);
			return true;
		});

		ItemDurabilityEvents.CHANGE.register((stack, totalDamageBefore, amount) -> {
			LOGGER.info("Changed the amount of damage of stack '{}' from {} to {} by {}", stack, totalDamageBefore, totalDamageBefore + amount, amount);
			return true;
		});

		ItemDurabilityEvents.REPAIR.register((experienceOrb, stack, player, amount) -> {
			LOGGER.info("Repaired stack '{}' by xp orb '{}' by {}", stack, experienceOrb, amount);
			return true;
		});
	}
}
