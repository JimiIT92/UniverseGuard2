/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnType;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.LogUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the fields flag
 * @author Jimi
 *
 */
public class FlagFieldsListener {

	@Listener
	public void onCropDrop(SpawnEntityEvent event, @First LocatableBlock block) {
		if(event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {
			SpawnType spawn = event.getContext().get(EventContextKeys.SPAWN_TYPE).get();
			BlockType type = block.getBlockState().getType();
			if(spawn.equals(SpawnTypes.DROPPED_ITEM) && FlagUtils.isCrop(type))
				this.handleEvent(event, block.getLocation(), null);	
		}
	}
	
	@Listener
	public void onBlockDestroyedByPlayer(ChangeBlockEvent.Break event, @First Player player) {
		if (!event.getTransactions().isEmpty()) {
			BlockSnapshot block = event.getTransactions().get(0).getOriginal();
			LogUtils.print(block.getState().getType().getId());
			if (FlagUtils.isCrop(block.getState().getType()) && block.getLocation().isPresent()) {
				Location<World> location = block.getLocation().get();
				this.handleEvent(event,location, player);
			}
		}
	}

	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.FIELDS, location, player, RegionEventType.LOCAL);
	}
}
