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

import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import com.universeguard.UniverseGuard;
import com.universeguard.region.components.RegionCommand;
import com.universeguard.region.components.RegionExplosion;
import com.universeguard.region.components.RegionFlag;
import com.universeguard.region.components.RegionInteract;
import com.universeguard.region.components.RegionMob;
import com.universeguard.region.components.RegionVehicle;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.EnumRegionInteract;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.region.enums.RegionType;
import com.universeguard.utils.FlagUtils;

/**
 * Region Class
 * @author Jimi
 *
 */
public class Region {

	/**
	 * Region Version
	 */
	private float VERSION = UniverseGuard.REGION_VERSION;
	/**
	 * Region ID
	 */
	private UUID ID;
	/**
	 * Region Type
	 */
	private RegionType TYPE;
	/**
	 * Region name
	 */
	private String NAME;
	/**
	 * Region GameMode
	 */
	private String GAMEMODE;
	/**
	 * Region Flags
	 */
	private ArrayList<RegionFlag> FLAGS;
	/**
	 * Region Interacts
	 */
	private ArrayList<RegionInteract> INTERACTS;
	/**
	 * Region Vehicles
	 */
	private ArrayList<RegionVehicle> VEHICLES;
	/**
	 * Region Explosions
	 */
	private ArrayList<RegionExplosion> EXPLOSIONS;
	/**
	 * Region Mobs
	 */
	private ArrayList<RegionMob> MOBS;
	/**
	 * Region Commands
	 */
	private ArrayList<RegionCommand> COMMANDS;
	
	/**
	 * Region Constructor
	 * @param type The Region Type
	 */
	public Region(RegionType type) {
		this(type, "");
	}
	
	/**
	 * Region Constructor
	 * @param type The Region Type
	 * @param name The Region name
	 */
	public Region(RegionType type, String name) {
		this(type, name, GameModes.NOT_SET.getId());
	}
	
	/**
	 * Region Constructor
	 * @param type The Region Type
	 * @param name The Region name
	 * @param gamemode The Region GameMode
	 */
	public Region(RegionType type, String name, String gamemode) {
		this.ID = UUID.randomUUID();
		this.TYPE = type;
		this.NAME = name;
		this.GAMEMODE = gamemode;
		this.COMMANDS = new ArrayList<RegionCommand>();
		this.initFlags();
	}
	

	/**
	 * Set the Region ID
	 * @param id The Region ID
	 */
	public void setId(UUID id) {
		this.ID = id;
	}
	
	/**
	 * Get the Region ID
	 * @return
	 */
	public UUID getId() {
		return this.ID;
	}
	/**
	 * INitialize Region flags
	 */
	public void initFlags() {
		this.FLAGS = new ArrayList<RegionFlag>();
		this.INTERACTS = new ArrayList<RegionInteract>();
		this.VEHICLES = new ArrayList<RegionVehicle>();
		this.EXPLOSIONS = new ArrayList<RegionExplosion>();
		this.MOBS = new ArrayList<RegionMob>();
		
		for(EnumRegionFlag flag : EnumRegionFlag.values())
			this.FLAGS.add(new RegionFlag(flag));
		
		for(EnumRegionInteract interact : EnumRegionInteract.values())
			this.INTERACTS.add(new RegionInteract(interact));
		
		for(EnumRegionVehicle vehicle : EnumRegionVehicle.values())
			this.VEHICLES.add(new RegionVehicle(vehicle));
		
		for(EnumRegionExplosion explosion : EnumRegionExplosion.values())
			this.EXPLOSIONS.add(new RegionExplosion(explosion));
	}
	
	/**
	 * Update Region flags to latest Region Version
	 */
	public void updateFlags() {
		removeOldFlags();
		setMissingFlags();
	}
	
	/**
	 * Set missing Region flags
	 */
	public void setMissingFlags() {
		boolean update = false;
		for(EnumRegionFlag f : EnumRegionFlag.values()) {
			RegionFlag flag = new RegionFlag(f);
			for(RegionFlag flags : this.FLAGS) {
				if(!flags.getName().equalsIgnoreCase(flag.getName()))
					update = true;
				else {
					update = false;
					break;
				}
			}
			if(update) {
				this.FLAGS.add(flag);
			}
		}
		update = false;
		for(EnumRegionInteract i : EnumRegionInteract.values()) {
			RegionInteract interact = new RegionInteract(i);
			for(RegionInteract interacts : this.INTERACTS) {
				if(!interacts.getBlock().equalsIgnoreCase(interact.getBlock()))
					update = true;
				else {
					update = false;
					break;
				}
			}
			if(update) {
				this.INTERACTS.add(interact);
			}
		}
		
		update = false;
		for(EnumRegionVehicle v : EnumRegionVehicle.values()) {
			RegionVehicle vehicle = new RegionVehicle(v);
			for(RegionVehicle vehicles : this.VEHICLES) {
				if(!vehicles.getName().equalsIgnoreCase(vehicle.getName()))
					update = true;
				else {
					update = false;
					break;
				}
			}
			if(update) {
				this.VEHICLES.add(vehicle);
			}
		}
		
		update = false;
		for(EnumRegionExplosion e : EnumRegionExplosion.values()) {
			RegionExplosion explosion = new RegionExplosion(e);
			for(RegionExplosion explosions : this.EXPLOSIONS) {
				if(!explosions.getExplosion().equalsIgnoreCase(explosion.getExplosion()))
					update = true;
				else {
					update = false;
					break;
				}
			}
			if(update) {
				this.EXPLOSIONS.add(explosion);
			}
		}
	}
	
	/**
	 * Remove old Region flags
	 */
	public void removeOldFlags() {
		ArrayList<RegionFlag> flagsToRemove = new ArrayList<RegionFlag>();
		ArrayList<RegionInteract> interactsToRemove = new ArrayList<RegionInteract>();
		ArrayList<RegionVehicle> vehiclesToRemove = new ArrayList<RegionVehicle>();
		ArrayList<RegionExplosion> explosionsToRemove = new ArrayList<RegionExplosion>();
		
		for(RegionFlag f : this.FLAGS) {
			EnumRegionFlag flag = FlagUtils.getFlag(f.getName());
			if(flag == null) {
				flagsToRemove.add(f);
			}
		}
		
		for(RegionInteract i : this.INTERACTS) {
			EnumRegionInteract interact = FlagUtils.getInteract(i.getBlock());
			if(interact == null) {
				interactsToRemove.add(i);
			}
		}
		
		for(RegionVehicle v : this.VEHICLES) {
			EnumRegionVehicle vehicle = FlagUtils.getVehicle(v.getName());
			if(vehicle == null) {
				vehiclesToRemove.add(v);
			}
		}
		
		for(RegionExplosion e : this.EXPLOSIONS) {
			EnumRegionExplosion explosion = FlagUtils.getExplosion(e.getExplosion());
			if(explosion == null) {
				explosionsToRemove.add(e);
			}
		}
		
		this.FLAGS.removeAll(flagsToRemove);
		this.INTERACTS.removeAll(interactsToRemove);
		this.VEHICLES.removeAll(vehiclesToRemove);
		this.EXPLOSIONS.removeAll(explosionsToRemove);
	}
	
	/**
	 * Set Region Type
	 * @param type The Region Type
	 */
	public void setType(RegionType type) {
		this.TYPE = type;
	}
	
	/**
	 * Get the Region Type
	 * @return The Region Type
	 */
	public RegionType getType() {
		return this.TYPE;
	}
	
	/**
	 * Set the Region name
	 * @param name The name
	 */
	public void setName(String name) {
		this.NAME = name;
	}
	
	/**
	 * Get the Region name
	 * @return The Region name
	 */
	public String getName() {
		return this.NAME;
	}
	
	/**
	 * Set the Region Flags
	 * @param flags The Region flag
	 */
	public void setFlags(ArrayList<RegionFlag> flags) {
		this.FLAGS = flags;
	}
	
	/**
	 * Get the Region flags
	 * @return The Region flags
	 */
	public ArrayList<RegionFlag> getFlags() {
		return this.FLAGS;
	}
	
	/**
	 * Set a flag for the Region
	 * @param flag The flag
	 * @param value The flag value
	 */
	public void setFlag(EnumRegionFlag flag, boolean value) {
		for(RegionFlag f : this.FLAGS) {
			if(f.getName().equalsIgnoreCase(flag.getName()))
				f.setValue(value);
		}
	}
	
	/**
	 * Get the value of a flag for the Region
	 * @param flag The flag
	 * @return The flag value
	 */
	public boolean getFlag(EnumRegionFlag flag) {
		for(RegionFlag f : this.FLAGS) {
			if(f.getName().equalsIgnoreCase(flag.getName()))
				return f.getValue();
		}
		return false;
	}
	
	/**
	 * Set all interacts for the Region
	 * @param value The interact value
	 */
	public void setAllInteract(boolean value) {
		for(RegionInteract i : this.INTERACTS) {
			i.setEnabled(value);
		}
	}
	
	/**
	 * Set a interact for the Region
	 * @param interact The interact
	 * @param value The interact value
	 */
	public void setInteract(EnumRegionInteract interact, boolean value) {
		for(RegionInteract i : this.INTERACTS) {
			if(i.getBlock().equalsIgnoreCase(interact.getName()))
				i.setEnabled(value);
		}
	}
	
	/**
	 * Get the value of a interact for the Region
	 * @param interact The interact
	 * @return The interact value
	 */
	public boolean getInteract(EnumRegionInteract interact) {
		for(RegionInteract i : this.INTERACTS) {
			if(i.getBlock().equalsIgnoreCase(interact.getName()))
				return i.isEnabled();
		}
		return false;
	}
	
	/**
	 * Set the GameMode for the Region
	 * @param gamemode The Gamemode
	 */
	public void setGamemode(String gamemode) {
		this.GAMEMODE = gamemode;
	}
	
	/**
	 * Get the GameMode for the Region
	 * @return The Region GameMode
	 */
	public String getGameMode() {
		return this.GAMEMODE;
	}
	
	/**
	 * Set the Region commands
	 * @param commands The Region commands
	 */
	public void setCommands(ArrayList<RegionCommand> commands) {
		this.COMMANDS = commands;
	}
	
	/**
	 * Get the Region commands
	 * @return The Region commands
	 */
	public ArrayList<RegionCommand> getCommands() {
		return this.COMMANDS;
	}
	
	/**
	 * Check if a command is enabled in the Region
	 * @param command The command
	 * @return true if the command is enabled or not set, false otherwise
	 */
	public boolean isCommandEnabled(String command) {
		RegionCommand regionCommand = getRegionCommand(command);
		return regionCommand == null ? true : regionCommand.isEnabled();
	}
	
	/**
	 * Get a Region command
	 * @param command The command
	 * @return The RegionCommand for the command if exists, null otherwise
	 */
	public RegionCommand getRegionCommand(String command) {
		for(RegionCommand regionCommand : this.COMMANDS) {
			if(regionCommand.getCommand().equalsIgnoreCase(command))
				return regionCommand;
		}
		return null;
	}
	
	/**
	 * Enable a command in the Region
	 * @param command The Command
	 */
	public void enableCommand(String command) {
		RegionCommand regionCommand = getRegionCommand(command);
		if(regionCommand != null) {
			regionCommand.setEnabled(true);
			this.COMMANDS.set(this.COMMANDS.indexOf(regionCommand), regionCommand);
		}
		else
			this.COMMANDS.add(new RegionCommand(command, true));
	}
	
	/**
	 * Disable a command in the Region
	 * @param command The Command
	 */
	public void disableCommand(String command) {
		RegionCommand regionCommand = getRegionCommand(command);
		if(regionCommand != null) {
			regionCommand.setEnabled(false);
			this.COMMANDS.set(this.COMMANDS.indexOf(regionCommand), regionCommand);
		}
		else
			this.COMMANDS.add(new RegionCommand(command, false));
	}
	
	/**
	 * Get the place value of a Vehicle for the Region
	 * @param vehicle The Vehicle
	 * @return The VehiclePlace value
	 */
	public boolean getVehiclePlace(EnumRegionVehicle vehicle) {
		for(RegionVehicle v : this.VEHICLES) {
			if(v.getName().equalsIgnoreCase(vehicle.getName()))
				return v.getPlace();
		}
		return true;
	}
	
	/**
	 * Set the place value for all Vehicles in the Region
	 * @param value The value
	 */
	public void setAllVehiclePlace(boolean value) {
		for(RegionVehicle v : this.VEHICLES) {
			v.setPlace(value);
		}
	}
	
	/**
	 * Set the place value for a Vehicle in the Region
	 * @param vehicle The Vehicle
	 * @param value The value
	 */
	public void setVehiclePlace(EnumRegionVehicle vehicle, boolean value) {
		for(RegionVehicle v : this.VEHICLES) {
			if(v.getName().equalsIgnoreCase(vehicle.getName()))
				v.setPlace(value);
		}
	}
	
	/**
	 * Get the destroy value of a Vehicle for the Region
	 * @param vehicle The Vehicle
	 * @return The VehicleDestroy value
	 */
	public boolean getVehicleDestroy(EnumRegionVehicle vehicle) {
		for(RegionVehicle v : this.VEHICLES) {
			if(v.getName().equalsIgnoreCase(vehicle.getName()))
				return v.getDestroy();
		}
		return true;
	}
	
	/**
	 * Set the destroy value for all Vehicles in the Region
	 * @param value The value
	 */
	public void setAllVehicleDestroy(boolean value) {
		for(RegionVehicle v : this.VEHICLES) {
			v.setDestroy(value);
		}
	}
	
	/**
	 * Set the destroy value for a Vehicle in the Region
	 * @param vehicle The Vehicle
	 * @param value The value
	 */
	public void setVehicleDestroy(EnumRegionVehicle vehicle, boolean value) {
		for(RegionVehicle v : this.VEHICLES) {
			if(v.getName().equalsIgnoreCase(vehicle.getName()))
				v.setDestroy(value);
		}
	}
	
	/**
	 * Get the damage value of an Explosion for the Region
	 * @param explosion The Explosion
	 * @return The damage value of the Explosion
	 */
	public boolean getExplosionDamage(EnumRegionExplosion explosion) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			if(e.getExplosion().equalsIgnoreCase(explosion.getName()))
				return e.getDamage();
		}
		return true;
	}
	
	/**
	 * Set a the damage value for all Explosions in the Region
	 * @param value The value
	 */
	public void setAllExplosionDamage(boolean value) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			e.setDamage(value);
		}
	}
	
	/**
	 * Set a the damage value for an Explosion in the Region
	 * @param explosion The Explosion
	 * @param value The value
	 */
	public void setExplosionDamage(EnumRegionExplosion explosion, boolean value) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			if(e.getExplosion().equalsIgnoreCase(explosion.getName()))
				e.setDamage(value);
		}
	}
	
	/**
	 * Get the destroy value of an Explosion for the Region
	 * @param explosion The Explosion
	 * @return The destroy value of the Explosion
	 */
	public boolean getExplosionDestroy(EnumRegionExplosion explosion) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			if(e.getExplosion().equalsIgnoreCase(explosion.getName()))
				return e.getDestroy();
		}
		return true;
	}
	
	/**
	 * Set the destroy value of all Explosions in the Region
	 * @param value The value
	 */
	public void setAllExplosionDestroy(boolean value) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			e.setDestroy(value);
		}
	}
	
	/**
	 * Set the destroy value of an Explosion in the Region
	 * @param explosion The Explosion
	 * @param value The value
	 */
	public void setExplosionDestroy(EnumRegionExplosion explosion, boolean value) {
		for(RegionExplosion e : this.EXPLOSIONS) {
			if(e.getExplosion().equalsIgnoreCase(explosion.getName()))
				e.setDestroy(value);
		}
	}
	
	/**
	 * Get the spawn value of a Mob in the Region
	 * @param mob The Mob
	 * @return the spawn value of the Mob
	 */
	public boolean getMobSpawn(String mob) {
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob))
				return m.getSpawn();
		}
		return true;
	}
	

	/**
	 * Set the spawn value of a Mob for the Region
	 * @param mob The Mod
	 * @param value The value
	 */
	public void setMobSpawn(String mob, boolean value) {
		RegionMob rm = null;
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob)) {
				m.setSpawn(value);
				return;
			}
		}
		rm = new RegionMob(mob);
		rm.setSpawn(value);
		this.MOBS.add(rm);
	}
	
	/**
	 * Get the pve value of a Mob in the Region
	 * @param mob The Mob
	 * @return The pve value of the Mob
	 */
	public boolean getMobPve(String mob) {
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob))
				return m.getPve();
		}
		return true;
	}
	
	/**
	 * Set the pve value of a Mob for the Region
	 * @param mob The Mob
	 * @param value The value
	 */
	public void setMobPve(String mob, boolean value) {
		RegionMob rm = null;
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob)) {
				m.setPve(value);
				return;
			}
		}
		rm = new RegionMob(mob);
		rm.setPve(value);
		this.MOBS.add(rm);
	}
	
	/**
	 * Get the damage value of a Mob in the Region
	 * @param mob The Mob
	 * @return The damage value of the Mob
	 */
	public boolean getMobDamage(String mob) {
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob))
				return m.getDamage();
		}
		return true;
	}
	
	/**
	 * Set the damage value of a Mob for the Region
	 * @param mob The Mob
	 * @param value The value
	 */
	public void setMobDamage(String mob, boolean value) {
		RegionMob rm = null;
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob)) {
				m.setDamage(value);
				return;
			}
		}
		rm = new RegionMob(mob);
		rm.setDamage(value);
		this.MOBS.add(rm);
	}
	
	/**
	 * Get the drop value of a Mob in the Region
	 * @param mob The Mob
	 * @return The drop value of the Mob
	 */
	public boolean getMobDrop(String mob) {
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob))
				return m.getDrop();
		}
		return true;
	}
	
	/**
	 * Set the drop value of a Mob for the Region
	 * @param mob The Mob
	 * @param value The value
	 */
	public void setMobDrop(String mob, boolean value) {
		RegionMob rm = null;
		for(RegionMob m : this.MOBS) {
			if(m.getMob().equalsIgnoreCase(mob)) {
				m.setDrop(value);
				return;
			}
		}
		rm = new RegionMob(mob);
		rm.setDrop(value);
		this.MOBS.add(rm);
	}
	
	/**
	 * Set the Interacts for the Region
	 * @param interact The Interact
	 */
	public void setInteracts(ArrayList<RegionInteract> interact) {
		this.INTERACTS = interact;
	}
	
	/**
	 * Get the Interacts for the Region
	 * @return The Region Interacts
	 */
	public ArrayList<RegionInteract> getInteracts() {
		return this.INTERACTS;
	}

	/**
	 * Get the Vehicles for the Region
	 * @return The Region Vehicles
	 */
	public ArrayList<RegionVehicle> getVehicles() {
		return this.VEHICLES;
	}

	/**
	 * Set the Vehicles for the Region
	 * @param vehicles The Vehicles
	 */
	public void setVehicles(ArrayList<RegionVehicle> vehicles) {
		this.VEHICLES = vehicles;
	}

	/**
	 * Get the Explosions for the Region
	 * @return The Region Explosions
	 */
	public ArrayList<RegionExplosion> getExplosions() {
		return this.EXPLOSIONS;
	}

	/**
	 * Set the Explosions for the Region
	 * @param explosion The Explosions
	 */
	public void setExplosions(ArrayList<RegionExplosion> explosion) {
		this.EXPLOSIONS = explosion;
	}

	/**
	 * Get the Mobs for the Region
	 * @return The Region Mobs
	 */
	public ArrayList<RegionMob> getMobs() {
		return this.MOBS;
	}
	
	/**
	 * Set the Mobs for the Region
	 * @param mobs The Mobs
	 */
	public void setMobs(ArrayList<RegionMob> mobs) {
		this.MOBS = mobs;
	}
	
	/**
	 * Set the Region Version
	 * @param version The Region Version
	 */
	public void setVersion(float version) {
		this.VERSION = version;
	}
	
	/**
	 * Get the Region Version
	 * @return The Region Version
	 */
	public float getVersion() {
		return this.VERSION;
	}
	
	/**
	 * Check if the Region is Global
	 * @return true if the Region is a GlobalRegion, false otherwise
	 */
	public boolean isGlobal() {
		return this.TYPE == RegionType.GLOBAL;
	}
	
	/**
	 * Check if the Region is Local
	 * @return true if the Region is a LocalRegion, false otherwise
	 */
	public boolean isLocal() {
		return this.TYPE == RegionType.LOCAL;
	}
}
