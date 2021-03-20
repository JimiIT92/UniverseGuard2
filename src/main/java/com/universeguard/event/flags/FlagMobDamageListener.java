/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.utils.LogUtils;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.projectile.LaunchProjectileEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.event.filter.cause.Root;

/**
 * Handler for the mobdamage flag
 * @author Jimi
 *
 */
public class FlagMobDamageListener {

	@Listener
	public void onMobDamage(DamageEntityEvent event, @First IndirectEntityDamageSource source) {
		Entity entity = source.getIndirectSource();
		if(event.getTargetEntity() instanceof Player && !(entity instanceof Player) && entity instanceof Living) {
			this.handleEvent(event, entity);
		}
	}

	@Listener
	public void onProjectileShot(LaunchProjectileEvent event, @First Living source) {
		if(!(event.getTargetEntity() instanceof Player)) {
			this.handleEvent(event, source);
		}
	}
	
	private void handleEvent(Cancellable event, Entity entity) {
		EntityType type = entity.getType();
		if(!FlagUtils.isBlockEntity(type) && !FlagUtils.isVehicle(type) && entity instanceof Living) {
			String name = type.getId().toLowerCase();
			Region region = RegionUtils.getRegion(entity.getLocation());
			if(region != null) {
				boolean relatedAllFlag = !region.getMobDamage("all");
				if(entity instanceof Monster)
					relatedAllFlag =  relatedAllFlag || !region.getMobDamage("allhostile");
				if(entity instanceof Creature || entity instanceof Animal)
					relatedAllFlag =  relatedAllFlag || !region.getMobDamage("allpassive");
				boolean cancel = relatedAllFlag || !region.getMobDamage(name);
				if(cancel) {
					event.setCancelled(true);
				}
			}
		}
		
	}
}
