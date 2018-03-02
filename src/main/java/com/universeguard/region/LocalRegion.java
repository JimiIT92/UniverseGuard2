/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region;

import java.util.ArrayList;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import com.universeguard.region.components.RegionLocation;
import com.universeguard.region.components.RegionMember;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionType;
import com.universeguard.utils.RegionUtils;

/**
 * Local Region Class
 * @author Jimi
 *
 */
public class LocalRegion extends Region {

	/**
	 * First point of the Region
	 */
	private RegionLocation FIRST_POINT;
	/**
	 * Second point of the Region
	 */
	private RegionLocation SECOND_POINT;
	/**
	 * Region Priority
	 */
	private int PRIORITY;
	/**
	 * Region Teleport location
	 */
	private RegionLocation TELEPORT_LOCATION;
	/**
	 * Region Spawn location
	 */
	private RegionLocation SPAWN_LOCATION;
	/**
	 * Region members
	 */
	private ArrayList<RegionMember> MEMBERS;
	
	/**
	 * LocalRegion Constructor
	 * @param name The Region name
	 */
	public LocalRegion(String name) {
		this(name, null, null);
	}
	
	/**
	 * Local Region Constructor
	 * @param name The Region name
	 * @param firstPoint The first point
	 * @param secondPoint The second point
	 */
	public LocalRegion(String name, RegionLocation firstPoint, RegionLocation secondPoint) {
		super(RegionType.LOCAL, name);
		this.FIRST_POINT= firstPoint;
		this.SECOND_POINT = secondPoint;
		this.PRIORITY = 0;
		this.TELEPORT_LOCATION = firstPoint;
		this.SPAWN_LOCATION = secondPoint;
		this.MEMBERS = new ArrayList<RegionMember>();
	}
	
	/**
	 * Set the first point base on location
	 * @param location The location
	 */
	public void setFirstPoint(RegionLocation location) {
		FIRST_POINT = location;
	}
	
	/**
	 * Get the first point
	 * @return The Region first point
	 */
	public RegionLocation getFirstPoint() {
		return FIRST_POINT;
	}
	
	/**
	 * Set the second point base on location
	 * @param location The location
	 */
	public void setSecondPoint(RegionLocation location) {
		SECOND_POINT = location;
	}
	
	/**
	 * Get the second point
	 * @return The Region second point
	 */
	public RegionLocation getSecondPoint() {
		return SECOND_POINT;
	}
	
	/**
	 * Set the TeleportLocation of the Region
	 * @param location The location
	 */
	public void setTeleportLocation(RegionLocation location) {
		this.TELEPORT_LOCATION = location;
	}
	
	/**
	 * Get the Region Teleport Location
	 * @return The Region Teleport Location if exists, the Region first point otherwise
	 */
	public RegionLocation getTeleportLocation() {
		return this.TELEPORT_LOCATION == null ? this.FIRST_POINT : this.TELEPORT_LOCATION;
	}
	
	/**
	 * Set the Region Spawn Location
	 * @param location The Location
	 */
	public void setSpawnLocation(RegionLocation location) {
		this.SPAWN_LOCATION = location;
	}
	
	/**
	 * Get the Region Spawn Location
	 * @return The Region Spawn Location if exists, the Region second point otherwise
	 */
	public RegionLocation getSpawnLocation() {
		return this.SPAWN_LOCATION == null ? this.SECOND_POINT : this.SPAWN_LOCATION;
	}

	/**
	 * Get the Region Priority
	 * @return The Region priority
	 */
	public int getPriority() {
		return PRIORITY;
	}

	/**
	 * Set the Region Priority
	 * @param priority The priority
	 */
	public void setPriority(int priority) {
		PRIORITY = priority;
	}
	
	/**
	 * Set the Region Members
	 * @param members The members
	 */
	public void setMembers(ArrayList<RegionMember> members) {
		this.MEMBERS = members;
	}
	
	/**
	 * Get the Region Members
	 * @return The Region Members
	 */
	public ArrayList<RegionMember> getMembers() {
		return this.MEMBERS;
	}
	
	/**
	 * Add a Player with the specified Role to the Region
	 * @param player The player
	 * @param role The role
	 */
	public void addMemberByUUID(UUID player, RegionRole role) {
		this.addMember(RegionUtils.getPlayer(player), role);
	}
	
	/**
	 * Add a Player with the specified Role to the Region
	 * @param player The player
	 * @param role The role
	 */
	public void addMember(Player player, RegionRole role) {
		this.MEMBERS.add(new RegionMember(player, role));
	}

	/**
	 * Remove a member from the Region
	 * @param player The player
	 */
	public void removeMemberByUUID(UUID player) {
		this.removeMember(RegionUtils.getPlayer(player));
	}
	
	/**
	 * Remove a member from the Region
	 * @param player The player
	 */
	public void removeMember(Player player) {
		RegionMember toRemove = null;
		for(RegionMember member : this.MEMBERS) {
			if(member.getUUID().equals(player.getUniqueId())) {
				toRemove = member;
				break;
			}
		}
		if(toRemove != null)
			this.MEMBERS.remove(toRemove);
	}
	
	/**
	 * Get the World object for the Region
	 * @return The Region World Object
	 */
	public World getWorld() {
		return Sponge.getServer().getWorld(this.FIRST_POINT.getWorld()).isPresent() ? Sponge.getServer().getWorld(this.FIRST_POINT.getWorld()).get() : null;
	}

}
