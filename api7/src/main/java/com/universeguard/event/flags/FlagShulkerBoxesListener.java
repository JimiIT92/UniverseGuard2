/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Handler for the chests flag
 * @author Jimi
 *
 */
public class FlagShulkerBoxesListener {
	
	@Listener
	public void onShulkerBox(InteractBlockEvent.Secondary event, @First Player player) {
		if(isShulkerBox(event.getTargetBlock().getState().getType()))
			this.handleEvent(event, event.getTargetBlock().getLocation().get(), player);
	}

	private boolean isShulkerBox(BlockType type) {
	    return type.equals(BlockTypes.WHITE_SHULKER_BOX) || type.equals(BlockTypes.YELLOW_SHULKER_BOX) ||
                type.equals(BlockTypes.BLACK_SHULKER_BOX) || type.equals(BlockTypes.BLUE_SHULKER_BOX) ||
                type.equals(BlockTypes.BROWN_SHULKER_BOX) || type.equals(BlockTypes.CYAN_SHULKER_BOX) ||
                type.equals(BlockTypes.GRAY_SHULKER_BOX) || type.equals(BlockTypes.GREEN_SHULKER_BOX) ||
                type.equals(BlockTypes.LIGHT_BLUE_SHULKER_BOX) || type.equals(BlockTypes.LIME_SHULKER_BOX) ||
                type.equals(BlockTypes.MAGENTA_SHULKER_BOX) || type.equals(BlockTypes.ORANGE_SHULKER_BOX) ||
                type.equals(BlockTypes.PINK_SHULKER_BOX) || type.equals(BlockTypes.PURPLE_SHULKER_BOX) ||
                type.equals(BlockTypes.RED_SHULKER_BOX) || type.equals(BlockTypes.SILVER_SHULKER_BOX);
    }
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.SHULKER_BOXES, location, player, RegionEventType.LOCAL);
	}
}
