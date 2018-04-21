/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import java.util.ArrayList;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg at
 * @author Jimi
 *
 */
public class RegionAtExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if(args.hasAny("location")) {
				Location<World> location = args.<Location<World>>getOne("location").get();
				ArrayList<LocalRegion> regions = RegionUtils.getAllLocalRegionsAt(location);
				String regionList = "";
				for(LocalRegion region : regions) {
					if(!region.getFlag(EnumRegionFlag.HIDE_REGION))
						regionList += region.getName() + ", ";
				}
				if(!regionList.isEmpty())
					MessageUtils.sendMessage(player, RegionText.REGION.getValue() + ": " + regionList.substring(0, regionList.length() - 2), TextColors.GREEN);
				else
					MessageUtils.sendErrorMessage(player, RegionText.NO_REGION_AT.getValue());
			} else {
				MessageUtils.sendErrorMessage(player, getCommandUsage());
			}
			
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg at <location>";
	}
}
