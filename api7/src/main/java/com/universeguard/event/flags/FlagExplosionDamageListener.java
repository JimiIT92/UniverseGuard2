/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the explosiondamage flag
 * 
 * @author Jimi
 *
 */
public class FlagExplosionDamageListener {

	@Listener
	public void onExplosionDamage(DamageEntityEvent event, @First Explosive entity) {
		Region region = RegionUtils.getRegion(event.getTargetEntity().getLocation());
		if (region != null) {
			EnumRegionExplosion explosion = EnumRegionExplosion.OTHER_EXPLOSIONS;		
			if (FlagUtils.isExplosion(entity.getType())) 
				explosion = FlagUtils.getExplosion(entity.getType());
			event.setCancelled(!region.getExplosionDamage(explosion));
		}
	}
}
