/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.entity.living.complex.EnderDragon;
import org.spongepowered.api.entity.living.complex.EnderDragonPart;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the enderdragonblockdamage flag
 * @author Jimi
 *
 */
public class FlagEnderDragonBlockDamageListener {
	
	@Listener
	public void onEnderDragonBlockDamage(ChangeBlockEvent.Break event, @Root EnderDragon enderdragon) {
		this.handleEvent(event, enderdragon.getLocation(), null);
	}
	
	@Listener
	public void onEnderDragonBlockDamage(ChangeBlockEvent.Break event, @Root EnderDragonPart enderdragonpart) {
		this.handleEvent(event, enderdragonpart.getLocation(), null);
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.ENDER_DRAGON_BLOCK_DAMAGE, location, player, RegionEventType.GLOBAL);
	}
}
