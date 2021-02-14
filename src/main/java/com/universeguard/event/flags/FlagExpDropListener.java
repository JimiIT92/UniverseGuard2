/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.utils.LogUtils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.Optional;

/**
 * Handler for the expdrop flag
 * @author Jimi
 *
 */
public class FlagExpDropListener {

    @Listener
    public void onRespawn(RespawnPlayerEvent event, @First Player player) {
        Player OriginalPlayer = event.getOriginalPlayer();
        Region region = RegionUtils.getRegion(OriginalPlayer.getLocation());
        if(region != null && !region.getFlag(EnumRegionFlag.EXP_DROP)) {
            Optional<Integer> OptionalLevel = OriginalPlayer.get(Keys.EXPERIENCE_LEVEL);
            Optional<Integer> OptionalExp = OriginalPlayer.get(Keys.TOTAL_EXPERIENCE);
            if(OptionalLevel.isPresent() && OptionalExp.isPresent()) {
                player.offer(Keys.EXPERIENCE_LEVEL, OptionalLevel.get());
                player.offer(Keys.TOTAL_EXPERIENCE, OptionalExp.get());
            }
        }
    }

	@Listener
	public void onExpDrop(SpawnEntityEvent event) {
		if(!event.getEntities().isEmpty()) {
			Entity entity = event.getEntities().get(0);
			EntityType type = entity.getType();
			if(type.equals(EntityTypes.EXPERIENCE_ORB)) {
				RegionUtils.handleEvent(event, EnumRegionFlag.EXP_DROP, entity.getLocation(), null, RegionEventType.GLOBAL);
			}
		}
	}
}
