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
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.SpawnType;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the itemdrop flag
 * @author Jimi
 *
 */
public class FlagItemDropListener {
	
	@Listener
	public void onItemDrop(DropItemEvent.Dispense event, @First Entity entity) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			this.handleEvent(event, player.getLocation(), player);
		}
		else
			this.handleEvent(event, entity.getLocation(), null);
	}

	@Listener
	public void onItemDropFromInventory(SpawnEntityEvent event, @First Player player, @Root SpawnType cause) {
		if(cause.equals(SpawnTypes.PLACEMENT) && !event.getEntities().isEmpty()) {
			Entity entity = event.getEntities().get(0);
			EntityType type = entity.getType();
			if(type.equals(EntityTypes.ITEM))
				this.handleEvent(event, player.getLocation(), player);
		}
	}
	
	@Listener
	public void onItemDrop(SpawnEntityEvent event, @First Player player, @Root SpawnType cause) {
		if(cause.equals(SpawnTypes.DROPPED_ITEM) && !event.getEntities().isEmpty()) {
			Entity entity = event.getEntities().get(0);
			EntityType type = entity.getType();
			if(type.equals(EntityTypes.ITEM))
				this.handleEvent(event, player.getLocation(), player);
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.ITEM_DROP, location, player, RegionEventType.GLOBAL);
	}
}
