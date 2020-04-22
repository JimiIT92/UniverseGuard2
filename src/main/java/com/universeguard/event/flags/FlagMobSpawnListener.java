/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.utils.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnType;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;

import java.util.stream.Collectors;

/**
 * Handler for the mobspawn flag
 * @author Jimi
 *
 */
public class FlagMobSpawnListener {

	@Listener
	public void onMobSpawn(SpawnEntityEvent event, @Root Entity cause) {
		if(!event.getEntities().isEmpty()) {
			if(cause instanceof Player)
				this.handleEvent(event, event.getEntities().get(0), (Player)cause);
			else
				this.handleEvent(event, event.getEntities().get(0), null);
		}
	}
	
	@Listener
	public void onMobSpawn(SpawnEntityEvent event) {
		if(event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {
			SpawnType type = event.getContext().get(EventContextKeys.SPAWN_TYPE).get();
			if(!event.getEntities().isEmpty() && !(type.equals(SpawnTypes.PLACEMENT)) && 
					!(type.equals(SpawnTypes.DROPPED_ITEM)) && !(type.equals(SpawnTypes.CHUNK_LOAD))) {
				this.handleEvent(event, event.getEntities().get(0), null);
			}	
		}
	}
	
	private void handleEvent(SpawnEntityEvent event, Entity entity, Player player)
	{
		EntityType type = entity.getType();
		if(!FlagUtils.isBlockEntity(type) && !FlagUtils.isVehicle(type) && entity instanceof Living)
		{
			String name = type.getId().toLowerCase();
			Region region = RegionUtils.getRegion(entity.getLocation());
			if(region != null) {
				boolean relatedAllFlag = !region.getMobSpawn("all");
				if(entity instanceof Monster)
					relatedAllFlag =  relatedAllFlag || !region.getMobSpawn("allhostile");
				if(entity instanceof Creature || entity instanceof Animal)
					relatedAllFlag =  relatedAllFlag || !region.getMobSpawn("allpassive");
				boolean cancel = relatedAllFlag || !region.getMobSpawn(name);
				if(player != null)
					cancel = cancel && !PermissionUtils.hasPermission(player, RegionPermission.REGION);
				if(cancel) {
					event.setCancelled(true);
					if(player != null)
						MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
				}
			}
		}
	}
}
