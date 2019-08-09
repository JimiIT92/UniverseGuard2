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
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

import java.util.Optional;

/**
 * Handler for the lighter flag
 * @author Jimi
 *
 */
public class FlagLighterListener {
	
	@Listener
	public void onLighter(InteractBlockEvent.Secondary event, @First Player player) {
		Optional<ItemStackSnapshot> item = event.getContext().get(EventContextKeys.USED_ITEM);
		if(item.isPresent() && item.get().getType().equals(ItemTypes.FLINT_AND_STEEL)){
			this.handleEvent(event, player.getLocation(), player);
		}
	}

	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.LIGHTER, location, player, RegionEventType.LOCAL);
	}
}
