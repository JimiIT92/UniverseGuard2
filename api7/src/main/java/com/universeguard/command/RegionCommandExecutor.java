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

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.CommandUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg command
 * @author Jimi
 *
 */
public class RegionCommandExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(RegionUtils.hasPendingRegion(src)) {
			if(args.hasAny("command") && args.hasAny("value")) {
				String command = args.<String>getOne("command").get();
				boolean value = Boolean.valueOf(args.<String>getOne("value").get());
				Region region = RegionUtils.getPendingRegion(src);
				if(value) {
					region.enableCommand(command);
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_COMMAND_ENABLED.getValue() + ": " + command);
				}
				else {
					region.disableCommand(command);
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_COMMAND_DISABLED.getValue() + ": " + command);
				}

				RegionUtils.updatePendingRegion(src, region);
			}
			else {
				MessageUtils.sendErrorMessage(src, getCommandUsage());
			}
			
		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
		return CommandResult.empty();
	}
	
	private String getCommandUsage() {
		return "/rg command <value> <command>";
	}

}