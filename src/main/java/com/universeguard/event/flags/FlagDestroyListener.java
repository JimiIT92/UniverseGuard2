/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.LogUtils;
import com.universeguard.utils.MessageUtils;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Piston;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the destroy flag
 * @author Jimi
 *
 */
public class FlagDestroyListener {

	@Listener
	public void onEntityDestroyed(InteractEntityEvent.Primary event, @First Player player) {
		Entity targetEntity = event.getTargetEntity();
		EntityType type = targetEntity.getType();
		if(FlagUtils.isBlockEntity(type) || targetEntity instanceof TileEntity) {
			this.handleEvent(event, targetEntity.getLocation(), player);
		}
	}

	@Listener
	public void onEntityDestroyedItemDrop(DropItemEvent.Destruct event, @First Entity entity) {
		LogUtils.log(entity.getType().getId());
	}

	@Listener
	public void onBucketFill(InteractItemEvent.Secondary.MainHand event, @First Player player) {
		this.handleBucketFill(event, player);
	}
	
	@Listener
	public void onBucketFill(InteractItemEvent.Secondary.OffHand event, @First Player player) {
		this.handleBucketFill(event, player);
	}
	
	@Listener
	public void onBucketFill(InteractBlockEvent.Secondary event, @First Player player) {
		BlockType block = event.getTargetBlock().getState().getType();
		if(block.equals(BlockTypes.WATER) || block.equals(BlockTypes.FLOWING_WATER) ||
				block.equals(BlockTypes.LAVA) || block.equals(BlockTypes.FLOWING_LAVA)) {
			if(player.getItemInHand(event.getHandType()).isPresent()) {
				ItemType item = player.getItemInHand(event.getHandType()).get().getType();
				if(item.equals(ItemTypes.BUCKET) || item.equals(ItemTypes.WATER_BUCKET) || item.equals(ItemTypes.LAVA_BUCKET))
					this.handleEvent(event, player.getLocation(), player);	
			}
		}
	}

	private void handleBucketFill(InteractItemEvent.Secondary event, Player player) {
		ItemType item = event.getItemStack().getType();
		if(item.equals(ItemTypes.BUCKET) || item.equals(ItemTypes.WATER_BUCKET) || item.equals(ItemTypes.LAVA_BUCKET)) {
			if(event.getInteractionPoint().isPresent()) {
				BlockType block = player.getWorld().getBlock(event.getInteractionPoint().get().toInt()).getType();
				if(block.equals(BlockTypes.WATER) || block.equals(BlockTypes.FLOWING_WATER) ||
						block.equals(BlockTypes.LAVA) || block.equals(BlockTypes.FLOWING_LAVA))
					this.handleEvent(event, player.getLocation(), player);
			}
		}
	}
	
	@Listener
	public void onEntityCollide(CollideEntityEvent.Impact event, @First Player player) {
		if(!event.getEntities().isEmpty()) {
			Entity targetEntity = event.getEntities().get(0);
			EntityType type = targetEntity.getType();
			if(FlagUtils.isBlockEntity(type))
				this.handleEvent(event, targetEntity.getLocation(), player);
		}
	}

	@Listener
	public void onBlockDestroyedByPlayer(ChangeBlockEvent.Break event, @First Player player) {
		if (!event.getTransactions().isEmpty()) {
			BlockSnapshot block = event.getTransactions().get(0).getOriginal();
			BlockType type = block.getState().getType();
			if (block.getLocation().isPresent()) {
                Region region = RegionUtils.getRegion(block.getLocation().get());
                if(region != null && FlagUtils.isExcludedFromDestroy(region, type)) {
                    if(region.getFlag(EnumRegionFlag.DESTROY)) {
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
		return RegionUtils.handleEvent(event, EnumRegionFlag.DESTROY, location, player, RegionEventType.LOCAL);
	}
}
