/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg help
 * @author Jimi
 *
 */
public class RegionHelpExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int page = 1;
		boolean flags = false;
		if(args.hasAny("page"))
			page = args.<Integer>getOne("page").get();
		if(args.hasAny("flags"))
			flags = args.<Boolean>getOne("flags").get();
		if(flags) {
			RegionUtils.printHelpPage(src, page);
		} else {
			RegionUtils.printFlagHelpPage(src, page);
		}

		return CommandResult.empty();
	}
}