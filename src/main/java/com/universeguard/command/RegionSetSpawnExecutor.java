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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionLocationUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg setspawn
 * @author Jimi
 *
 */
public class RegionSetSpawnExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player) src;
			if(RegionUtils.hasPendingRegion(player)) {
				if(args.hasAny("location")) {
					Location<World> location = args.<Location<World>>getOne("location").get();
					Region region = RegionUtils.getPendingRegion(player);
					if(region.isLocal()) {
						((LocalRegion)region).setSpawnLocation(RegionLocationUtils.fromLocation(player, location));
						MessageUtils.sendSuccessMessage(player, RegionText.REGION_SPAWN_LOCATION_UPDATED.getValue());
						RegionUtils.updatePendingRegion(player, region);	
					}
					else
						MessageUtils.sendErrorMessage(player, RegionText.REGION_LOCAL_ONLY.getValue());
				}
				else {
					MessageUtils.sendErrorMessage(player, getCommandUsage());
				}
				
			} else
				MessageUtils.sendErrorMessage(player, RegionText.NO_PENDING_REGION.getValue());
		}
		
		return CommandResult.empty();
	}
	
	private String getCommandUsage() {
		return "/rg setspawn <location>";
	}

}
