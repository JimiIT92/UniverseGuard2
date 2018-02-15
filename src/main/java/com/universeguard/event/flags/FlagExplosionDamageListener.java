/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the explosiondamage flag
 * @author Jimi
 *
 */
public class FlagExplosionDamageListener {

	@Listener
	public void onExplosionDamage(DamageEntityEvent event, @Root DamageSource source) {
		if(source.isExplosive() && !(source instanceof EntityDamageSource)) {
			Region region = RegionUtils.getRegion(event.getTargetEntity().getLocation());
			if(region != null)
			{
				if(!FlagUtils.isBlockEntity(event.getTargetEntity().getType()))
					event.setCancelled(!region.getExplosionDamage(EnumRegionExplosion.OTHER_EXPLOSIONS));
				else
					event.setCancelled(true);
			}
		}
	}
	
	@Listener
	public void onExplosionDamage(DamageEntityEvent event, @Root IndirectEntityDamageSource source) {
		if(source.isExplosive()) {
			EntityType entity = source.getSource().getType();
			Region region = RegionUtils.getRegion(event.getTargetEntity().getLocation());
			if(region != null)
			{
				EnumRegionExplosion explosion = EnumRegionExplosion.OTHER_EXPLOSIONS;
				if(FlagUtils.isExplosion(entity)) {
					explosion = FlagUtils.getExplosion(entity);
				}
				if(!FlagUtils.isBlockEntity(event.getTargetEntity().getType()))
					event.setCancelled(!region.getExplosionDamage(explosion));
				else
					event.setCancelled(true);
			}
		}
	}
}
