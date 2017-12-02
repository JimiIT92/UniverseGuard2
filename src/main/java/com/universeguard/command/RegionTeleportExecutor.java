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
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg tp
 * @author Jimi
 *
 */
public class RegionTeleportExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if (args.hasAny("name")) {
				Region region = RegionUtils.load(args.<String>getOne("name").get());
				if (region != null) {
					if(region.isLocal())
					{
						if(region.getFlag(EnumRegionFlag.CAN_TP))
							player.setLocation(((LocalRegion)region).getTeleportLocation().getLocation());
						else if(!PermissionUtils.hasPermission(player, RegionPermission.REGION))
							MessageUtils.sendErrorMessage(player, RegionText.REGION_NO_TP.getValue());
					}
						
					else
						MessageUtils.sendErrorMessage(player, RegionText.REGION_LOCAL_ONLY.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_FOUND.getValue());
			} else {
				MessageUtils.sendErrorMessage(player, getCommandUsage());
			}
		}

		return CommandResult.empty();
	}
	/**
	 * Command Usage
	 * @return
	 */
	private String getCommandUsage() {
		return "/rg tp <name>";
	}

}
