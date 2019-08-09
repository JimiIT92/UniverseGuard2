/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import java.util.Objects;
import java.util.Optional;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
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
	public void onLavaFlow(ChangeBlockEvent.Pre event) {
		if(!event.getLocations().isEmpty()) {
			BlockSnapshot block = event.getLocations().get(0).getExtent().createSnapshot(event.getLocations().get(0).getBlockX(), event.getLocations().get(0).getBlockY(), event.getLocations().get(0).getBlockZ());
			Location<World> location = event.getLocations().get(event.getLocations().size() - 1);
			BlockState state = block.getState();
			Optional<ItemStackSnapshot> item = event.getContext().get(EventContextKeys.USED_ITEM);
			if(!item.isPresent()) {
				if(location != null) {
					Optional<MatterProperty> matter = state.getProperty(MatterProperty.class);
					if(matter.isPresent() && Objects.equals(matter.get().getValue(), Matter.LIQUID)) {
						BlockType blockType = state.getType();
						if(blockType.equals(BlockTypes.LAVA) || blockType.equals(BlockTypes.FLOWING_LAVA)) {
							this.handleEvent(event, location);
						}
					}
				}
			}
		}
	}

	private boolean handleEvent(Cancellable event, Location<World> location) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.LAVA_FLOW, location, null, RegionEventType.GLOBAL);
	}
}
