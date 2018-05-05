/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

/**
 * 
 * Utility class for messages
 * @author Jimi
 *
 */
public class MessageUtils {

	/**
	 * Send a message to the player (written in white)
	 * @param player The player
	 * @param message The message
	 */
	public static void sendMessage(Player player, String message) {
		sendMessage(player, message, TextColors.WHITE);
	}
	
	/**
	 * Send an error message to the player (written in red)
	 * @param player The player
	 * @param message The error message
	 */
	public static void sendErrorMessage(Player player, String message) {
		sendMessage(player, message, TextColors.RED);
	}
	
	/**
	 * Send an error message to the CommandSource (written in red)
	 * @param source The command source
	 * @param message The error message
	 */
	public static void sendErrorMessage(CommandSource source, String message) {
		sendMessage(source, message, TextColors.RED);
	}
	
	/**
	 * Send a success message to the player (written in green)
	 * @param player The player
	 * @param message The success message
	 */
	public static void sendSuccessMessage(Player player, String message) {
		sendMessage(player, message, TextColors.GREEN);
	}

	/**
	 * Send a success message to the CommandSource (written in green)
	 * @param source The command source
	 * @param message The success message
	 */
	public static void sendSuccessMessage(CommandSource source, String message) {
		sendMessage(source, message, TextColors.GREEN);
	}
	
	/**
	 * Send a colored message to the player
	 * @param player The player
	 * @param message The message
	 * @param color The color
	 */
	public static void sendMessage(Player player, String message, TextColor color) {
		player.sendMessage(Text.of(color, message));
	}
	
	/**
	 * Send a colored message to the command source
	 * @param source The command source
	 * @param message The message
	 * @param color The color
	 */
	public static void sendMessage(CommandSource source, String message, TextColor color) {
		source.sendMessage(Text.of(color, message));
	}
	
	/**
	 * Send a message to the player's hotbar (written in gold)
	 * @param player The player
	 * @param message The message
	 */
	public static void sendHotbarMessage(Player player, String message) {
		sendHotbarMessage(player, message, TextColors.GOLD);
	}
	
	/**
	 * Send an error message to the player's hotbar (written in red)
	 * @param player The player
	 * @param message The error message
	 */
	public static void sendHotbarErrorMessage(Player player, String message) {
		sendHotbarMessage(player, message, TextColors.RED);
	}
	
	/**
	 * Send a success message to the player's hotbar (written in green)
	 * @param player The player
	 * @param message The success message
	 */
	public static void sendHotbarSuccessMessage(Player player, String message) {
		sendHotbarMessage(player, message, TextColors.GREEN);
	}
	
	/**
	 * Send a colored message to the player's hotbar (written in gold)
	 * @param player The player
	 * @param message The message
	 * @param color The color
	 */
	public static void sendHotbarMessage(Player player, String message, TextColor color) {
		player.sendMessage(ChatTypes.ACTION_BAR, Text.of(color, message));
	}
}