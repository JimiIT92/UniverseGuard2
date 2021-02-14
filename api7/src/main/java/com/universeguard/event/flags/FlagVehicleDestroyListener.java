/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the vehicledestroy flag
 * @author Jimi
 *
 */
public class FlagVehicleDestroyListener {

	@Listener
	public void onInteract(InteractEntityEvent.Primary event, @First Player player) {
		EntityType type = event.getTargetEntity().getType();
		if(FlagUtils.isVehicle(type)) {
			EnumRegionVehicle vehicle = FlagUtils.getVehicle(type);
			this.handleEvent(event, vehicle, player);	
		}
	}
	
	private void handleEvent(InteractEntityEvent.Primary event, EnumRegionVehicle vehicle, Player player) {
		Region region = RegionUtils.getRegion(player.getLocation());
		if(region != null && vehicle != null) {
			boolean cancel = !region.getVehicleDestroy(vehicle) && !RegionUtils.hasPermission(player, region);
			if(cancel) {
				event.setCancelled(true);
				if(player != null)
					MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
			}
		}
		
	}

}
