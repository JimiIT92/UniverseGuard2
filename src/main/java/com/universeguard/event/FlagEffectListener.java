/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event;

import com.flowpowered.math.vector.Vector3i;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.components.RegionEffect;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.utils.DirectionUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Handler for the enter flag
 * @author Jimi
 *
 */
public class FlagEffectListener implements Runnable {
	
	@Override
	public void run() {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			Region region = RegionUtils.getRegion(player.getLocation());
			if(region != null && region.isLocal() && ((LocalRegion)region).getEffects().size() > 0) {
                Optional<PotionEffectData> potions = player.getOrCreate(PotionEffectData.class);
                if(potions.isPresent()) {
                    ArrayList<PotionEffectType> activePotionEffects = new ArrayList<PotionEffectType>();
                    for(PotionEffect potion : potions.get().asList()) {
                        activePotionEffects.add(potion.getType());
                    }
                    for(RegionEffect effect : ((LocalRegion)region).getEffects()) {
                        if(!activePotionEffects.contains(effect.getEffect())) {
                            potions.get().addElement(PotionEffect.builder().potionType(effect.getEffect()).amplifier(effect.getLevel()).duration(200).ambience(true).build());
                        }
                    }
                    player.offer(potions.get());
                }
			}
		}
	}
}
