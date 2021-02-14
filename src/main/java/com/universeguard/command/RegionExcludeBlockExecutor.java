/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionBlock;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * 
 * Command Handler for /rg add
 * @author Jimi
 *
 */
public class RegionExcludeBlockExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("block") && args.hasAny("type")) {
            BlockType block = args.<BlockType>getOne("block").get();
            EnumRegionBlock type = args.<EnumRegionBlock>getOne("type").get();
            Region region = RegionUtils.getPendingRegion(src);
            if(region != null) {
                region.excludeBlock(block, type);
                MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
            } else {
                MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
            }
		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg add <role> <player> (region)";
	}
	
}