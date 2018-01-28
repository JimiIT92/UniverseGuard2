/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the vehicleplace flag
 * @author Jimi
 *
 */
public class FlagVehiclePlaceListener {

	/*@Listener
	public void onInteract(SpawnEntityEvent event, @Root EntitySpawnCause cause, @First Player player) {
		if(cause.getType().equals(SpawnTypes.PLACEMENT) && !event.getEntities().isEmpty()) {
			EntityType type = event.getEntities().get(0).getType();
			if(FlagUtils.isVehicle(type)) {
				EnumRegionVehicle vehicle = FlagUtils.getVehicle(type);
				this.handleEvent(event, vehicle, player);	
			}
			
		}
	}*/
	
	private void handleEvent(SpawnEntityEvent event, EnumRegionVehicle vehicle, Player player) {
		Region region = RegionUtils.getRegion(player.getLocation());
		if(region != null) {
			boolean cancel = false;
			if(vehicle != null)
				cancel = !region.getVehiclePlace(vehicle) && !RegionUtils.hasPermission(player, region);
			if(cancel) {
				event.setCancelled(true);
				if(player != null)
					MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
			}
		}
		
	}

}
