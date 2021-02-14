/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
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

	@Listener
	public void onPvp(InteractItemEvent event, @First Player player) {
		if(event.getContext().containsKey(EventContextKeys.ENTITY_HIT)) {
			if(handleEvent(event, player.getLocation(), player))
				event.setCancelled(true);
		}
	}

	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.INVINCIBLE, location, player, RegionEventType.GLOBAL);
	}
}
