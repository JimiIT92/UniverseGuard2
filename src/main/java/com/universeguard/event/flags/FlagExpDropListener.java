/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the expdrop flag
 * @author Jimi
 *
 */
public class FlagExpDropListener {

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
