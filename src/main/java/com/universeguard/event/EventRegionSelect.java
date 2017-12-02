/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.enums.RegionPoint;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.InventoryUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionLocationUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the Region selection
 * @author Jimi
 *
 */
public class EventRegionSelect {

	@Listener
	public void onRightClick(InteractBlockEvent.Primary.MainHand event) {
		this.setPoint(RegionPoint.PRIMARY, HandTypes.MAIN_HAND, event);
	}

	@Listener
	public void onRightClick(InteractBlockEvent.Primary.OffHand event) {
		this.setPoint(RegionPoint.PRIMARY, HandTypes.OFF_HAND, event);
	}

	@Listener
	public void onRightClick(InteractBlockEvent.Secondary.MainHand event) {
		this.setPoint(RegionPoint.SECONDARY, HandTypes.MAIN_HAND, event);
	}

	@Listener
	public void onRightClick(InteractBlockEvent.Secondary.OffHand event) {
		this.setPoint(RegionPoint.SECONDARY, HandTypes.OFF_HAND, event);
	}

	private void setPoint(RegionPoint point, HandType hand, InteractBlockEvent event) {
		if (event.getCause().last(Player.class).isPresent()) {
			Player player = event.getCause().last(Player.class).get();
			if (player.getItemInHand(hand).isPresent()) {
				ItemStack itemStack = player.getItemInHand(hand).get();
				if (InventoryUtils.isSelector(itemStack)) {
					if(PermissionUtils.hasAllPermissions(player)) {
						event.setCancelled(true);
						BlockSnapshot block = event.getTargetBlock();
						BlockType blockType = block.getState().getType();
						RegionLocation location = null;
						if (blockType.equals(BlockTypes.AIR))
							location = RegionLocationUtils.fromLocation(player, player.getLocation());
						else
							location = RegionLocationUtils.fromLocation(player, block.getLocation().get());
						LocalRegion region = null;
						if (!RegionUtils.hasPendingRegion(player)) {
							region = new LocalRegion("");
						} else {
							region = (LocalRegion) RegionUtils.getPendingRegion(player);
							RegionUtils.setPendingRegion(player, null);
						}
						if (point.equals(RegionPoint.PRIMARY)) {
							region.setFirstPoint(location);
							MessageUtils.sendHotbarMessage(player,
									RegionText.FIRST_POINT_SET.getValue() + " " + location.toString());
						} else {
							region.setSecondPoint(location);
							MessageUtils.sendHotbarMessage(player,
									RegionText.SECOND_POINT_SET.getValue() + " " + location.toString());
						}
						RegionUtils.setPendingRegion(player, region);
					}
					else
						MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_ITEM.getValue());
				}
			}

		}
	}
}
