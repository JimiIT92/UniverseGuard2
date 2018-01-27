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
 * Command Handler for /rg save
 * @author Jimi
 *
 */
public class RegionSaveExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			if(RegionUtils.hasPendingRegion(player)) {
				Region region = RegionUtils.getPendingRegion(player);
				if(region.isLocal()) {
					LocalRegion localRegion = (LocalRegion) region;
					if(localRegion.getFirstPoint() == null || localRegion.getSecondPoint() == null) {
						MessageUtils.sendErrorMessage(player, RegionText.REGION_SELECT_POINTS.getValue());
						return CommandResult.empty();
					}
				}
				if(RegionUtils.save(region, false)) {
					RegionUtils.setPendingRegion(player, null);
					MessageUtils.sendSuccessMessage(player, RegionText.REGION_SAVED.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_SAVED.getValue());
			} else
				MessageUtils.sendErrorMessage(player, RegionText.NO_PENDING_REGION.getValue());
		}
		return CommandResult.empty();
	}

}
