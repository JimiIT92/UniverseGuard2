/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import com.universeguard.region.GlobalRegion;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.World;

import java.util.Optional;

/**
 * 
 * Command Handler for /rg create
 * @author Jimi
 *
 */
public class RegionGlobalForExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("dimension")) {
			String dimension = args.<String>getOne("dimension").get();
			if(RegionUtils.load(dimension) != null) {
				MessageUtils.sendErrorMessage(src, RegionText.REGION_DIMENSION_EXISTS.getValue());
			}
			else {
				Optional<World> optionalWorld = Sponge.getServer().getWorld(dimension);
				if(optionalWorld.isPresent()) {
					World dimWorld = optionalWorld.get();
					GlobalRegion dimRegion = new GlobalRegion(dimWorld.getName(), false);
					if(RegionUtils.save(dimRegion)) {
						MessageUtils.sendSuccessMessage(src, RegionText.REGION_SAVED.getValue());
					} else {
						MessageUtils.sendErrorMessage(src, RegionText.REGION_SAVE_EXCEPTION.getValue());
					}
				}
				else {
					MessageUtils.sendErrorMessage(src, RegionText.DIMENSION_NOT_FOUND.getValue());
				}
			}
		}
		else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}
		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg globalfor <dimension-id>";
	}
}