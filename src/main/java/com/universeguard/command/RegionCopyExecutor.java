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
import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg copy
 * @author Jimi
 *
 */
public class RegionCopyExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if (args.hasAny("name") && args.hasAny("newRegion")) {
				Region region = RegionUtils.load(args.<String>getOne("name").get());
				if (region != null) {
					if(region.isLocal()) {
						String newRegion = args.<String>getOne("newRegion").get();
						if(RegionUtils.load(newRegion) == null) {
							if(RegionUtils.save(RegionUtils.copy((LocalRegion)region, newRegion))) {
								MessageUtils.sendSuccessMessage(player, RegionText.REGION_COPIED.getValue());	
							} else
								MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_COPIED.getValue());
						}
						else
							MessageUtils.sendErrorMessage(player, RegionText.REGION_NAME_NOT_VALID.getValue());
					} else
						MessageUtils.sendErrorMessage(player, RegionText.REGION_LOCAL_ONLY.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_FOUND.getValue());
			} else {
				MessageUtils.sendErrorMessage(player, getCommandUsage());
			}
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg copy <Region1> <Region2>";
	}

}
