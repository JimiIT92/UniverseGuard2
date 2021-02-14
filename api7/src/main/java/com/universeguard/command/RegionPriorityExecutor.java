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

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg priority
 * @author Jimi
 *
 */
public class RegionPriorityExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(RegionUtils.hasPendingRegion(src)) {
			if(args.hasAny("priority")) {
				int priority = args.<Integer>getOne("priority").get();
				Region region = RegionUtils.getPendingRegion(src);
				if(region.isLocal()) {
					((LocalRegion)region).setPriority(priority);
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_PRIORITY_UPDATED.getValue());
					RegionUtils.updatePendingRegion(src, region);						
				}
				else
					MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
			}
			else {
				MessageUtils.sendErrorMessage(src, getCommandUsage());
			}
			
		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
		
		return CommandResult.empty();
	}
	
	private String getCommandUsage() {
		return "/rg priority <value>";
	}

}