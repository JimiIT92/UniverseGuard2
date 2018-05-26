/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import com.universeguard.UniverseGuard;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.enums.EnumDirection;

/**
 * 
 * Utility class for region locations
 * @author Jimi
 *
 */
public class RegionLocationUtils {

	/**
	 * Get the dimension of the player
	 * @param player The player
	 * @return The dimension id the player is in
	 */
	public static String getDimension(Player player) {
		return player.getWorld().getDimension().getType().getId();
	}
	
	/**
	 * Get the world of the player
	 * @param player The player
	 * @return The world name the player is in
	 */
	public static String getWorld(Player player) {
		return player.getWorld().getName();
	}
	
	/**
	 * Get a RegionLocation from player and location
	 * @param player The Player
	 * @param location The Location
	 * @return The RegionLocation from player and location
	 */
	public static RegionLocation fromLocation(Player player, Location<World> location) {
		return new RegionLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), getDimension(player), getWorld(player));
	}

	public static boolean isMaxSize(LocalRegion region) {
	    if(UniverseGuard.LIMIT_REGIONS_SIZE) {
	        int distanceX = Math.abs(region.getFirstPoint().getX() - region.getSecondPoint().getX());
            int distanceY = Math.abs(region.getFirstPoint().getY() - region.getSecondPoint().getY());
            int distanceZ = Math.abs(region.getFirstPoint().getZ() - region.getSecondPoint().getZ());
            int maxsize = UniverseGuard.MAX_REGION_SIZE;
            return distanceX > maxsize || distanceY > maxsize || distanceZ > maxsize;
        }
	    return false;
    }

	/**
	 * Reset the first or the second point of a Local Region based on the given direction
	 * @param region The Region
	 * @param direction Where to expand the region
	 * @param hasBlocks If the region should expand of a given blocks
	 * @param blocks The numbe rof blocks to expand
	 */
	public static void expandPoint(LocalRegion region, EnumDirection direction, boolean hasBlocks, int blocks) {
		RegionLocation firstPoint = region.getFirstPoint();
		RegionLocation secondPoint = region.getSecondPoint();
		switch(direction) {
		case UP:
		default:
			if(firstPoint.getY() >= secondPoint.getY())
				region.setFirstPoint(new RegionLocation(firstPoint.getX(), hasBlocks ? firstPoint.getY() + blocks : 256, firstPoint.getZ(), firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX(), hasBlocks ? secondPoint.getY() + blocks : 256, secondPoint.getZ(), secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		case DOWN:
			if(firstPoint.getY() <= secondPoint.getY())
				region.setFirstPoint(new RegionLocation(firstPoint.getX(), hasBlocks ? firstPoint.getY() - blocks : 0, firstPoint.getZ(), firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX(), hasBlocks ? secondPoint.getY() - blocks : 0, secondPoint.getZ(), secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		case LEFT:
			if(firstPoint.getX() <= secondPoint.getX())
				region.setFirstPoint(new RegionLocation(firstPoint.getX() - blocks, firstPoint.getY(), firstPoint.getZ(), firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX() - blocks, secondPoint.getY(), secondPoint.getZ(), secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		case RIGHT:
			if(firstPoint.getX() >= secondPoint.getX())
				region.setFirstPoint(new RegionLocation(firstPoint.getX() + blocks, firstPoint.getY(), firstPoint.getZ(), firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX() + blocks, secondPoint.getY(), secondPoint.getZ(), secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		case FRONT:
			if(firstPoint.getZ() >= secondPoint.getZ())
				region.setFirstPoint(new RegionLocation(firstPoint.getX(), firstPoint.getY(), firstPoint.getZ() + blocks, firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX(), secondPoint.getY(), secondPoint.getZ() + blocks, secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		case BACK:
			if(firstPoint.getZ() <= secondPoint.getZ())
				region.setFirstPoint(new RegionLocation(firstPoint.getX(), firstPoint.getY(), firstPoint.getZ() - blocks, firstPoint.getDimension(), firstPoint.getWorld()));
			else
				region.setSecondPoint(new RegionLocation(secondPoint.getX(), secondPoint.getY(), secondPoint.getZ() - blocks, secondPoint.getDimension(), secondPoint.getWorld()));
			break;
		}
	}
}
