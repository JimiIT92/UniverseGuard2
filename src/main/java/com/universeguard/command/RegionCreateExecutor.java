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
import org.spongepowered.api.world.DimensionType;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg create
 * @author Jimi
 *
 */
public class RegionCreateExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("x1") && args.hasAny("y1") && args.hasAny("z1") && args.hasAny("x2") && args.hasAny("y2") && args.hasAny("z2") && args.hasAny("name") && args.hasAny("dimension")) {
			DimensionType dimension = args.<DimensionType>getOne("dimension").get();
			String world = args.<String>getOne("world").get();
			String name = args.<String>getOne("name").get();
			RegionLocation firstPoint = new RegionLocation(args.<Integer>getOne("x1").get(), args.<Integer>getOne("y1").get(), args.<Integer>getOne("z1").get(), dimension.getId(), world);
			RegionLocation secondPoint = new RegionLocation(args.<Integer>getOne("x2").get(), args.<Integer>getOne("y2").get(), args.<Integer>getOne("z2").get(), dimension.getId(), world);
			LocalRegion region = new LocalRegion(name, firstPoint, secondPoint);
			if(RegionUtils.save(region))
				MessageUtils.sendSuccessMessage(src, RegionText.REGION_SAVED.getValue());
		}
		return CommandResult.empty();
	}
}