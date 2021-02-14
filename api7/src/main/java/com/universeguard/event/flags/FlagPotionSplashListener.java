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
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the potionsplash flag
 * @author Jimi
 *
 */
public class FlagPotionSplashListener {
	
	@Listener
	public void onPotionSplash(InteractItemEvent.Secondary event, @Root Player player) {
		if(event.getItemStack().getType().equals(ItemTypes.SPLASH_POTION) || event.getItemStack().getType().equals(ItemTypes.LINGERING_POTION))
			this.handleEvent(event, player.getLocation(), player);
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.POTION_SPLASH, location, player, RegionEventType.LOCAL);
	}
}
