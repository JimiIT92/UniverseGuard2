/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the enderchests flag
 * @author Jimi
 *
 */
public class FlagEnderChestsListener {
	
	@Listener
	public void onEnderChest(InteractBlockEvent.Secondary event, @First Player player) {
		if(event.getTargetBlock().getState().getType().equals(BlockTypes.ENDER_CHEST))
			this.handleEvent(event, event.getTargetBlock().getLocation().get(), player);
	}

	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.ENDER_CHESTS, location, player, RegionEventType.LOCAL);
	}
}
