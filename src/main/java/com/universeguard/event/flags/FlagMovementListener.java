/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import com.flowpowered.math.vector.Vector3i;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the hunger flag
 * @author Jimi
 *
 */
public class FlagMovementListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			Region region = RegionUtils.getRegion(player.getLocation());
			Direction direction = Direction.getClosestHorizontal(player.getTransform().getRotation());
			if(region != null && region.isLocal() && !region.getFlag(EnumRegionFlag.ENTER)) {
				player.setLocation(player.getLocation().sub(getOffset(direction)));
			}
		}
	}
	
	private static Vector3i getOffset(Direction direction) {
		int offsetX = 0;
		int offsetY = 0;
		int offsetZ = 0;
		if(direction.equals(Direction.NORTH)/* || direction.equals(Direction.NORTH_NORTHEAST) ||direction.equals(Direction.NORTH_NORTHWEST) 
				|| direction.equals(Direction.NORTHEAST) || direction.equals(Direction.NORTHWEST)*/) {
			offsetZ = 1;
		}
		else if(direction.equals(Direction.SOUTH)/* || direction.equals(Direction.SOUTH_SOUTHEAST) ||direction.equals(Direction.SOUTH_SOUTHWEST) 
				|| direction.equals(Direction.SOUTHEAST) || direction.equals(Direction.SOUTHWEST)*/) {
			offsetZ = -1;
		}
		else if(direction.equals(Direction.EAST)/* || direction.equals(Direction.EAST_NORTHEAST) ||direction.equals(Direction.EAST_SOUTHEAST)*/) {
			offsetX = -1;
		}
		else if(direction.equals(Direction.WEST)/* || direction.equals(Direction.WEST_NORTHWEST) ||direction.equals(Direction.WEST_SOUTHWEST)*/) {
			offsetX = 1;
		}
		else if(direction.equals(Direction.UP)) {
			offsetY = -1;
		}
		else if(direction.equals(Direction.DOWN)) {
			offsetY = 1;
		}
		return new Vector3i(offsetX, offsetY, offsetZ);
	}
	
}
