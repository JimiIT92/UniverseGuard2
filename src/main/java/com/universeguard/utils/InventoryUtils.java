/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import java.util.Arrays;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.UniverseGuard;

/**
 * 
 * Utility class for inventory
 * @author Jimi
 *
 */
public class InventoryUtils {
	
	/**
	 * Get the selector ItemStack
	 * @return The Region Selector ItemStack
	 */
	public static ItemStack getSelector() {
		ItemStack selector = getItemStack(UniverseGuard.SELECTOR_ITEM);
		selector.offer(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Region Selector"));
		selector.offer(Keys.ITEM_ENCHANTMENTS, Arrays.asList(Enchantment.of(EnchantmentTypes.INFINITY, 1)));
		selector.offer(Keys.UNBREAKABLE, true);
		selector.offer(Keys.HIDE_ENCHANTMENTS, true);
		return selector.createSnapshot().createStack();
	}

	/**
	 * Check if an ItemStack is the Region Selector ItemStack
	 * @param itemStack The ItemStack
	 * @return true if the ItemStack is the Region Selector, false otherwise
	 */
	public static boolean isSelector(ItemStack itemStack) {
		ItemStack selector = getSelector();
		if(itemStack.get(Keys.DISPLAY_NAME).isPresent()) {
			Text text = itemStack.get(Keys.DISPLAY_NAME).get();
			Text selectorText = selector.get(Keys.DISPLAY_NAME).get();
			return text.equals(selectorText) && selector.getType().equals(itemStack.getType());
		}
		return false;
	}

	/**
	 * Add an ItemStack to a player's inventory
	 * @param player The Player
	 * @param itemStack The ItemStack
	 */
	public static boolean addItemStackToInventory(Player player, ItemStack itemStack) {
		return player.getInventory().offer(itemStack).getType().equals(InventoryTransactionResult.Type.SUCCESS);
	}

    public static boolean addItemsToInventory(Player player, ItemType itemType, int quantity) {
        return addItemStackToInventory(player, ItemStack.of(itemType, quantity));
    }

	/**
	 * Add an ItemStack to a player's hotbar
	 * @param player The Player
	 * @param itemStack The ItemStack
	 */
	public static boolean addItemStackToHotbar(Player player, ItemStack itemStack) {
		Inventory inventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
		if(inventory instanceof Hotbar) {
			Hotbar hotbar = (Hotbar)inventory;
			if(hotbar.set(new SlotIndex(hotbar.getSelectedSlotIndex()), itemStack).getType().equals(InventoryTransactionResult.Type.FAILURE))
				return addItemStackToInventory(player, itemStack);
			return true;
		}
		return false;
	}

	public static boolean removeFromInventory(Player player, ItemStack itemStack) {
        Inventory items = player.getInventory().query(QueryOperationTypes.ITEM_TYPE.of(itemStack.getType()));
        if (items.peek(itemStack.getQuantity()).isPresent()) {
            items.poll(itemStack.getQuantity());
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * Create an ItemStack from an ItemType
	 * @param item The ItemType
	 * @return The ItemStack for the ItemType
	 */
	public static ItemStack getItemStack(ItemType item) {
		return ItemStack.builder().itemType(item).quantity(1).build();
	}
	
}
