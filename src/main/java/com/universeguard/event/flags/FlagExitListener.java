/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import com.flowpowered.math.vector.Vector3i;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the hunger flag
 * @author Jimi
 *
 */
public class FlagExitListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			Region region = RegionUtils.getRegion(player.getLocation());
			Direction direction = GetPlayerDirection(player);
			if(region != null && region.isLocal() && !RegionUtils.hasPermission(player, region) && !region.getFlag(EnumRegionFlag.EXIT)) {
				player.setLocation(player.getLocation().sub(getOffset(direction)));
			}
		}
	}
	
	public Direction GetPlayerDirection(Player playerSelf){
        Direction dir;
        double y = playerSelf.getTransform().getYaw();
        if( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if(i == 0){dir = Direction.SOUTH;}
        else if(i == 1){dir = Direction.SOUTH;}
        else if(i == 2){dir = Direction.WEST;}
        else if(i == 3){dir = Direction.WEST;}
        else if(i == 4){dir = Direction.WEST;}
        else if(i == 5){dir = Direction.WEST;}
        else if(i == 6){dir = Direction.WEST;}
        else if(i == 7){dir = Direction.NORTH;}
        else if(i == 8){dir = Direction.NORTH;}
        else if(i == 9){dir = Direction.NORTH;}
        else if(i == 10){dir = Direction.EAST;}
        else if(i == 11){dir = Direction.EAST;}
        else if(i == 12){dir = Direction.EAST;}
        else if(i == 13){dir = Direction.EAST;}
        else if(i == 14){dir = Direction.EAST;}
        else if(i == 15){dir = Direction.SOUTH;}
        else {dir = Direction.SOUTH;}
        return dir;
   }
	
	private static Vector3i getOffset(Direction direction) {
		int offsetX = 0;
		int offsetY = 0;
		int offsetZ = 0;
		if(direction.equals(Direction.NORTH)) {
			offsetZ = 1;
		}
		else if(direction.equals(Direction.SOUTH)) {
			offsetZ = -1;
		}
		else if(direction.equals(Direction.EAST)) {
			offsetX = -1;
		}
		else if(direction.equals(Direction.WEST)) {
			offsetX = 1;
		}
		else if(direction.equals(Direction.UP)) {
			offsetY = 1;
		}
		else if(direction.equals(Direction.DOWN)) {
			offsetY = -1;
		}
		return new Vector3i(offsetX, offsetY, offsetZ);
	}
	
}
