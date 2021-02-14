/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.InventoryUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

/**
 * Handler for the itemuse flag
 * @author Jimi
 *
 */
public class FlagItemUseListener {

    @Listener
    public void onItemUse(InteractItemEvent event, @Root Player player) {
        Region region = RegionUtils.getRegion(player.getLocation());
        if(region != null){
            ItemStackSnapshot item = event.getItemStack();
            if(!PermissionUtils.hasPermission(player, RegionPermission.REGION) && region.getDisallowedItems().contains(item.getType().getId())){
                event.setCancelled(true);
                MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
                ItemStack itemStack = item.createStack();
                InventoryUtils.removeFromInventory(player, itemStack);
                Task.builder()
                        .execute(new FlagItemUseTaskListener(player, itemStack))
                        .delay(0, TimeUnit.MICROSECONDS)
                        .interval(0, TimeUnit.MICROSECONDS)
                        .name("Item Use Flag Task")
                        .submit(UniverseGuard.INSTANCE);
            }
        }
    }
}
