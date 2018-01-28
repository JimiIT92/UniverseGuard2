/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import java.util.ArrayList;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the explosiondestroy flag
 * 
 * @author Jimi
 *
 */
public class FlagExplosionDestroyListener {

	@Listener
	public void onExplosionDamage(ExplosionEvent.Detonate event) {
		if (event.getExplosion().getSourceExplosive().isPresent()) {
			EntityType entity = event.getExplosion().getSourceExplosive().get().getType();
			ArrayList<Region> regions = new ArrayList<Region>();
			ArrayList<Location<World>> locations = new ArrayList<Location<World>>();
			if (FlagUtils.isExplosion(entity)) {
				EnumRegionExplosion explosion = FlagUtils.getExplosion(entity);
				for (Location<World> location : event.getAffectedLocations()) {
					if (event.getTargetWorld().getBlock(location.getBlockPosition()).getType() != BlockTypes.AIR) {
						Region region = RegionUtils.getRegion(location);
						if (region != null && !regions.contains(region)) {
							if (!region.getExplosionDestroy(explosion)) {
								regions.add(region);
								locations.add(location);
							}
						}
					}
				}
			} else {
				for (Location<World> location : event.getAffectedLocations()) {
					if (event.getTargetWorld().getBlock(location.getBlockPosition()).getType() != BlockTypes.AIR) {
						Region region = RegionUtils.getRegion(location);
						if (region != null && !regions.contains(region)) {
							if (!region.getExplosionDestroy(EnumRegionExplosion.OTHER_EXPLOSIONS)) {
								regions.add(region);
								locations.add(location);
							}
						}
					}
				}
			}
			event.getAffectedLocations().removeAll(locations);
		}
	}
}
