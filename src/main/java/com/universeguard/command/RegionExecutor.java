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

import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.InventoryUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;

/**
 * 
 * Command Handler for /rg
 * @author Jimi
 *
 */
public class RegionExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			if(PermissionUtils.hasAllPermissions(player)) {
				if(InventoryUtils.addItemStackToHotbar(player, InventoryUtils.getSelector()))
					MessageUtils.sendHotbarSuccessMessage(player, RegionText.REGION_SELECTOR_ADDED.getValue());
				return CommandResult.success();
			}
			else
				MessageUtils.sendErrorMessage(player, RegionText.NO_PERMISSION_COMMAND.getValue());
		}
		return CommandResult.empty();
	}

}
