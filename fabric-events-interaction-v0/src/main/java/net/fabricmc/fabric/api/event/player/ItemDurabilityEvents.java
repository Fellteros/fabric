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

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

///
/// Callbacks for events related to item durability changes.
///
public final class ItemDurabilityEvents {
	///
	/// Called when an item is broken by an entity.
	/// If [`onItemBreak`][Break#onItemBreak] returns `false`, the item won't be incremented and the itemCallback won't be used
	///
	/// @see ItemStack#onDurabilityChange
	///
	public static final Event<Break> BREAK = EventFactory.createArrayBacked(Break.class,
			listeners -> ((stack, totalDamageBefore, damage, player, itemCallback) -> {
				for (Break event : listeners) {
					return event.onItemBreak(stack, totalDamageBefore, damage, player, itemCallback);
				}

				return true;
			}));

	///
	/// Called when a [LivingEntity] damages an item.
	///
	/// @see ItemStack#damage(int, LivingEntity, EquipmentSlot)
	///
	public static final Event<DamageByEntity> DAMAGE_ENTITY = EventFactory.createArrayBacked(DamageByEntity.class,
			listeners -> ((stack, damage, entity, slot) -> {
				for (DamageByEntity event : listeners) {
					return event.onDamageByEntity(stack, damage, entity, slot);
				}

				return true;
			}));

	///
	/// Called when a non-entity, such as a [`dispenser`][net.minecraft.block.DispenserBlock], damages an item.
	///
	/// @see ItemStack#damage(int, ServerWorld, ServerPlayerEntity, Consumer)
	///
	public static final Event<DamageByNonEntity> DAMAGE_NON_ENTITY = EventFactory.createArrayBacked(DamageByNonEntity.class,
			listeners -> ((stack, damage, world, itemCallback) -> {
				for (DamageByNonEntity event : listeners) {
					return event.onDamageByNonEntity(stack, damage, world, itemCallback);
				}

				return true;
			}));

	///
	/// Called every time an item's durability is changed, whether increased or decreased.
	/// If [`onDurabilityChange`][Change#onDurabilityChange] returns `false`, the damage won't change.
	///
	/// @apiNote This is also called when adding partially broken items to item groups
	///
	/// @see ItemStack#setDamage(int)
	///
	public static final Event<Change> CHANGE = EventFactory.createArrayBacked(Change.class,
			listeners -> ((stack, totalDamageBefore, amount) -> {
				for (Change event : listeners) {
					return event.onDurabilityChange(stack, totalDamageBefore, amount);
				}

				return true;
			}));

	///
	/// Called **only** when an item is repairable and is repaired by xp.
	///
	/// @see ExperienceOrbEntity#repairPlayerGears(ServerPlayerEntity, int)
	///
	public static final Event<Repair> REPAIR = EventFactory.createArrayBacked(Repair.class,
			listeners -> ((orb, stack, player, amount) -> {
				for (Repair event : listeners) {
					return event.onRepair(orb, stack, player, amount);
				}

				return true;
			}));

	@FunctionalInterface
	public interface Break {
		///
		/// Determines whether to call the vanilla logic after executing this method call.
		///
		/// @param stack the stack to be broken
		/// @param totalDamageBefore the initial amount of damage to the stack
		/// @param damage the amount of damage to be added to the total damage, after which the item breaks
		/// @param player the player breaking the item. <b>May be null</b> if the stack gets broken by a block (e.g., dispenser)
		/// @param breakCallback the callback to be run when the stack breaks
		/// @return `true` to allow further processing, `false` to cancel any other action
		///
		boolean onItemBreak(ItemStack stack, int totalDamageBefore, int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback);
	}

	@FunctionalInterface
	public interface DamageByEntity {
		///
		/// Determines whether to call the vanilla logic after executing this method call.
		///
		/// @param stack the stack to be damaged
		/// @param damage the amount of damage to be added to the total
		/// @param entity the entity damaging the item
		/// @param slot the slot in which the item is located
		/// @return `true` to allow further processing, `false` to cancel any other action
		///
		boolean onDamageByEntity(ItemStack stack, int damage, LivingEntity entity, EquipmentSlot slot);
	}

	@FunctionalInterface
	public interface DamageByNonEntity {
		///
		/// Determines whether to call the vanilla logic after executing this method call.
		///
		/// @param stack the stack to be damaged
		/// @param damage the amount of damage to be added to the total damage
		/// @param world the world in which the stack is damaged
		/// @param breakCallback the callback to be run when the stack breaks
		/// @return `true` to allow further processing, `false` to cancel any other action
		///
		boolean onDamageByNonEntity(ItemStack stack, int damage, ServerWorld world, Consumer<Item> breakCallback);
	}

	@FunctionalInterface
	public interface Change {
		///
		/// Determines whether to call the vanilla logic after executing this method call.
		///
		/// @param stack the stack whose durability is to be changed
		/// @param totalDamageBefore the initial amount of damage to the stack
		/// @param amount the amount of damage to be added to the total damage. <b>May be negative</b> if the item is to be repaired by xp.
		/// @return `true` to allow further processing, `false` to cancel any other action
		///
		boolean onDurabilityChange(ItemStack stack, int totalDamageBefore, int amount);
	}

	@FunctionalInterface
	public interface Repair {
		///
		/// Determines whether to call the vanilla logic after executing this method call.
		///
		/// @param experienceOrb the xp orb carrying the amount of xp to be used when repaired
		/// @param stack the stack to be repaired
		/// @param player the player picking up the xp orb
		/// @param amount the amount of leftover experience after the repair
		/// @return `true` to allow further processing, `false` to cancel any other action
		///
		boolean onRepair(ExperienceOrbEntity experienceOrb, ItemStack stack, ServerPlayerEntity player, int amount);
	}
}
