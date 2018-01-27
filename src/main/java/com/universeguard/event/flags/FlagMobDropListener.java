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
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import com.universeguard.region.Region;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the mobdrop flag
 * @author Jimi
 *
 */
public class FlagMobDropListener {
	
	@Listener
	public void onMobDrop(SpawnEntityEvent event, @Root EntitySpawnCause cause) {
		if(!(cause.getEntity() instanceof Player) && cause.getType().equals(SpawnTypes.DROPPED_ITEM) && !event.getEntities().isEmpty()) {
			Entity entity = cause.getEntity();
			EntityType type = entity.getType();
			if(!type.equals(EntityTypes.ITEM) && !FlagUtils.isBlockEntity(type) && !FlagUtils.isExplosion(type) && !FlagUtils.isVehicle(type))
				this.handleEvent(event, entity);
		}
	}
	
	private void handleEvent(SpawnEntityEvent event, Entity entity)
	{
		EntityType type = entity.getType();
		String name = type.getId().toLowerCase();
		Region region = RegionUtils.getRegion(entity.getLocation());
		if(region != null) {
			boolean cancel = !region.getMobDrop("all") || !region.getMobDrop(name);
			if(cancel) {
				event.setCancelled(true);
			}
		}
	}
}
