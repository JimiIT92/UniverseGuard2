/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the mobdamage flag
 * @author Jimi
 *
 */
public class FlagMobDamageListener {

	@Listener
	public void onMobDamage(DamageEntityEvent event, @First EntityDamageSource source) {
		if(event.getTargetEntity() instanceof Player && !(source.getSource() instanceof Player) && source.getSource() instanceof Living) {
			this.handleEvent(event, source.getSource());
		}
	}
	
	private void handleEvent(DamageEntityEvent event, Entity entity)
	{
		EntityType type = entity.getType();
		if(!FlagUtils.isBlockEntity(type) && !FlagUtils.isVehicle(type) && entity instanceof Living)
		{
			String name = type.getId().toLowerCase();
			Region region = RegionUtils.getRegion(entity.getLocation());
			if(region != null) {
				boolean relatedAllFlag = !region.getMobDamage("all");
				if(entity instanceof Monster)
					relatedAllFlag =  relatedAllFlag || !region.getMobDamage("allhostile");
				if(entity instanceof Creature || entity instanceof Animal)
					relatedAllFlag =  relatedAllFlag || !region.getMobDamage("allpassive");
				boolean cancel = relatedAllFlag || !region.getMobDamage(name);
				if(cancel) {
					event.setCancelled(true);
				}
			}
		}
		
	}
}
