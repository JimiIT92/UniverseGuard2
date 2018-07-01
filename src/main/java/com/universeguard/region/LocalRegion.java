/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import com.universeguard.UniverseGuard;
import com.universeguard.region.components.RegionEffect;
import com.universeguard.region.components.RegionValue;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
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
	 * Region Farewell message
	 */
	private String FAREWELL_MESSAGE;
	/**
	 * Region Greeting message
	 */
	private String GREETING_MESSAGE;
    /**
     * The effects a player will get into this Region
     */
	private ArrayList<RegionEffect> EFFECTS;
    /**
     * If this Region has been sold
     */
	private boolean SOLD;
    /**
     * The value needed to buy this Region
     */
	private RegionValue VALUE;
	/**
	 * LocalRegion Constructor
	 * @param name The Region name
	 */
	public LocalRegion(String name) {
		this(name, null, null, false);
	}
	
	/**
	 * Local Region Constructor
	 * @param name The Region name
	 * @param firstPoint The first point
	 * @param secondPoint The second point
	 */
	public LocalRegion(String name, RegionLocation firstPoint, RegionLocation secondPoint, boolean template) {
		super(RegionType.LOCAL, name, template);
		this.FIRST_POINT= firstPoint;
		this.SECOND_POINT = secondPoint;
		this.PRIORITY = 0;
		this.TELEPORT_LOCATION = firstPoint;
		this.SPAWN_LOCATION = secondPoint;
		this.MEMBERS = new ArrayList<RegionMember>();
		this.FAREWELL_MESSAGE = "";
		this.GREETING_MESSAGE = "";
		this.EFFECTS = new ArrayList<RegionEffect>();
		this.SOLD = false;
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
	 * Add a Player with the specified Role to the Region by giving it's UUID and username
	 * @param player The player's UUID
	 * @param username The player's username
	 * @param role The role
	 */
	public void addMemberByUUIDAndUsername(UUID player, String username, RegionRole role) {
		this.MEMBERS.add(new RegionMember(player, username, role));
	}

	/**
	 * Remove a member from the Region
	 * @param player The player
	 */
	public void removeMemberByUUID(UUID player) {
		RegionMember toRemove = null;
		for(RegionMember member : this.MEMBERS) {
			if(member.getUUID().equals(player)) {
				toRemove = member;
				break;
			}
		}
		if(toRemove != null)
			this.MEMBERS.remove(toRemove);
	}
	
	/**
	 * Remove a member from the Region
	 * @param player The player
	 */
	public void removeMember(Player player) {
		removeMemberByUUID(player.getUniqueId());
	}
	
	/**
	 * Set the Farewell message
	 * @param message the Farewell message
	 */
	public void setFarewellMessage(String message) {
		this.FAREWELL_MESSAGE = message;
	}
	
	/**
	 * Get the Farewell message
	 * @return the Farewell message
	 */
	public String getFarewellMessage() {
		return this.FAREWELL_MESSAGE;
	}
	
	/**
	 * Set the Greeting message
	 * @param message the Greeting message
	 */
	public void setGreetingMessage(String message) {
		this.GREETING_MESSAGE = message;
	}
	
	/**
	 * Get the Greeting message
	 * @return the Greeting message
	 */
	public String getGreetingMessage() {
		return this.GREETING_MESSAGE;
	}

	public void setEffects(ArrayList<RegionEffect> effects) {
	    this.EFFECTS = effects;
    }

    public ArrayList<RegionEffect> getEffects() {
	    return this.EFFECTS;
    }

    public void addEffect(PotionEffectType potion, int level) {
	    boolean found = false;
	    for(RegionEffect effect : this.EFFECTS) {
	        if(effect.getEffect().equals(potion)) {
                effect.setLevel(level);
                found = true;
                break;
            }
        }
        if(!found)
            this.EFFECTS.add(new RegionEffect(potion, level));
    }

    public void removeEffect(PotionEffectType potion) {
        RegionEffect effectToRemove = null;
	    for (RegionEffect effect : this.EFFECTS) {
            if(effect.getEffect().equals(potion)) {
                effectToRemove = effect;
                break;
            }
        }
        if(effectToRemove != null) {
	        this.EFFECTS.remove(effectToRemove);
        }
    }

    public void setSold(boolean sold) {
	    this.SOLD = sold;
    }

    public boolean getSold() {
        return this.SOLD;
    }

    public void setValue(RegionValue value) {
	    this.VALUE = value;
    }

    public RegionValue getValue() {
	    return this.VALUE;
    }

    /**
	 * Get the World object for the Region
	 * @return The Region World Object
	 */
	public World getWorld() {
		return this.FIRST_POINT != null && Sponge.getServer().getWorld(this.FIRST_POINT.getWorld()).isPresent() ? Sponge.getServer().getWorld(this.FIRST_POINT.getWorld()).get() : null;
	}

}
