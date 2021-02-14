package com.universeguard.event.flags;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
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

public class FlagIceMeltListener {

	@Listener
	public void onIceMelt(ChangeBlockEvent.Place event, @Root LocatableBlock block) {
		BlockType blockType = event.getTransactions().get(0).getFinal().getState().getType();
		if(!event.getTransactions().isEmpty() 
			&& block.getBlockState().getType().equals(BlockTypes.ICE)
			&& (blockType.equals(BlockTypes.WATER) ||
				blockType.equals(BlockTypes.FLOWING_WATER) ||
				blockType.equals(BlockTypes.AIR))) {
			this.handleEvent(event, block.getLocation());
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.ICE_MELT, location, null, RegionEventType.GLOBAL);
	}
}
