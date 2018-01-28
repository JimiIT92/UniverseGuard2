/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the invincible flag
 * @author Jimi
 *
 */
public class FlagInvincibleListener {
	
	@Listener
	public void onDamage(DamageEntityEvent event) {
		if(event.getTargetEntity() instanceof Player)
			this.handleEvent(event, event.getTargetEntity().getLocation(), null);
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.INVINCIBLE, location, player, RegionEventType.GLOBAL);
	}
}
