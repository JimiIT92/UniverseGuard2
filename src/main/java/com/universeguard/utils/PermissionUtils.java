/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.enums.RegionPermission;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

/**
 * 
 * Utility class for permissions
 * @author Jimi
 *
 */
public class PermissionUtils {
	
	/**
	 * Set permissions
	 * @param configNode
	 */
	public static void setDefaultValues(CommentedConfigurationNode configNode) {
		for(RegionPermission permission : RegionPermission.values()) {
			if(configNode.getNode("permissions", permission.getName()).isVirtual())
				configNode.getNode("permissions", permission.getName()).setValue(permission.getValue()).setComment(permission.getComment());
		}
	}
	
	/**
	 * Get permissions
	 * @param configNode
	 */
	public static void getValues(CommentedConfigurationNode configNode) {
		for(RegionPermission permission : RegionPermission.values()) {
			permission.setValue(configNode.getNode("permissions", permission.getName()).getString());
		}
	}
	
	/**
	 * If a player has a permission
	 * @param player The player
	 * @param permission The permission
	 * @return true if the player has all or the specified permission, false otherwise
	 */
	public static boolean hasPermission(Player player, RegionPermission permission) {
		return hasAllPermissions(player) || player.hasPermission(permission.getValue());
	}
	
	/**
	 * If a player has all permissions
	 * @param player The player
	 * @return true if the player has all permissions, false otherwise
	 */
	public static boolean hasAllPermissions(Player player) {
		return player.hasPermission("*") || player.hasPermission(RegionPermission.ALL.getValue()) || player.hasPermission("universeguard.*");
	}
}
