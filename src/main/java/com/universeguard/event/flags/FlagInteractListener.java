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
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionInteract;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the interact flag
 * @author Jimi
 *
 */
public class FlagInteractListener {
	
	@Listener
	public void onInteractBlockSecondaryMainhand(InteractBlockEvent.Secondary.MainHand event, @First Player player) {
		BlockType block = event.getTargetBlock().getState().getType();
		if(event.getTargetBlock().getLocation().isPresent() && !block.equals(BlockTypes.AIR)) {
			EnumRegionInteract interact = FlagUtils.getInteract(block);
			this.handleEvent(event, event.getTargetBlock().getLocation().get(), interact, player);
		}
	}
	
	@Listener
	public void onInteractBlockSecondaryOffhand(InteractBlockEvent.Secondary.OffHand event, @First Player player) {
		BlockType block = event.getTargetBlock().getState().getType();
		if(event.getTargetBlock().getLocation().isPresent() && !block.equals(BlockTypes.AIR)) {
			EnumRegionInteract interact = FlagUtils.getInteract(block);
			this.handleEvent(event, event.getTargetBlock().getLocation().get(), interact, player);
		}
	}
	
	@Listener
	public void onInteractEntityPrimaryMainhand(InteractEntityEvent.Primary.MainHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		if(entity.equals(EntityTypes.ITEM_FRAME))
			this.handleEvent(event, event.getTargetEntity().getLocation(), EnumRegionInteract.ITEM_FRAME, player);
	}
	
	@Listener
	public void onInteractEntityPrimaryOffhand(InteractEntityEvent.Primary.OffHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		if(entity.equals(EntityTypes.ITEM_FRAME))
			this.handleEvent(event, event.getTargetEntity().getLocation(), EnumRegionInteract.ITEM_FRAME, player);
	}

	@Listener
	public void onInteractEntitySecondaryMainhand(InteractEntityEvent.Secondary.MainHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		EnumRegionInteract interact = FlagUtils.getInteract(entity);
		this.handleEvent(event, event.getTargetEntity().getLocation(), interact, player);
	}
	
	@Listener
	public void onInteractEntitySecondaryOffhand(InteractEntityEvent.Secondary.OffHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		EnumRegionInteract interact = FlagUtils.getInteract(entity);
		this.handleEvent(event, event.getTargetEntity().getLocation(), interact, player);
	}
	
	private void handleEvent(Cancellable event, Location<World> location, EnumRegionInteract interact, Player player) {
		Region region = RegionUtils.getRegion(location);
		if(region != null && interact != null) {
			boolean cancel = false;
			if(region.isLocal())
				cancel = !region.getInteract(interact) && !RegionUtils.hasPermission(player, region);
			else
				cancel = !region.getInteract(interact) && !PermissionUtils.hasPermission(player, RegionPermission.REGION);
			if(cancel) {
				event.setCancelled(true);
				if(player != null)
					MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
			}
		}
		
	}
}
