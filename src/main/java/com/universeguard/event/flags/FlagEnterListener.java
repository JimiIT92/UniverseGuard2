/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.utils.LogUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import com.flowpowered.math.vector.Vector3i;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.utils.DirectionUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.world.World;

/**
 * Handler for the enter flag
 * @author Jimi
 *
 */
public class FlagEnterListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			Region region = RegionUtils.getRegion(player.getLocation());
			if(region != null && region.isLocal() && !RegionUtils.hasPermission(player, region) && !region.getFlag(EnumRegionFlag.ENTER)) {
				//Direction direction = DirectionUtils.getPlayerDirection(player);
				player.setVelocity(player.getVelocity().mul(-2));
				player.getVehicle().ifPresent(Entity::clearPassengers);
			}
		}
	}
	
	private static Vector3i getOffset(Direction direction) {
		int offsetX = 0;
		int offsetY = 0;
		int offsetZ = 0;
		if(direction.equals(Direction.NORTH)) {
			offsetZ = -1;
		}
		else if(direction.equals(Direction.SOUTH)) {
			offsetZ = 1;
		}
		else if(direction.equals(Direction.EAST)) {
			offsetX = 1;
		}
		else if(direction.equals(Direction.WEST)) {
			offsetX = -1;
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
