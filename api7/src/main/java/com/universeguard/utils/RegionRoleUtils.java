/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import com.universeguard.region.enums.RegionRole;

/**
 * 
 * Utility class for region roles
 * @author Jimi
 *
 */
public class RegionRoleUtils {

	/**
	 * Get a Region Role from name
	 * @param name The name of the role
	 * @return RegionRole if exists, null otherwise
	 */
	public static RegionRole getRole(String name) {
		for(RegionRole role : RegionRole.values()) {
			if(role.getName().equalsIgnoreCase(name))
				return role;
		}
		return null;
	}
}
