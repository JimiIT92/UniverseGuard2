/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.*;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Handler for the place flag
 * @author Jimi
 *
 */
public class FlagFrostWalkerListener {


	@Listener
	public void onBlockPlacedByPlayer(ChangeBlockEvent.Place event) {
        if (!event.getTransactions().isEmpty()) {
            BlockSnapshot block = event.getTransactions().get(0).getFinal();
            BlockType type = block.getState().getType();
            if (block.getLocation().isPresent() && type.equals(BlockTypes.FROSTED_ICE)) {
                Region region = RegionUtils.getRegion(block.getLocation().get());
                if(region != null && !region.getFlag(EnumRegionFlag.FROST_WALKER)) {
                    event.setCancelled(true);
                }
            }
        }
	}

    @Listener
    public void onBlockPlacedByPlayer(ChangeBlockEvent.Place event, @First Player player) {
        if (!event.getTransactions().isEmpty()) {
            BlockSnapshot block = event.getTransactions().get(0).getFinal();
            BlockType type = block.getState().getType();
            if (block.getLocation().isPresent() && type.equals(BlockTypes.FROSTED_ICE)) {
                Region region = RegionUtils.getRegion(block.getLocation().get());
                if(region != null && !region.getFlag(EnumRegionFlag.FROST_WALKER)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
