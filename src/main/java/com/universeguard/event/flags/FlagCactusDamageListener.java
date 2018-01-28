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
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the cactusdamage flag
 * @author Jimi
 *
 */
public class FlagCactusDamageListener {
	
	@Listener
	public void onCactusDamage(DamageEntityEvent event, @Root DamageSource source) {
		if(source.getType().equals(DamageTypes.CONTACT) && event.getTargetEntity() instanceof Player) {
			this.handleEvent(event, event.getTargetEntity().getLocation(), null);
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.CACTUS_DAMAGE, location, player, RegionEventType.GLOBAL);
	}
}
