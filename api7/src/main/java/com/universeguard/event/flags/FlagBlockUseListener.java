/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

/**
 * Handler for the blockuse flag
 * @author Jimi
 *
 */
public class FlagBlockUseListener {

    @Listener
    public void onItemUse(InteractBlockEvent.Secondary event, @First Player player) {
        Region region = RegionUtils.getRegion(player.getLocation());
        if(region != null){
            BlockType block = event.getTargetBlock().getState().getType();
            if(!PermissionUtils.hasPermission(player, RegionPermission.REGION) && region.getDisallowedBlocks().contains(block.getId())){
                event.setCancelled(true);
                MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
            }
        }
    }
}
