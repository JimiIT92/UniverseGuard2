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
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.utils.GameModeUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the gamemode flag
 * @author Jimi
 *
 */
public class FlagGamemodeListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			this.handleEvent(player.getLocation(), player);
		}
	}
	
	private boolean handleEvent(Location<World> location, Player player) {
		Region region = RegionUtils.getRegion(location);
		if(region != null && player != null) {
			if(!PermissionUtils.hasPermission(player, RegionPermission.REGION)) {
				GameMode gameMode = GameModeUtils.getGameMode(region.getGameMode());
				if(!gameMode.equals(GameModes.NOT_SET)) {
					if(player.gameMode().exists() && !player.gameMode().get().equals(gameMode)) {
						player.offer(player.getGameModeData().set(Keys.GAME_MODE, gameMode));				
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
