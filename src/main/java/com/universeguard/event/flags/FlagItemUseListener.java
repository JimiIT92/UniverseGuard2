/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.region.enums.*;
import com.universeguard.utils.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * Handler for the itemuse flag
 * @author Jimi
 *
 */
public class FlagItemUseListener {

    @Listener
    public void onItemUse(InteractItemEvent.Secondary event, @First Player player) {
        Region region = RegionUtils.getRegion(player.getLocation());
        if(region != null){
            ItemStackSnapshot stack = event.getItemStack();
            if(stack != null){
                if(region.getDisallowedItems().contains(stack.getType().getId())){
                    event.setCancelled(true);

                }
            }
        }
    }

    private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
        MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
        return RegionUtils.handleEvent(event, EnumRegionFlag.LIGHTER, location, player, RegionEventType.LOCAL);
    }
}
