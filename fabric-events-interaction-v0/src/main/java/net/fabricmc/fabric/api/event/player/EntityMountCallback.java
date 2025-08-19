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

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Callback for entity mounting/dismounting.
 *
 * <p><i>This implementation is heavily inspired by NeoForge's <a href="https://github.com/neoforged/NeoForge/blob/1.21.x/src/main/java/net/neoforged/neoforge/event/entity/EntityMountEvent.java">EntityMountEvent</a></i>
 */
public interface EntityMountCallback {
	/**
	 * Called upon mounting/dismounting an entity on both server and client side.
	 */
	Event<EntityMountCallback> EVENT = EventFactory.createArrayBacked(EntityMountCallback.class,
			listeners -> ((mountingEntity, mountedEntity, world, isMounting) -> {
				for (EntityMountCallback event : listeners) {
					return event.onMount(mountingEntity, mountedEntity, world, isMounting);
				}

				return true;
			}));

	/**
	 * Determines whether to call the vanilla logic after executing this method call.
	 *
	 * @param mountingEntity the mounting entity
	 * @param mountedEntity the entity being mounted
	 * @param world the world the entities are in
	 * @param isMounting true when the mounted entity is being mounted
	 * @return {@code true} to allow further processing, {@code false} to cancel any other action
	 */
	boolean onMount(Entity mountingEntity, Entity mountedEntity, World world, boolean isMounting);
}
