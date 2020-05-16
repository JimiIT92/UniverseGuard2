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
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.*;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

/**
 * Handler for the place flag
 * @author Jimi
 *
 */
public class FlagPlaceListener {

	@Listener
	public void onEntityPlacedByPlayer(SpawnEntityEvent event, @First Player player) {
		if (!event.getEntities().isEmpty()) {
			Entity placedEntity = event.getEntities().get(0);
			EntityType type = placedEntity.getType();
			Location<World> location = placedEntity.getLocation();
			if (FlagUtils.isBlockEntity(type) || placedEntity instanceof TileEntity)
				if (this.handleEvent(event, location, player)) {
					if (player.gameMode().exists() && player.gameMode().get().equals(GameModes.SURVIVAL)) {
						ItemType item = ItemTypes.NONE;
						if (type.equals(EntityTypes.ENDER_CRYSTAL))
							item = ItemTypes.END_CRYSTAL;
						else if (type.equals(EntityTypes.ARMOR_STAND))
							item = ItemTypes.ARMOR_STAND;
						else if (type.equals(EntityTypes.ITEM_FRAME))
							item = ItemTypes.ITEM_FRAME;
						else if (type.equals(EntityTypes.PAINTING))
							item = ItemTypes.PAINTING;
						if (!item.equals(ItemTypes.NONE))
							InventoryUtils.addItemStackToInventory(player, InventoryUtils.getItemStack(item));
					}
				}
		}
	}

	@Listener
	public void onBucketUse(InteractItemEvent.Secondary event, @First Player player) {
		Optional<ItemStackSnapshot> item = event.getContext().get(EventContextKeys.USED_ITEM);
		if(item.isPresent() && (item.get().getType().equals(ItemTypes.WATER_BUCKET) || item.get().getType().equals(ItemTypes.LAVA_BUCKET))){
			this.handleEvent(event, player.getLocation(), player);
		}
	}

	@Listener
	public void onBlockPlacedByPlayer(ChangeBlockEvent.Place event, @First Player player) {
		if (!event.getTransactions().isEmpty()) {
			BlockSnapshot block = event.getTransactions().get(0).getFinal();
			BlockType type = block.getState().getType();
			if (block.getLocation().isPresent() && !type.equals(BlockTypes.FROSTED_ICE)) {
			    Region region = RegionUtils.getRegion(block.getLocation().get());
				if(region != null && FlagUtils.isExcludedFromPlace(region, type) && !PermissionUtils.hasPermission(player, RegionPermission.REGION)) {
				    if(region.getFlag(EnumRegionFlag.PLACE)) {
                        event.setCancelled(true);
                        MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
                    }
                } else {
                    this.handleEvent(event, block.getLocation().get(), player);
                }
			}
		}
	}

	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.PLACE, location, player, RegionEventType.LOCAL);
	}
}
