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
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg flaginfo
 * @author Jimi
 *
 */
public class RegionFlagInfoExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("flag") && args.hasAny("name")) {
			Region region = RegionUtils.load(args.<String>getOne("name").get());
			if (region != null && !region.getFlag(EnumRegionFlag.HIDE_REGION)) {
				EnumRegionFlag flag = args.<EnumRegionFlag>getOne("flag").get();
				if(flag != null) {
					if(region.getFlag(EnumRegionFlag.HIDE_FLAGS))
						MessageUtils.sendMessage(src, RegionText.FLAG_HIDDEN.getValue(), TextColors.RED);
					else {
						boolean value = region.getFlag(flag);
						MessageUtils.sendMessage(src, String.valueOf(value), value ? TextColors.GREEN : TextColors.RED);	
					}
				} else
					MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
			} else
				MessageUtils.sendErrorMessage(src, RegionText.REGION_NOT_FOUND.getValue());
		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg info <name>";
	}

}