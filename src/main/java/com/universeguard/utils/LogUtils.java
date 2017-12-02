/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.UniverseGuard;

/**
 * 
 * Utility class for log
 * @author Jimi
 *
 */
public class LogUtils {

	private static Logger LOGGER;
	private static ConsoleSource CONSOLE;
	
	/**
	 * Initialize the Logger
	 * @param log
	 */
	public static void init(Logger log) {
		LOGGER = log;
		CONSOLE = Sponge.getGame().getServer().getConsole();
	}
	
	/**
	 * Log an exception
	 * @param exception The Exception
	 */
	public static void log(Exception exception) {
		log(exception.getStackTrace().toString());
	}
	
	/**
	 * Log a message
	 * @param message The message
	 */
	public static void log(String message) {
		LOGGER.log(Level.INFO, UniverseGuard.NAME + ": " + message);
	}
	
	/**
	 * Print a message to the console (written in yellow)
	 * @param message The message
	 */
	public static void print(String message) {
		print(TextColors.YELLOW, message);
	}
	
	/**
	 * Print a colored message to the console
	 * @param color The color
	 * @param message The message
	 */
	public static void print(TextColor color, String message) {
		CONSOLE.sendMessage(Text.of(color, UniverseGuard.NAME, ": ", message));
	}
}
