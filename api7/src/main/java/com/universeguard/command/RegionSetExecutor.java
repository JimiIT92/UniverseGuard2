/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.enums.RegionPoint;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg set
 * @author Jimi
 *
 */
public class RegionSetExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("point") && args.hasAny("x") && args.hasAny("y") && args.hasAny("z")) {
			RegionPoint point = RegionPoint.valueOf(args.<String>getOne("point").get().toUpperCase());
			int x = args.<Integer>getOne("x").get();
			int y = args.<Integer>getOne("y").get();
			int z = args.<Integer>getOne("z").get();
			
			Region region = null;
			if(RegionUtils.hasPendingRegion(src)) {
				region = RegionUtils.getPendingRegion(src);
				if(region.isGlobal()) {
					MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
					return CommandResult.empty();
				}
			} else
				region = new LocalRegion("");
			if(region != null) {
				LocalRegion localRegion = (LocalRegion)region;
				RegionLocation regionPoint = null;
				String dimension = "minecraft:overworld";
				String world = localRegion.getWorld() != null ? localRegion.getWorld().getName() : Sponge.getServer().getDefaultWorldName();
				if(localRegion.getFirstPoint() != null) {
					dimension = localRegion.getFirstPoint().getDimension();
				} else if(localRegion.getSecondPoint() != null) {
					dimension = localRegion.getSecondPoint().getDimension();
				}
				regionPoint = new RegionLocation(x, y, z, dimension, world);
				if(regionPoint != null) {
					switch(point) {
						case PRIMARY:
							localRegion.setFirstPoint(regionPoint);
							MessageUtils.sendSuccessMessage(src,
									RegionText.FIRST_POINT_SET.getValue() + " " + x + " " + y + " " + z);
							break;
						case SECONDARY:
							localRegion.setSecondPoint(regionPoint);
							MessageUtils.sendSuccessMessage(src,
									RegionText.SECOND_POINT_SET.getValue() + " " + x + " " + y + " " + z);
							break;
					}
				}
				RegionUtils.updatePendingRegion(src, localRegion);
				if(src instanceof Player)
					RegionUtils.setRegionScoreboard((Player)src, localRegion);
			}
				
		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}
		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg set <point> <x> <y> <z>";
	}
}