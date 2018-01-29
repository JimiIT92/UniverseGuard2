/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import com.universeguard.region.enums.EnumDirection;

/**
 * 
 * Utility class for Directions
 * @author Jimi
 *
 */
public class DirectionUtils {
	
	/**
	 * Get a Direction from name
	 * @param name The name
	 * @return The Direction with that given name if exists, null otherwise
	 */
	public static EnumDirection getDirection(String name) {
		for(EnumDirection direction : EnumDirection.values()) {
			if(direction.name().equalsIgnoreCase(name))
				return direction;
		}
		return null;
	}

	/**
	 * Get a player's Direction
	 * @param playerSelf The player whose direction to get
	 * @return The Direction of the player
	 */
	public static Direction getPlayerDirection(Player playerSelf){
	    Direction dir;
	    double y = playerSelf.getTransform().getYaw();
	    if( y < 0 ){y += 360;}
	    y %= 360;
	    int i = (int)((y+8) / 22.5);
	    if(i >= 2 && i <= 6){dir = Direction.WEST;}
	    else if(i >= 7 && i <= 9){dir = Direction.NORTH;}
	    else if(i >= 10 && i <= 14){dir = Direction.EAST;}
	    else {dir = Direction.SOUTH;}
	    return dir;
	}
}
