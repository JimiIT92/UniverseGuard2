/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * 
 * Utility class for Commands
 * @author Jimi
 *
 */
public class CommandUtils {

	/**
	 * Build a CommandSpec
	 * @param description Command description
	 * @param executor Command Executor
	 * @return The CommandSpec
	 */
	public static CommandSpec buildCommandSpec(String description, CommandExecutor executor) {
		return CommandSpec.builder().description(Text.of(description))
				.executor(executor)
		.build();
	}
	
	/**
	 * Build a CommandSpec
	 * @param description Command description
	 * @param executor Command Executor
	 * @param permission The permission to run this command
	 * @return The CommandSpec
	 */
	public static CommandSpec buildCommandSpec(String description, CommandExecutor executor, String permission) {
		return CommandSpec.builder().description(Text.of(description))
				.permission(permission)
				.executor(executor)
		.build();
	}
	
	/**
	 * Build a CommandSpec
	 * @param description Command description
	 * @param args Command arguments
	 * @return The CommandSpec
	 */
	public static CommandSpec buildCommandSpec(String description, CommandExecutor executor, CommandElement... args) {
		return CommandSpec.builder().description(Text.of(description))
				.executor(executor)
				.arguments(args)
		.build();
	}
	
	/**
	 * Build a CommandSpec
	 * @param description Command description
	 * @param executor Command Executor
	 * @param permission The permission to run this command
	 * @param args Command arguments
	 * @return The CommandSpec
	 */
	public static CommandSpec buildCommandSpec(String description, CommandExecutor executor, String permission, CommandElement... args) {
		return CommandSpec.builder().description(Text.of(description))
				.executor(executor)
				.permission(permission)
				.arguments(args)
		.build();
	}
	
	/**
	 * Check if a string si a command
	 * @param name The string
	 * @return true if that string is a command, false otherwise
	 */
	public static boolean isValid(String name) {
		return Sponge.getCommandManager().get(name).isPresent();
	}
	
}
