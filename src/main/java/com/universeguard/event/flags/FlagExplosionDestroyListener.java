/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import java.util.ArrayList;
import java.util.Iterator;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
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
	public void onExplosionDestroy(ExplosionEvent.Detonate event) {
		EnumRegionExplosion explosion;
		if (event.getExplosion().getSourceExplosive().isPresent()) {
			EntityType entity = event.getExplosion().getSourceExplosive().get().getType();
			if (FlagUtils.isExplosion(entity)) {
				explosion = FlagUtils.getExplosion(entity);
			} else {
				explosion = EnumRegionExplosion.OTHER_EXPLOSIONS;
			}
		} else {
			explosion = EnumRegionExplosion.OTHER_EXPLOSIONS;
		}

		ArrayList<Vector3i> entityVectors = new ArrayList<Vector3i>();
		for (Entity entity : event.getEntities()) {
			if (FlagUtils.isBlockEntity(entity.getType())) {
				Region region = RegionUtils.getRegion(entity.getLocation());
				if (region != null) {
					if (!region.getExplosionDestroy(explosion)) {
						entityVectors.add(new Vector3i(entity.getLocation().getBlockX(),
								entity.getLocation().getBlockY(), entity.getLocation().getBlockZ()));
					}
				}
			}
		}
		
		Iterator<Location<World>> it = event.getAffectedLocations().iterator();
		while(it.hasNext()) {
			Location<World> location = it.next();
			if(!entityVectors.contains(new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ()))) {
				BlockState block = event.getTargetWorld().getBlock(location.getBlockPosition());
				if (block != null && block.getType() != BlockTypes.AIR) {
					Region region = RegionUtils.getRegion(location);
					if (region != null) {
						if (!region.getExplosionDestroy(explosion)) {
							it.remove();
						}
					}
				}
			}
			else {
				it.remove();
			}
		}
		
	}
}
