/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

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
	public void onInteract(InteractBlockEvent.Secondary.MainHand event, @First Player player) {
		BlockType block = event.getTargetBlock().getState().getType();
		this.handleInteractBlock(event, block, player);
	}
	
	@Listener
	public void onInteract(InteractBlockEvent.Secondary.OffHand event, @First Player player) {
		BlockType block = event.getTargetBlock().getState().getType();
		this.handleInteractBlock(event, block, player);
	}
	
	@Listener
	public void onInteract(InteractEntityEvent.Primary.MainHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		if(entity.equals(EntityTypes.ITEM_FRAME))
			this.handleEvent(event, EnumRegionInteract.ITEM_FRAME, player);
	}
	
	@Listener
	public void onInteract(InteractEntityEvent.Primary.OffHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		if(entity.equals(EntityTypes.ITEM_FRAME))
			this.handleEvent(event, EnumRegionInteract.ITEM_FRAME, player);
	}

	@Listener
	public void onInteract(InteractEntityEvent.Secondary.MainHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		this.handleEntityInteract(event, entity, player);
	}
	
	@Listener
	public void onInteract(InteractEntityEvent.Secondary.OffHand event, @First Player player) {
		EntityType entity = event.getTargetEntity().getType();
		this.handleEntityInteract(event, entity, player);
	}
	
	private void handleInteractBlock(InteractBlockEvent.Secondary event, BlockType block, Player player) {
		EnumRegionInteract interact = FlagUtils.getInteract(block);
		this.handleEvent(event, interact, player);
	}
	
	private void handleEntityInteract(InteractEntityEvent.Secondary event, EntityType entity, Player player) {
		EnumRegionInteract interact = FlagUtils.getInteract(entity);
		this.handleEvent(event, interact, player);
	}
	
	private void handleEvent(Cancellable event, EnumRegionInteract interact, Player player) {
		Region region = RegionUtils.getRegion(player.getLocation());
		if(region != null) {
			boolean cancel = false;
			if(interact != null) {
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
}
