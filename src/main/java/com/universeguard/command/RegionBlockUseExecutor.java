/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.ItemType;

/**
 * 
 * Command Handler for /rg blockuse
 * @author Jimi
 *
 */
public class RegionBlockUseExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(RegionUtils.hasPendingRegion(src)) {
			if(args.hasAny("block") && args.hasAny("value")) {
                BlockType block = args.<BlockType>getOne("block").get();
                boolean value = Boolean.valueOf(args.<String>getOne("value").get());
                Region region = RegionUtils.getPendingRegion(src);
                if(value) {
                    region.allowBlock(block);
                    MessageUtils.sendSuccessMessage(src, RegionText.REGION_BLOCK_ENABLED.getValue() + ": " + block.getName());
                }
                else {
                    region.disallowBlock(block);
                    MessageUtils.sendSuccessMessage(src, RegionText.REGION_BLOCK_DISABLED.getValue() + ": " + block.getName());
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
		return "/rg blockuse <value> <item>";
	}

}