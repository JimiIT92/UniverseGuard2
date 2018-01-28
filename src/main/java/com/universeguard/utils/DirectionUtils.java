/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

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
}
