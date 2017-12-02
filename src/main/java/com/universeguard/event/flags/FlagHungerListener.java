/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the hunger flag
 * @author Jimi
 *
 */
public class FlagHungerListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			if(player.gameMode().exists() && player.gameMode().get().equals(GameModes.SURVIVAL) ) {
				if(this.handleEvent(null, player.getLocation(), null))
					player.offer(player.getFoodData().set(Keys.FOOD_LEVEL, 20));
			}
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.HUNGER, location, player, RegionEventType.GLOBAL);
	}
	
}
