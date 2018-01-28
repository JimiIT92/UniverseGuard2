/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.enums.RegionRole;

/**
 * Region Member Class
 * @author Jimi
 *
 */
public class RegionMember {
	private UUID UUID;
	private String USERNAME;
	private RegionRole ROLE;
	
	public RegionMember(Player player, RegionRole role) {
		this.UUID = player.getUniqueId();
		this.USERNAME = player.getName();
		this.ROLE = role;
	}
	
	public void setUUID(UUID id) {
		this.UUID = id;
	}
	
	public UUID getUUID() {
		return this.UUID;
	}
	
	public void setUsername(String username) {
		this.USERNAME = username;
	}
	
	public String getUsername() {
		return this.USERNAME;
	}
	
	public void setRole(RegionRole role) {
		this.ROLE = role;
	}
	
	public RegionRole getRole() {
		return this.ROLE;
	}
}
