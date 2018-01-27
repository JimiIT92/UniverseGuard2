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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the mobpve flag
 * @author Jimi
 *
 */
public class FlagMobPveListener {

	@Listener
	public void onMobPve(DamageEntityEvent event, @First EntityDamageSource source) {
		if(!(event.getTargetEntity() instanceof Player) && source.getSource() instanceof Player) {
			this.handleEvent(event, event.getTargetEntity(), (Player) source.getSource());
		}
	}
	
	private void handleEvent(DamageEntityEvent event, Entity entity, Player player)
	{
		EntityType type = entity.getType();
		if(!FlagUtils.isBlockEntity(type) && !FlagUtils.isExplosion(type) && !FlagUtils.isVehicle(type))
		{
			String name = type.getId().toLowerCase();
			Region region = RegionUtils.getRegion(entity.getLocation());
			if(region != null) {
				boolean cancel = !region.getMobPve("all") || !region.getMobPve(name) && !PermissionUtils.hasPermission(player, RegionPermission.REGION);
				if(cancel) {
					event.setCancelled(true);
					MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
				}
			}
		}
		
	}
}
