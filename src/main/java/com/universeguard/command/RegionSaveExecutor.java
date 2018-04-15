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
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
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
		if(RegionUtils.hasPendingRegion(src)) {
			Region region = RegionUtils.getPendingRegion(src);
			if(region.isLocal()) {
				LocalRegion localRegion = (LocalRegion) region;
				if(localRegion.getFirstPoint() == null || localRegion.getSecondPoint() == null) {
					MessageUtils.sendErrorMessage(src, RegionText.REGION_SELECT_POINTS.getValue());
					return CommandResult.empty();
				}
				if(src instanceof Player) {
					Player player = (Player)src;
					if(PermissionUtils.hasPermission(player, RegionPermission.CREATE) || PermissionUtils.hasPermission(player, RegionPermission.EDIT))
						((LocalRegion)region).addMember(player, RegionRole.OWNER);	
				}
			}
			if(RegionUtils.save(region)) {
				if(src instanceof Player) {
					Player player = (Player)src;
					if(player.getScoreboard().getObjective("Region").isPresent())
						player.getScoreboard().removeObjective(player.getScoreboard().getObjective("Region").get());
				}
				RegionUtils.setPendingRegion(src, null);
				MessageUtils.sendSuccessMessage(src, RegionText.REGION_SAVED.getValue());
			} else
				MessageUtils.sendErrorMessage(src, RegionText.REGION_NOT_SAVED.getValue());
		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
		return CommandResult.empty();
	}

}
