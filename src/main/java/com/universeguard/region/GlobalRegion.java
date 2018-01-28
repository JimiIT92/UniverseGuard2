/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.RegionType;

/**
 * Global Region Class
 * @author Jimi
 *
 */
public class GlobalRegion extends Region {

	/**
	 * Global Region Constructor
	 * @param name The Region name
	 */
	public GlobalRegion(String name) {
		super(RegionType.GLOBAL, name);
	}
	
	/**
	 * Get the World object for the Region
	 * @return The Region World Object
	 */
	public World getWorld() {
		return Sponge.getServer().getWorld(this.getName()).get();
	}
}
