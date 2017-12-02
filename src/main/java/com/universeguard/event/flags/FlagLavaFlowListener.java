/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the lavaflow flag
 * @author Jimi
 *
 */
public class FlagLavaFlowListener {
	
	@Listener
	public void onLavaFlow(ChangeBlockEvent.Pre event, @Root LocatableBlock block) {
		BlockType type = block.getBlockState().getType();
		if(type.equals(BlockTypes.LAVA) || type.equals(BlockTypes.FLOWING_LAVA))
			this.handleEvent(event, block.getLocation(), null);
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.LAVA_FLOW, location, player, RegionEventType.GLOBAL);
	}
}
