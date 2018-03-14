/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the Farewell message
 * @author Jimi
 *
 */
public class FlagGreetingListener {
	
	@Listener
	public void onFarewell(MoveEntityEvent event) {
		if(event.getTargetEntity() instanceof Player)
			this.handleEvent(event, (Player)event.getTargetEntity());
		else if(!event.getTargetEntity().getPassengers().isEmpty()) {
			for(Entity entity : event.getTargetEntity().getPassengers()) {
				if(entity instanceof Player) {
					this.handleEvent(event, (Player)entity);
				}
			}
		}
	}
	
	public void handleEvent(MoveEntityEvent event, Player player)
	{
		Location<World> to = event.getToTransform().getLocation();
		Region regionTo = RegionUtils.getRegion(to);
		if(regionTo.isLocal()) {
			Location<World> from = event.getFromTransform().getLocation();
			Region regionFrom = RegionUtils.getRegion(from);
			if(regionFrom != null && regionTo != null && regionFrom != regionTo && regionTo.getFlag(EnumRegionFlag.ENTER) &&
					!((LocalRegion)regionTo).getGreetingMessage().isEmpty()) {
				MessageUtils.sendHotbarSuccessMessage(player, ((LocalRegion)regionTo).getGreetingMessage());
			}
		}
	}
	
}
