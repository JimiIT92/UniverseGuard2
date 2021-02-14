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
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg name
 * @author Jimi
 *
 */
public class RegionNameExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(RegionUtils.hasPendingRegion(src)) {
			if(args.hasAny("name")) {
				String name = args.<String>getOne("name").get();
				if(RegionUtils.load(name) == null) {
					Region region = RegionUtils.getPendingRegion(src);
					region.setName(name);
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_NAME_UPDATED.getValue());
					RegionUtils.updatePendingRegion(src, region);
				}
				else
					MessageUtils.sendErrorMessage(src, RegionText.REGION_NAME_NOT_VALID.getValue());
			}
			else {
				MessageUtils.sendErrorMessage(src, getCommandUsage());
			}
			
		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
		
		return CommandResult.empty();
	}
	
	private String getCommandUsage() {
		return "/rg name <value>";
	}

}