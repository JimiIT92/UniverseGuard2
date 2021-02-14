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

import com.universeguard.UniverseGuard;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg
 * @author Jimi
 *
 */
public class RegionReloadExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		UniverseGuard.ALL_REGIONS.clear();
		UniverseGuard.ALL_REGIONS = RegionUtils.getAllRegions();
		MessageUtils.sendSuccessMessage(src, RegionText.REGION_RELOAD.getValue());
		return CommandResult.empty();
	}

}