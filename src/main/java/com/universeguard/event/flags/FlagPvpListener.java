/*
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the pvp flag
 * @author Jimi
 *
 */
public class FlagPvpListener {

	@Listener
	public void onPvp(DamageEntityEvent event, @First IndirectEntityDamageSource source) {
		if(event.getTargetEntity() instanceof Player && !source.isExplosive() && source.getIndirectSource() instanceof Player) {
			Player player = (Player) source.getIndirectSource();
			Player targetPlayer = (Player) event.getTargetEntity();
			if(handleEvent(event, player.getLocation(), player) || handleEvent(event, targetPlayer.getLocation(), targetPlayer))
				event.setCancelled(true);
		}
	}

	@Listener
	public void onPvp(InteractItemEvent event, @First Player player) {
		if(event.getContext().containsKey(EventContextKeys.ENTITY_HIT)) {
			if(handleEvent(event, player.getLocation(), player))
				event.setCancelled(true);
		}
	}

	@Listener
	public void onPvp(AttackEntityEvent event, @First EntityDamageSource source) {
		if(event.getTargetEntity() instanceof Player && source.getSource() instanceof Player) {
			Player player = (Player) source.getSource();
			Player targetPlayer = (Player) event.getTargetEntity();
			if(handleEvent(event, player.getLocation(), player) || handleEvent(event, targetPlayer.getLocation(), targetPlayer))
				event.setCancelled(true);
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.PVP, location, player, RegionEventType.GLOBAL);
	}
}
