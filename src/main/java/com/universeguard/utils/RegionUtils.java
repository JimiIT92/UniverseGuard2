/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.reflect.TypeToken;
import com.universeguard.region.components.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.universeguard.UniverseGuard;
import com.universeguard.region.GlobalRegion;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.EnumRegionInteract;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionText;
import com.universeguard.region.enums.RegionType;

/**
 * 
 * Utility class for regions
 * 
 * @author Jimi
 *
 */
public class RegionUtils {

	// Pending Regions
	private static HashMap<CommandSource, Region> PENDINGS = new HashMap<CommandSource, Region>();
	private static HashMap<UUID, RegionSell> SELLING_REGIONS = new HashMap<>();
	/**
	 * Save a Region to a JSON file
	 * 
	 * @param region
	 *            The Region
	 * @return true if the Region has been saved correctly, false otherwise
	 */
	public static boolean save(Region region) {
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		FileWriter fileWriter = null;
		try {
			File directory = region.isLocal() ? getRegionFolder() : getGlobalRegionFolder();
			if (!directory.exists())
				directory.mkdirs();
			File file = getFile(region);
			if (!file.exists())
				file.createNewFile();
			fileWriter = new FileWriter(file);
			fileWriter.write(gson.toJson(region));
			Region cachedRegion = null;
			for (Region cached : UniverseGuard.ALL_REGIONS) {
				if (cached.getId() != null && cached.getId().compareTo(region.getId()) == 0) {
					cachedRegion = cached;
					break;
				}
			}
			if (cachedRegion != null) {
				UniverseGuard.ALL_REGIONS.remove(cachedRegion);
			}
			UniverseGuard.ALL_REGIONS.add(region);
			saveIndex();
			return true;
		} catch (IOException e) {
			LogUtils.log(e);
			LogUtils.print(TextColors.RED, RegionText.REGION_SAVE_EXCEPTION.getValue(), "rg utils");
			return false;
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					LogUtils.log(e);
					LogUtils.print(RegionText.REGION_WRITER_CLOSE_EXCEPTION.getValue(), "rg utils");
				}
			}
		}
	}

	/**
	 * Save a Region to a JSON file
	 * 
	 * @param region
	 *            The Region
	 * @return true if the Region has been saved correctly, false otherwise
	 */
	public static LocalRegion copy(LocalRegion region, String newRegionName) {
		LocalRegion newRegion = new LocalRegion(newRegionName, region.getFirstPoint(), region.getSecondPoint(), region.getTemplate());
		newRegion.setPriority(region.getPriority());
		newRegion.setTeleportLocation(region.getTeleportLocation());
		newRegion.setSpawnLocation(region.getSpawnLocation());
		newRegion.setFarewellMessage(region.getFarewellMessage());
		newRegion.setGreetingMessage(region.getGreetingMessage());
		if (!UniverseGuard.UNIQUE_REGIONS) {
			newRegion.setMembers(region.getMembers());
		}
		newRegion.setFlags(region.getFlags());
		newRegion.setInteracts(region.getInteracts());
		newRegion.setVehicles(region.getVehicles());
		newRegion.setExplosions(region.getExplosions());
		newRegion.setMobs(region.getMobs());
		newRegion.setCommands(region.getCommands());
		return newRegion;
	}

	/**
	 * Remove a Region from the regions folder
	 * 
	 * @param region
	 *            The Region
	 * @return true if the Region has been removed correctly, false otherwise
	 */
	public static boolean remove(Region region) {
	    if(region.isLocal()) {
            ((LocalRegion)region).getMembers().clear();
        }
	    File directory = region.isLocal() ? getRegionFolder() : getGlobalRegionFolder();
		if (!directory.exists())
			return false;
		File file = getFile(region);
		if (file.exists() && file.delete()) {
			UniverseGuard.ALL_REGIONS.remove(region);
			saveIndex();
			return true;
		}
		return false;
	}

	/**
	 * Remove a Region from the regions folder
	 * 
	 * @param region
	 *            The Region
	 * @return true if the Region has been removed correctly, false otherwise
	 */
	public static boolean removeByName(Region region) {
		File directory = region.isLocal() ? getRegionFolder() : getGlobalRegionFolder();
		if (!directory.exists())
			return false;
		File file = getFileByName(region);
		if (file.exists() && file.delete()) {
			UniverseGuard.ALL_REGIONS.remove(region);
			saveIndex();
			return true;
		}
		return false;
	}

	public static void loadSellingRegions() {
		Gson gson = new Gson();
		BufferedReader bufferedReader = null;
		try {
			File file = new File(getConfigFolder() + "/" + "selling_regions.json");
			if(file.exists()) {
				bufferedReader = new BufferedReader(new FileReader(file));
				Type type = new TypeToken<Map<UUID, RegionSell>>(){}.getType();
				SELLING_REGIONS = gson.fromJson(bufferedReader, type);
			}
		} catch (FileNotFoundException e) {
			LogUtils.log(e);
			LogUtils.print(TextColors.RED, RegionText.REGION_SELLING_LOAD_EXCEPTION.getValue(), "rg utils");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LogUtils.log(e);
					LogUtils.print(TextColors.RED, RegionText.REGION_READER_CLOSE_EXCEPTION.getValue(), "rg utils");
				}
			}
		}
	}

	/**
	 * Save a Regions index file
	 */
	public static void saveIndex() {
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		FileWriter fileWriter = null;
		try {
			File directory = new File(getConfigFolder());
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(getConfigFolder() + "/" + "index.json");
			if (!file.exists())
				file.createNewFile();
			fileWriter = new FileWriter(file);
			HashMap<String, UUID> regions = new HashMap<String, UUID>();
			for (Region region : UniverseGuard.ALL_REGIONS)
				regions.put(region.getName(), region.getId());
			fileWriter.write(gson.toJson(regions));
		} catch (IOException e) {
			LogUtils.log(e);
			LogUtils.print(TextColors.RED, RegionText.REGION_SAVE_INDEX_EXCEPTION.getValue(), "rg utils");
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					LogUtils.log(e);
					LogUtils.print(RegionText.REGION_WRITER_INDEX_CLOSE_EXCEPTION.getValue(), "rg utils");
				}
			}
		}
	}

	/**
	 * Save the Selling Regions file
	 */
	public static void saveSellingRegions() {
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		FileWriter fileWriter = null;
		try {
			File directory = new File(getConfigFolder());
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(getConfigFolder() + "/" + "selling_regions.json");
			if (!file.exists())
				file.createNewFile();
			fileWriter = new FileWriter(file);
			fileWriter.write(gson.toJson(SELLING_REGIONS));
		} catch (IOException e) {
			LogUtils.log(e);
			LogUtils.print(TextColors.RED, RegionText.REGION_SAVE_INDEX_EXCEPTION.getValue(), "rg utils");
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					LogUtils.log(e);
					LogUtils.print(RegionText.REGION_WRITER_INDEX_CLOSE_EXCEPTION.getValue(), "rg utils");
				}
			}
		}
	}

	public static void addSellingRegion(UUID owner, UUID regionId, RegionValue value) {
		SELLING_REGIONS.putIfAbsent(regionId,new RegionSell(owner, regionId, value));
		saveSellingRegions();
	}

	public static HashMap<UUID, RegionSell> getSellingRegions(UUID regionId) {
		return SELLING_REGIONS;
	}

	public static RegionSell getSellingRegionForPlayer(UUID playerId) {
		return SELLING_REGIONS.size() == 0 ? null : SELLING_REGIONS.values().stream().filter(sell -> sell.getOwner().equals(playerId) && sell.isBought()).findFirst().orElse(null);
	}

	public static void setRegionBought(UUID regionId) {
		RegionSell sell = SELLING_REGIONS.get(regionId);
		if(sell != null){
			sell.setBought(true);
			SELLING_REGIONS.put(regionId, sell);
			Player owner = getPlayer(sell.getOwner());
			if(owner != null && owner.isOnline()) {
				MessageUtils.sendSuccessMessage(owner, RegionText.REGION_BOUGHT.getValue());
				InventoryUtils.addItemsToInventory(owner, sell.getValue().getItem(), sell.getValue().getQuantity());
				SELLING_REGIONS.remove(regionId);
			}
			saveSellingRegions();
		}
	}

	public static void removeSellingRegion(UUID regionId) {
		RegionSell region = SELLING_REGIONS.get(regionId);
		if(region != null) {
			Player owner = getPlayer(region.getOwner());
			if(owner != null && owner.isOnline()) {
				MessageUtils.sendSuccessMessage(owner, RegionText.REGION_BOUGHT.getValue());
				InventoryUtils.addItemsToInventory(owner, region.getValue().getItem(), region.getValue().getQuantity());
				SELLING_REGIONS.remove(regionId);
				saveSellingRegions();
			}
		}
	}

	/**
	 * Get a Region from name
	 * 
	 * @param name
	 *            The name of the region
	 * @return The Region with that name if exists, null otherwise
	 */
	public static Region load(String name) {
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if (region.getName().equalsIgnoreCase(name))
				return region;
		}
		return null;
	}

	/**
	 * Get a Region from ID
	 * 
	 * @param id
	 *            The ID of the region
	 * @return The Region with that name if exists, null otherwise
	 */
	public static Region load(UUID id) {
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if (region.getId().compareTo(id) == 0)
				return region;
		}
		return null;
	}

	/**
	 * Update a Region to the latest RegionVersion
	 * 
	 * @param region
	 *            The Region to update
	 */
	public static void update(Region region) {
		boolean removeOld = false;
		if (region.isLocal()) {
			LocalRegion newRegion = new LocalRegion(region.getName(), ((LocalRegion) region).getFirstPoint(),
					((LocalRegion) region).getSecondPoint(), region.getTemplate());
			newRegion.setMembers(((LocalRegion) region).getMembers());
			newRegion.setPriority(((LocalRegion) region).getPriority());
			if (((LocalRegion) region).getSpawnLocation() != null)
				newRegion.setSpawnLocation(((LocalRegion) region).getSpawnLocation());
			if (((LocalRegion) region).getTeleportLocation() != null)
				newRegion.setTeleportLocation(((LocalRegion) region).getTeleportLocation());
			newRegion.setFlags(region.getFlags());
			newRegion.setCommands(region.getCommands());
			newRegion.setInteracts(region.getInteracts());
			newRegion.setVehicles(region.getVehicles());
			newRegion.setExplosions(region.getExplosions());
			newRegion.setMobs(region.getMobs());
			newRegion.setGamemode(region.getGameMode());
			newRegion.setName(region.getName());
			newRegion.updateFlags();
			removeOld = save(newRegion);
		} else {
			GlobalRegion newRegion = new GlobalRegion(region.getName(), region.getTemplate());
			newRegion.setFlags(region.getFlags());
			newRegion.setGamemode(region.getGameMode());
			newRegion.setCommands(region.getCommands());
			newRegion.setInteracts(region.getInteracts());
			newRegion.setVehicles(region.getVehicles());
			newRegion.setExplosions(region.getExplosions());
			newRegion.setMobs(region.getMobs());
			newRegion.updateFlags();
			removeOld = save(newRegion);
		}
		if (removeOld) {
			if (region.getId() == null)
				removeByName(region);
			else
				remove(region);
		}
	}

	/**
	 * Get All Regions
	 * 
	 * @return The list of all Regions
	 */
	public static ArrayList<Region> getAllRegions() {
		ArrayList<Region> regions = new ArrayList<Region>();
		if (getRegionFolder().exists())
			regions.addAll(loadRegions(getRegionFolder(), RegionType.LOCAL));
		if (getGlobalRegionFolder().exists())
			regions.addAll(loadRegions(getGlobalRegionFolder(), RegionType.GLOBAL));
		return regions;
	}

	/**
	 * Check if there are old regions to convert
	 */
	public static boolean shouldConvertOldRegions() {
		return getOldRegionFolder().exists() || getOldGlobalRegionFolder().exists();
	}

	/**
	 * Convert the old region format to the new one (from UniverseGuard to
	 * UniverseGuard2)
	 */
	public static void convertOldRegions() {
		if (getOldRegionFolder().exists())
			loadOldRegions(getOldRegionFolder(), RegionType.LOCAL);
		if (getOldGlobalRegionFolder().exists())
			loadOldRegions(getOldGlobalRegionFolder(), RegionType.GLOBAL);
	}

	/**
	 * Convert the old region format to the new one (from UniverseGuard to
	 * UniverseGuard2)
	 * 
	 * @param directory
	 *            The folder where the old regions are
	 * @param type
	 *            The RegionType to convert
	 */
	public static void loadOldRegions(File directory, RegionType type) {
		for (File file : directory.listFiles()) {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(file));
				JsonObject jsonObject = new JsonObject();
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(new FileReader(file));
				jsonObject = jsonElement.getAsJsonObject();
				for (Iterator<Entry<String, JsonElement>> elements = jsonObject.entrySet().iterator(); elements
						.hasNext();) {
					Entry<String, JsonElement> element = elements.next();
					JsonObject jObj = element.getValue().getAsJsonObject();
					Region region = null;
					if (type.equals(RegionType.LOCAL))
						region = new LocalRegion(element.getKey());
					else
						region = new GlobalRegion(element.getKey(), false);

					JsonObject flagObject = jObj.getAsJsonObject("flags");
					for (EnumRegionFlag flag : EnumRegionFlag.values()) {
						JsonElement flagElement = flagObject.get(flag.getName().toLowerCase());
						region.setFlag(flag, flagElement != null ? flagElement.getAsBoolean() : flag.getValue());
					}
					JsonElement flagBuildElement = flagObject.get("build");
					region.setFlag(EnumRegionFlag.PLACE, flagBuildElement != null ? flagBuildElement.getAsBoolean()
							: EnumRegionFlag.PLACE.getValue());
					region.setFlag(EnumRegionFlag.DESTROY, flagBuildElement != null ? flagBuildElement.getAsBoolean()
							: EnumRegionFlag.DESTROY.getValue());

					JsonElement flagChestsElement = flagObject.get("chests");
					region.setFlag(EnumRegionFlag.CHESTS, flagChestsElement != null ? flagChestsElement.getAsBoolean()
							: EnumRegionFlag.CHESTS.getValue());
					region.setFlag(EnumRegionFlag.TRAPPED_CHESTS,
							flagChestsElement != null ? flagChestsElement.getAsBoolean()
									: EnumRegionFlag.TRAPPED_CHESTS.getValue());

					JsonElement flagUseElement = flagObject.get("use");
					for (EnumRegionInteract interact : EnumRegionInteract.values())
						region.setInteract(interact,
								flagUseElement != null ? flagUseElement.getAsBoolean() : interact.getValue());

					JsonElement flagVehiclePlaceElement = flagObject.get("vehicleplace");
					for (EnumRegionVehicle vehicle : EnumRegionVehicle.values())
						region.setVehiclePlace(vehicle,
								flagVehiclePlaceElement != null ? flagVehiclePlaceElement.getAsBoolean()
										: vehicle.getPlace());

					JsonElement flagVehicleDestroyElement = flagObject.get("vehicledestroy");
					for (EnumRegionVehicle vehicle : EnumRegionVehicle.values())
						region.setVehicleDestroy(vehicle,
								flagVehicleDestroyElement != null ? flagVehicleDestroyElement.getAsBoolean()
										: vehicle.getDestroy());

					JsonElement flagExplosionDestroyElement = flagObject.get("otherexplosions");
					for (EnumRegionExplosion explosion : EnumRegionExplosion.values())
						region.setExplosionDestroy(explosion,
								flagExplosionDestroyElement != null ? flagExplosionDestroyElement.getAsBoolean()
										: explosion.getDestroy());

					JsonElement flagExplosionDamageElement = flagObject.get("otherexplosionsdamage");
					for (EnumRegionExplosion explosion : EnumRegionExplosion.values())
						region.setExplosionDamage(explosion,
								flagExplosionDamageElement != null ? flagExplosionDamageElement.getAsBoolean()
										: explosion.getDamage());

					JsonElement flagTntElement = flagObject.get("tnt");
					region.setExplosionDestroy(EnumRegionExplosion.TNT,
							flagTntElement != null ? flagTntElement.getAsBoolean()
									: EnumRegionExplosion.TNT.getDestroy());
					JsonElement flagTntDamageElement = flagObject.get("tntdamage");
					region.setExplosionDamage(EnumRegionExplosion.TNT,
							flagTntDamageElement != null ? flagTntDamageElement.getAsBoolean()
									: EnumRegionExplosion.TNT.getDamage());

					JsonElement flagCreeperElement = flagObject.get("creeperexplosions");
					region.setExplosionDestroy(EnumRegionExplosion.CREEPER,
							flagCreeperElement != null ? flagCreeperElement.getAsBoolean()
									: EnumRegionExplosion.CREEPER.getDestroy());
					JsonElement flagCreeperDamageElement = flagObject.get("mobdamage");
					region.setExplosionDamage(EnumRegionExplosion.CREEPER,
							flagCreeperDamageElement != null ? flagCreeperDamageElement.getAsBoolean()
									: EnumRegionExplosion.CREEPER.getDamage());

					if (type.equals(RegionType.LOCAL)) {
						JsonObject firstPoint = jObj.getAsJsonObject("pos1");
						JsonObject secondPoint = jObj.getAsJsonObject("pos2");
						JsonObject teleportPoint = jObj.getAsJsonObject("teleport");
						JsonObject spawnPoint = jObj.getAsJsonObject("spawn");
						((LocalRegion) region).setFirstPoint(new RegionLocation(firstPoint.get("x").getAsInt(),
								firstPoint.get("y").getAsInt(), firstPoint.get("z").getAsInt(),
								jObj.get("dimension").getAsString(), jObj.get("world").getAsString()));
						((LocalRegion) region).setSecondPoint(new RegionLocation(secondPoint.get("x").getAsInt(),
								secondPoint.get("y").getAsInt(), secondPoint.get("z").getAsInt(),
								jObj.get("dimension").getAsString(), jObj.get("world").getAsString()));
						((LocalRegion) region).setTeleportLocation(new RegionLocation(teleportPoint.get("x").getAsInt(),
								teleportPoint.get("y").getAsInt(), teleportPoint.get("z").getAsInt(),
								jObj.get("dimension").getAsString(), jObj.get("world").getAsString()));
						((LocalRegion) region).setSpawnLocation(new RegionLocation(spawnPoint.get("x").getAsInt(),
								spawnPoint.get("y").getAsInt(), spawnPoint.get("z").getAsInt(),
								jObj.get("dimension").getAsString(), jObj.get("world").getAsString()));
						((LocalRegion) region).setPriority(jObj.get("priority").getAsInt());
						((LocalRegion) region).setGamemode(jObj.get("gamemode").getAsString());
					}

					save(region);
				}

			} catch (FileNotFoundException e) {
				LogUtils.log(e);
				LogUtils.print(TextColors.RED, RegionText.REGION_LOAD_EXCEPTION.getValue(), "rg utils");
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						LogUtils.log(e);
						LogUtils.print(TextColors.RED, RegionText.REGION_READER_CLOSE_EXCEPTION.getValue(), "rg utils");
					}
				}
			}
		}
	}

	/**
	 * Get all Regions of the given type
	 * 
	 * @param directory
	 *            The folder where the regions are
	 * @param type
	 *            The RegionType to load
	 * @return The list of the Regions of that given RegionType
	 */
	public static ArrayList<Region> loadRegions(File directory, RegionType type) {
		ArrayList<Region> regions = new ArrayList<Region>();
		for (File file : directory.listFiles()) {
			Gson gson = new Gson();
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(file));
				if (type == RegionType.LOCAL) {
					LocalRegion region = gson.fromJson(bufferedReader, LocalRegion.class);
					if (region != null)
						regions.add(region);
				} else {
					GlobalRegion region = gson.fromJson(bufferedReader, GlobalRegion.class);
					if (region != null)
						regions.add(region);
				}
			} catch (FileNotFoundException e) {
				LogUtils.log(e);
				LogUtils.print(TextColors.RED, RegionText.REGION_LOAD_EXCEPTION.getValue(), "rg utils");
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						LogUtils.log(e);
						LogUtils.print(TextColors.RED, RegionText.REGION_READER_CLOSE_EXCEPTION.getValue(), "rg utils");
					}
				}
			}
		}
		return regions;
	}

	public static Region reloadRegion(UUID id, RegionType type) {
		Region loadedRegion = load(id);
		Region region = null;
		if(loadedRegion != null) {
			File file = getFile(loadedRegion);
			Gson gson = new Gson();
			BufferedReader bufferedReader = null;
			try {
				assert loadedRegion != null;
				bufferedReader = new BufferedReader(new FileReader(file));
				if (type == RegionType.LOCAL) {
					region = gson.fromJson(bufferedReader, LocalRegion.class);
				} else {
					region = gson.fromJson(bufferedReader, GlobalRegion.class);
				}
			} catch (FileNotFoundException e) {
				LogUtils.log(e);
				LogUtils.print(TextColors.RED, RegionText.REGION_LOAD_EXCEPTION.getValue(), "rg utils");
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						LogUtils.log(e);
						LogUtils.print(TextColors.RED, RegionText.REGION_READER_CLOSE_EXCEPTION.getValue(), "rg utils");
					}
				}
			}
		}

		if(region != null) {
			Region cachedRegion = null;
			for (Region cached : UniverseGuard.ALL_REGIONS) {
				if (cached.getId() != null && cached.getId().compareTo(region.getId()) == 0) {
					cachedRegion = cached;
					break;
				}
			}
			if (cachedRegion != null) {
				UniverseGuard.ALL_REGIONS.remove(cachedRegion);
			}
			UniverseGuard.ALL_REGIONS.add(region);
		}

		return region;
	}

	/**
	 * Set the pending Region for a player
	 * 
	 * @param player
	 *            The player
	 * @param region
	 *            The Region
	 */
	public static void setPendingRegion(Player player, Region region) {
		if (region == null)
			PENDINGS.remove(player);
		else if (!PENDINGS.containsKey(player)) {
			PENDINGS.put(player, region);
		}
	}
	
	/**
	 * Set the pending Region for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param region
	 *            The Region
	 */
	public static void setPendingRegion(CommandSource source, Region region) {
		if (region == null)
			PENDINGS.remove(source);
		else if (!PENDINGS.containsKey(source)) {
			PENDINGS.put(source, region);
		}
	}

	/**
	 * Get the pending region for a player
	 * 
	 * @param player
	 *            The player
	 * @return The pending Region of the player if exists, null otherwise
	 */
	public static Region getPendingRegion(Player player) {
		return PENDINGS.get(player);
	}
	
	/**
	 * Get the pending region for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @return The pending Region of the command source if exists, null otherwise
	 */
	public static Region getPendingRegion(CommandSource source) {
		return PENDINGS.get(source);
	}

	/**
	 * Update the pending region of a player
	 * 
	 * @param player
	 *            The player
	 * @param region
	 *            The pending Region
	 */
	public static void updatePendingRegion(Player player, Region region) {
		setPendingRegion(player, null);
		setPendingRegion(player, region);
	}
	
	/**
	 * Update the pending region of a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param region
	 *            The pending Region
	 */
	public static void updatePendingRegion(CommandSource source, Region region) {
		setPendingRegion(source, null);
		setPendingRegion(source, region);
		if(source instanceof Player && region.isLocal()) {
			RegionUtils.setRegionScoreboard((Player)source, (LocalRegion)region);
		}
	}

	/**
	 * Check if a player has a pending Region
	 * 
	 * @param player
	 *            The player
	 * @return true if the player has a pending Region, false otherwise
	 */
	public static boolean hasPendingRegion(Player player) {
		return getPendingRegion(player) != null;
	}
	
	/**
	 * Check if a CommandSource has a pending Region
	 * 
	 * @param source
	 *            The command source
	 * @return true if the command source has a pending Region, false otherwise
	 */
	public static boolean hasPendingRegion(CommandSource source) {
		return getPendingRegion(source) != null;
	}

	/**
	 * Shows the list of all Regions to a player
	 * 
	 * @param player
	 *            The player
	 */
	public static void printRegionsList(Player player) {
		printRegionsList((CommandSource)player);
	}
	
	/**
	 * Shows the list of all Regions to a CommandSource
	 * 
	 * @param source
	 *            The command source
	 */
	public static void printRegionsList(CommandSource source) {
		StringBuilder regions = new StringBuilder();
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if (!region.getFlag(EnumRegionFlag.HIDE_REGION) && !region.getTemplate())
				regions.append(region.getName() + ", ");
		}
		MessageUtils.sendMessage(source, RegionText.REGION_LIST.getValue(), TextColors.GOLD);
		MessageUtils.sendMessage(source, regions.substring(0, regions.length() - 2), TextColors.YELLOW);
	}

	/**
	 * Check if a player is online
	 * 
	 * @param uuid
	 *            The UUID of the player
	 * @return true if the player with that UUID is online, false otherwise
	 */
	public static boolean isOnline(UUID uuid) {
		return Sponge.getServer().getPlayer(uuid).isPresent();
	}

	/**
	 * Shows the region informations to a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param region
	 *            The Region
	 */
	public static void printRegion(CommandSource source, Region region) {
		MessageUtils.sendMessage(source, RegionText.REGION_INFO.getValue() + ": " + region.getName(), TextColors.GOLD);
		MessageUtils.sendMessage(source, RegionText.TYPE.getValue() + ": " + region.getType().toString(),
				TextColors.YELLOW);
		if (region.isLocal()) {
			LocalRegion localRegion = (LocalRegion) region;
			MessageUtils.sendMessage(source,
					RegionText.PRIORITY.getValue() + ": " + String.valueOf(localRegion.getPriority()),
					TextColors.YELLOW);
			if (!localRegion.getFarewellMessage().isEmpty())
				MessageUtils.sendMessage(source,
						RegionText.FAREWELL_MESSAGE.getValue() + ": " + localRegion.getFarewellMessage(),
						TextColors.RED);
			if (!localRegion.getGreetingMessage().isEmpty())
				MessageUtils.sendMessage(source,
						RegionText.GREETING_MESSAGE.getValue() + ": " + localRegion.getGreetingMessage(),
						TextColors.GREEN);
			if(localRegion.getValue() != null) {
                MessageUtils.sendMessage(source,
                        RegionText.REGION_VALUE.getValue() + ": " + localRegion.getValue().getItem() + " (" + localRegion.getValue().getQuantity() + ")",
                        TextColors.GREEN);
            }
			if (!localRegion.getFlag(EnumRegionFlag.HIDE_LOCATIONS)) {
				MessageUtils.sendMessage(source,
						RegionText.FROM.getValue() + ": " + localRegion.getFirstPoint().toString(), TextColors.AQUA);
				MessageUtils.sendMessage(source,
						RegionText.TO.getValue() + ": " + localRegion.getSecondPoint().toString(), TextColors.AQUA);
				MessageUtils.sendMessage(source,
						RegionText.TELEPORT.getValue() + ": " + localRegion.getTeleportLocation().toString(),
						TextColors.AQUA);
				MessageUtils.sendMessage(source,
						RegionText.SPAWN.getValue() + ": " + localRegion.getSpawnLocation().toString(),
						TextColors.AQUA);
			}
			if (!localRegion.getFlag(EnumRegionFlag.HIDE_MEMBERS)) {
				MessageUtils.sendMessage(source, RegionText.MEMBERS.getValue(), TextColors.YELLOW);
				ArrayList<Text> members = new ArrayList<Text>();
				for (int i = 0; i < localRegion.getMembers().size(); i++) {
					RegionMember member = localRegion.getMembers().get(i);
					members.add(Text.of(
							source instanceof Player && member.getUUID().equals(((Player)source).getUniqueId()) ? TextColors.AQUA
									: isOnline(member.getUUID()) ? TextColors.GREEN : TextColors.RED,
							member.getUsername(), i < localRegion.getMembers().size() - 1 ? ", " : ""));
				}
				source.sendMessage(Text.of(members.toArray()));
			}

            MessageUtils.sendMessage(source, RegionText.EFFECTS.getValue(), TextColors.YELLOW);
            ArrayList<Text> effects = new ArrayList<Text>();
            for (int i = 0; i < localRegion.getEffects().size(); i++) {
                RegionEffect effect = ((LocalRegion) region).getEffects().get(i);
                effects.add(Text.of(TextColors.GREEN, effect.getEffect().getTranslation().get(), " ", effect.getLevel() + 1, String.valueOf(i < localRegion.getEffects().size() - 1 ? ", " : "")));
            }
            source.sendMessage(Text.of(effects.toArray()));
		}
		if (!region.getFlag(EnumRegionFlag.HIDE_FLAGS)) {
			MessageUtils.sendMessage(source, RegionText.FLAGS.getValue(), TextColors.YELLOW);
			ArrayList<Text> flags = new ArrayList<Text>();
			for (int i = 0; i < region.getFlags().size(); i++) {
				RegionFlag flag = region.getFlags().get(i);
				flags.add(Text.of(flag.getValue() ? TextColors.GREEN : TextColors.RED, flag.getName(),
						i < region.getFlags().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(flags.toArray()));
			MessageUtils.sendMessage(source, RegionText.INTERACTS.getValue(), TextColors.YELLOW);
			ArrayList<Text> interacts = new ArrayList<Text>();
			for (int i = 0; i < region.getInteracts().size(); i++) {
				RegionInteract interact = region.getInteracts().get(i);
				interacts.add(Text.of(interact.isEnabled() ? TextColors.GREEN : TextColors.RED, interact.getBlock(),
						i < region.getInteracts().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(interacts.toArray()));
			MessageUtils.sendMessage(source, RegionText.EXPLOSIONS_DAMAGE.getValue(), TextColors.YELLOW);
			ArrayList<Text> explosionsDamage = new ArrayList<Text>();
			for (int i = 0; i < region.getExplosions().size(); i++) {
				RegionExplosion explosion = region.getExplosions().get(i);
				explosionsDamage.add(Text.of(explosion.getDamage() ? TextColors.GREEN : TextColors.RED,
						explosion.getExplosion(), i < region.getExplosions().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(explosionsDamage.toArray()));
			MessageUtils.sendMessage(source, RegionText.EXPLOSIONS_DESTROY.getValue(), TextColors.YELLOW);
			ArrayList<Text> explosionsDestroy = new ArrayList<Text>();
			for (int i = 0; i < region.getExplosions().size(); i++) {
				RegionExplosion explosion = region.getExplosions().get(i);
				explosionsDestroy.add(Text.of(explosion.getDestroy() ? TextColors.GREEN : TextColors.RED,
						explosion.getExplosion(), i < region.getExplosions().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(explosionsDestroy.toArray()));

			MessageUtils.sendMessage(source, RegionText.VEHICLES_PLACE.getValue(), TextColors.YELLOW);
			ArrayList<Text> vehiclesPlace = new ArrayList<Text>();
			for (int i = 0; i < region.getVehicles().size(); i++) {
				RegionVehicle vehicle = region.getVehicles().get(i);
				vehiclesPlace.add(Text.of(vehicle.getPlace() ? TextColors.GREEN : TextColors.RED, vehicle.getName(),
						i < region.getVehicles().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(vehiclesPlace.toArray()));

			MessageUtils.sendMessage(source, RegionText.VEHICLES_DESTROY.getValue(), TextColors.YELLOW);
			ArrayList<Text> vehiclesDestroy = new ArrayList<Text>();
			for (int i = 0; i < region.getVehicles().size(); i++) {
				RegionVehicle vehicle = region.getVehicles().get(i);
				vehiclesDestroy.add(Text.of(vehicle.getDestroy() ? TextColors.GREEN : TextColors.RED, vehicle.getName(),
						i < region.getVehicles().size() - 1 ? ", " : ""));
			}
			source.sendMessage(Text.of(vehiclesDestroy.toArray()));

			if (region.getMobs().size() > 0) {
				MessageUtils.sendMessage(source, RegionText.MOBS_SPAWN.getValue(), TextColors.YELLOW);
				ArrayList<Text> mobsSpawn = new ArrayList<Text>();
				for (int i = 0; i < region.getMobs().size(); i++) {
					RegionMob mob = region.getMobs().get(i);
					mobsSpawn.add(Text.of(mob.getSpawn() ? TextColors.GREEN : TextColors.RED, mob.getMob(),
							i < region.getMobs().size() - 1 ? ", " : ""));
				}
				source.sendMessage(Text.of(mobsSpawn.toArray()));

				MessageUtils.sendMessage(source, RegionText.MOBS_PVE.getValue(), TextColors.YELLOW);
				ArrayList<Text> mobsPve = new ArrayList<Text>();
				for (int i = 0; i < region.getMobs().size(); i++) {
					RegionMob mob = region.getMobs().get(i);
					mobsPve.add(Text.of(mob.getPve() ? TextColors.GREEN : TextColors.RED, mob.getMob(),
							i < region.getMobs().size() - 1 ? ", " : ""));
				}
				source.sendMessage(Text.of(mobsPve.toArray()));

				MessageUtils.sendMessage(source, RegionText.MOBS_DAMAGE.getValue(), TextColors.YELLOW);
				ArrayList<Text> mobsDamage = new ArrayList<Text>();
				for (int i = 0; i < region.getMobs().size(); i++) {
					RegionMob mob = region.getMobs().get(i);
					mobsDamage.add(Text.of(mob.getDamage() ? TextColors.GREEN : TextColors.RED, mob.getMob(),
							i < region.getMobs().size() - 1 ? ", " : ""));
				}
				source.sendMessage(Text.of(mobsDamage.toArray()));

				MessageUtils.sendMessage(source, RegionText.MOBS_DROP.getValue(), TextColors.YELLOW);
				ArrayList<Text> mobsDrop = new ArrayList<Text>();
				for (int i = 0; i < region.getMobs().size(); i++) {
					RegionMob mob = region.getMobs().get(i);
					mobsDrop.add(Text.of(mob.getDrop() ? TextColors.GREEN : TextColors.RED, mob.getMob(),
							i < region.getMobs().size() - 1 ? ", " : ""));
				}
				source.sendMessage(Text.of(mobsDrop.toArray()));

                MessageUtils.sendMessage(source, RegionText.MOBS_INTERACT.getValue(), TextColors.YELLOW);
                ArrayList<Text> mobsInteract = new ArrayList<Text>();
                for (int i = 0; i < region.getMobs().size(); i++) {
                    RegionMob mob = region.getMobs().get(i);
                    mobsInteract.add(Text.of(mob.getInteract() ? TextColors.GREEN : TextColors.RED, mob.getMob(),
                            i < region.getMobs().size() - 1 ? ", " : ""));
                }
                source.sendMessage(Text.of(mobsInteract.toArray()));
			}

		}
	}

	/**
	 * Get a player from it's UUID
	 */
	public static Player getPlayer(UUID uuid) {
		Optional<Player> onlinePlayer = Sponge.getServer().getPlayer(uuid);

		if (onlinePlayer.isPresent()) {
			return onlinePlayer.get();
		}

		Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
		if (userStorage.isPresent() && userStorage.get().get(uuid).isPresent()) {
			if (userStorage.get().get(uuid).get().getPlayer().isPresent())
				return userStorage.get().get(uuid).get().getPlayer().get();
			return null;
		}
		return null;
	}

	/**
	 * Get the member of a Region
	 * 
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @return The RegionMember if the player is a member of that Region, null
	 *         otherwise
	 */
	public static RegionMember getMember(LocalRegion region, Player player) {
		for (RegionMember member : region.getMembers()) {
			if (member.getUUID().equals(player.getUniqueId()))
				return member;
		}
		return null;
	}

	/**
	 * Get the member of a Region by UUID
	 * 
	 * @param region
	 *            The Region
	 * @param player
	 *            The player's UUID
	 * @return The RegionMember if the player is a member of that Region, null
	 *         otherwise
	 */
	public static RegionMember getMember(LocalRegion region, UUID player) {
		for (RegionMember member : region.getMembers()) {
			if (member.getUUID().equals(player))
				return member;
		}
		return null;
	}
	
	/**
	 * Check if a player is a member of a Region
	 * 
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @return true if the player is a member of that Region, false otherwise
	 */
	public static boolean isMemberByUUID(Region region, UUID player) {
		if (region.isLocal()) {
			return getMember((LocalRegion) region, player) != null;
		}
		return false;
	}

	/**
	 * Check if a player is a member of a Region
	 * 
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @return true if the player is a member of that Region, false otherwise
	 */
	public static boolean isMember(Region region, Player player) {
		if (region.isLocal()) {
			return getMember((LocalRegion) region, player) != null;
		}
		return false;
	}

	/**
	 * Check if a player is the owner of a Region
	 * 
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @return true is the player is the owner of that Region, false otherwise
	 */
	public static boolean isOwner(Region region, Player player) {
		if (region.isLocal()) {
			return isMember(region, player)
					&& getMember((LocalRegion) region, player).getRole().equals(RegionRole.OWNER);
		}
		return false;
	}

	/**
	 * Check if a player has a Region permission
	 * 
	 * @param player
	 *            The Player
	 * @param region
	 *            The Region
	 * @return true if the player has permissions in that Region, false otherwise
	 */
	public static boolean hasPermission(Player player, Region region) {
		return PermissionUtils.hasPermission(player, RegionPermission.REGION) || isMember(region, player);
	}

	/**
	 * Check if a player has a Region
	 * 
	 * @param player
	 *            The player
	 * @return true if the player has a Region, false otherwise
	 */
	public static boolean hasRegion(Player player) {
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if (isMember(region, player))
				return true;
		}
		return false;
	}

	/**
	 * Check if a player has a Region given it's UUID
	 * 
	 * @param player
	 *            The player's UUID
	 * @return true if the player has a Region, false otherwise
	 */
	public static boolean hasRegionByUUID(UUID player) {
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if (isMemberByUUID(region, player))
				return true;
		}
		return false;
	}

	/**
	 * Shows the Help page header for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param page
	 *            The page to display
	 */
	public static void printHelpHeader(CommandSource source, int page) {
		MessageUtils.sendMessage(source, RegionText.HELP.getValue() + "(" + String.valueOf(page) + "/5)",
				TextColors.GOLD);
	}

	/**
	 * Shows the FlagHelp page header for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param page
	 *            The page to display
	 */
	public static void printFlagHelpHeader(CommandSource source, int page) {
		MessageUtils.sendMessage(source, RegionText.FLAG_HELP.getValue() + "(" + String.valueOf(page) + "/10)",
				TextColors.GOLD);
	}

	/**
	 * Shows the command help text for a CommandSource
	 * 
	 * @param source
	 *            The source
	 * @param command
	 *            The command
	 * @param text
	 *            The help text
	 */
	public static void printHelpFor(CommandSource source, String command, RegionText text) {
		MessageUtils.sendMessage(source, "/rg " + command + " - " + text.getValue(), TextColors.YELLOW);
	}

	/**
	 * Shows the flag help text for a C
	 * 
	 * @param source
	 *            The player
	 * @param flag
	 *            The flag
	 * @param text
	 *            The help text
	 */
	public static void printFlagHelpFor(CommandSource source, EnumRegionFlag flag, RegionText text) {
		printFlagHelpFor(source, flag.getName(), text);
	}

	/**
	 * Shows the flag help text for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param flag
	 *            The flag
	 * @param text
	 *            The help text
	 */
	public static void printFlagHelpFor(CommandSource source, String flag, RegionText text) {
		MessageUtils.sendMessage(source, flag + " - " + text.getValue(), TextColors.YELLOW);
	}

	/**
	 * Check if a location is in a Region
	 * 
	 * @param region
	 *            The Region
	 * @param location
	 *            The location
	 * @return true if that location is in that Region, false otherwise
	 */
	public static boolean isInRegion(LocalRegion region, Location<World> location) {
	    if(region.getTemplate())
	        return false;
		Location<World> pos1 = region.getFirstPoint().getLocation();
		Location<World> pos2 = region.getSecondPoint().getLocation();
		if (pos1 != null && pos2 != null) {
			int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
			int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
			int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
			int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
			int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
			int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

			return region.getWorld().equals(location.getExtent())
					&& region.getFirstPoint().getDimension()
							.equalsIgnoreCase(location.getExtent().getDimension().getType().getId())
					&& ((location.getBlockX() >= x1 && location.getBlockX() <= x2)
							&& (location.getBlockY() >= y1 && location.getBlockY() <= y2)
							&& (location.getBlockZ() >= z1 && location.getBlockZ() <= z2));
		}
		return false;
	}

	/**
	 * Get a Region at a location
	 * 
	 * @param location
	 *            The location
	 * @return The Region at the given location if exists, the GlobalRegion of the
	 *         location world otherwise
	 */
	public static Region getRegion(Location<World> location) {
		LocalRegion localRegion = getLocalRegion(location);
		return localRegion != null ? localRegion.getTemplate() ? null : localRegion : getGlobalRegion(location);
	}

	/**
	 * Get all Regions at a location
	 * 
	 * @param location
	 *            The location
	 * @return The Regions at the given location if exists, null otherwise
	 */
	public static ArrayList<LocalRegion> getAllRegionsAt(Location<World> location) {
		ArrayList<LocalRegion> localRegions = getAllLocalRegionsAt(location);
		return localRegions;
	}

	/**
	 * Get a LocalRegion at a location
	 * 
	 * @param location
	 *            The location
	 * @return The LocalRegion at the given location if exists, null otherwise
	 */
	public static LocalRegion getLocalRegion(Location<World> location) {
		LocalRegion region = null;
		for (Region r : UniverseGuard.ALL_REGIONS) {
			if (r.isLocal() && isInRegion((LocalRegion) r, location)) {
				if (region == null || ((LocalRegion) r).getPriority() >= region.getPriority())
					region = ((LocalRegion) r);
			}
		}
		return region;
	}

	/**
	 * Get all LocalRegions at a location
	 * 
	 * @param location
	 *            The location
	 * @return The LocalRegions at the given location if exists, null otherwise
	 */
	public static ArrayList<LocalRegion> getAllLocalRegionsAt(Location<World> location) {
		ArrayList<LocalRegion> regions = new ArrayList<LocalRegion>();
		for (Region r : UniverseGuard.ALL_REGIONS) {
			if (r.isLocal() && isInRegion((LocalRegion) r, location) && !r.getFlag(EnumRegionFlag.HIDE_REGION)) {
				regions.add((LocalRegion) r);
			}
		}
		return regions;
	}

	/**
	 * Get a GlobalRegion at a location
	 * 
	 * @param location
	 *            The location
	 * @return The GlobalRegion at the given location
	 */
	public static GlobalRegion getGlobalRegion(Location<World> location) {
	    GlobalRegion global = (GlobalRegion) load(location.getExtent().getName());
		return global != null ? global.getTemplate() ? null : global : null;
	}

	/**
	 * Handle a Flag event
	 * 
	 * @param event
	 *            The event
	 * @param flag
	 *            The flag
	 * @param location
	 *            The location
	 * @param player
	 *            The player
	 * @param type
	 *            The EventType
	 * @return true if the event has been cancelled, false otherwise
	 */
	public static boolean handleEvent(Cancellable event, EnumRegionFlag flag, Location<World> location, Player player,
			RegionEventType type) {
		Region region = RegionUtils.getRegion(location);
		return handleEvent(event, flag, region, player, type);
	}

	/**
	 * Handle a Flag event for a Region
	 * 
	 * @param event
	 *            The event
	 * @param flag
	 *            The flag
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @param type
	 *            The EventType
	 * @return true if the event has been cancelled, false otherwise
	 */
	private static boolean handleEvent(Cancellable event, EnumRegionFlag flag, Region region, Player player,
			RegionEventType type) {
		if (region != null) {
			boolean cancel = flag.equals(EnumRegionFlag.INVINCIBLE) == region.getFlag(flag);
			if (player != null) {
				if (type.equals(RegionEventType.LOCAL)) {
					if (region.isLocal())
						cancel = cancel && !RegionUtils.hasPermission(player, region);
					else if (PermissionUtils.hasPermission(player, RegionPermission.REGION))
						cancel = false;
				} else if (PermissionUtils.hasPermission(player, RegionPermission.REGION))
					cancel = false;
			}
			if (cancel) {
				if (event != null)
					event.setCancelled(true);
				if (player != null)
					MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
				return true;
			}
		}
		return false;
	}

	/**
	 * Handle the Interact event for a Region
	 * 
	 * @param event
	 *            The event
	 * @param interact
	 *            The interact
	 * @param region
	 *            The Region
	 * @param player
	 *            The player
	 * @return true if the event has been cancelled, false otherwise
	 */
	public static boolean handleInteract(Cancellable event, EnumRegionInteract interact, Region region, Player player) {
		if (region != null) {
			if (!region.getInteract(interact) && !RegionUtils.hasPermission(player, region)) {
				event.setCancelled(true);
				MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
				return true;
			}
		}
		return false;
	}

	/**
	 * Shows a help page for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param page
	 *            The page
	 */
	public static void printHelpPage(CommandSource source, int page) {
		printHelpHeader(source, page);
		switch (page) {
		case 1:
		default:
			printHelpFor(source, "", RegionText.REGION_HELP_RG);
			printHelpFor(source, "save", RegionText.REGION_HELP_SAVE);
			printHelpFor(source, "info [region]", RegionText.REGION_HELP_INFO);
			printHelpFor(source, "delete [region]", RegionText.REGION_HELP_DELETE);
			printHelpFor(source, "name [name]", RegionText.REGION_HELP_NAME);
			break;
		case 2:
			printHelpFor(source, "list", RegionText.REGION_HELP_LIST);
			printHelpFor(source, "gamemode [gamemode]", RegionText.REGION_HELP_GAMEMODE);
			printHelpFor(source, "edit [region]", RegionText.REGION_HELP_EDIT);
			printHelpFor(source, "flag [subflag] [flag] [value]", RegionText.REGION_HELP_FLAG);
			printHelpFor(source, "add [role] [player] (region)", RegionText.REGION_HELP_ADD);
			break;
		case 3:
			printHelpFor(source, "remove [player] (region)", RegionText.REGION_HELP_REMOVE);
			printHelpFor(source, "setteleport [x] [y] [z]", RegionText.REGION_HELP_SET_TELEPORT);
			printHelpFor(source, "setspawn [x] [y] [z]", RegionText.REGION_HELP_SET_SPAWN);
			printHelpFor(source, "teleport [region]", RegionText.REGION_HELP_TELEPORT);
			printHelpFor(source, "spawn [region]", RegionText.REGION_HELP_SPAWN);
			break;
		case 4:
			printHelpFor(source, "priority [priority]", RegionText.REGION_HELP_PRIORITY);
			printHelpFor(source, "command [command]", RegionText.REGION_HELP_COMMAND);
			printHelpFor(source, "expand [direction] (blocks)", RegionText.REGION_HELP_EXPAND);
			printHelpFor(source, "here", RegionText.REGION_HELP_HERE);
			printHelpFor(source, "reload", RegionText.REGION_HELP_RELOAD);
			break;
		case 5:
			printHelpFor(source, "farewell", RegionText.REGION_HELP_FAREWELL);
			printHelpFor(source, "greeting", RegionText.REGION_HELP_GREETING);
            printHelpFor(source, "effectadd [effect] [amplifier]", RegionText.REGION_HELP_EFFECT_ADD);
            printHelpFor(source, "effectremove [effect]", RegionText.REGION_HELP_EFFECT_REMOVE);
			printHelpFor(source, "setvalue [region] [item] [quantity]", RegionText.REGION_HELP_SET_VALUE);
			break;
        case 6:
            printHelpFor(source, "removevalue [region]", RegionText.REGION_HELP_REMOVE_VALUE);
            printHelpFor(source, "buy [region]", RegionText.REGION_HELP_BUY);
            printHelpFor(source, "sell [region]", RegionText.REGION_HELP_SELL);
            printHelpFor(source, "excludeblock [block] [type]", RegionText.REGION_HELP_EXCLUDE_BLOCK);
            printHelpFor(source, "includeblock [block] [type]", RegionText.REGION_HELP_INCLUDE_BLOCK);
            break;
        case 7:
            printHelpFor(source, "tempplate [value]", RegionText.REGION_HELP_TEMPLATE);
            printHelpFor(source, "removefarewell", RegionText.REGION_HELP_REMOVEFAREWELL);
            printHelpFor(source, "removegreeting", RegionText.REGION_HELP_REMOVEGREETING);
            printHelpFor(source, "itemuse", RegionText.REGION_HELP_ITEMUSE);
			printHelpFor(source, "globalfor", RegionText.REGION_HELP_GLOBAL_FOR);
            printHelpFor(source, "help (flag) (page)", RegionText.REGION_HELP_HELP);
            break;
		}
	}

	/**
	 * Shows a flag help page for a CommandSource
	 * 
	 * @param source
	 *            The command source
	 * @param page
	 *            The page
	 */
	public static void printFlagHelpPage(CommandSource source, int page) {
		printFlagHelpHeader(source, page);
		switch (page) {
		case 1:
		default:
			printFlagHelpFor(source, EnumRegionFlag.PLACE, RegionText.REGION_FLAG_HELP_PLACE);
			printFlagHelpFor(source, EnumRegionFlag.DESTROY, RegionText.REGION_FLAG_HELP_DESTROY);
			printFlagHelpFor(source, EnumRegionFlag.PVP, RegionText.REGION_FLAG_HELP_PVP);
			printFlagHelpFor(source, EnumRegionFlag.EXP_DROP, RegionText.REGION_FLAG_HELP_EXP_DROP);
			printFlagHelpFor(source, EnumRegionFlag.ITEM_DROP, RegionText.REGION_FLAG_HELP_ITEM_DROP);
			break;
		case 2:
			printFlagHelpFor(source, EnumRegionFlag.ENDERPEARL, RegionText.REGION_FLAG_HELP_ENDERPEARL);
			printFlagHelpFor(source, EnumRegionFlag.SLEEP, RegionText.REGION_FLAG_HELP_SLEEP);
			printFlagHelpFor(source, EnumRegionFlag.LIGHTER, RegionText.REGION_FLAG_HELP_LIGHTER);
			printFlagHelpFor(source, EnumRegionFlag.CHESTS, RegionText.REGION_FLAG_HELP_CHESTS);
			printFlagHelpFor(source, EnumRegionFlag.TRAPPED_CHESTS, RegionText.REGION_FLAG_HELP_TRAPPED_CHESTS);
			break;
		case 3:
			printFlagHelpFor(source, EnumRegionFlag.WATER_FLOW, RegionText.REGION_FLAG_HELP_WATER_FLOW);
			printFlagHelpFor(source, EnumRegionFlag.LAVA_FLOW, RegionText.REGION_FLAG_HELP_LAVA_FLOW);
			printFlagHelpFor(source, EnumRegionFlag.LEAF_DECAY, RegionText.REGION_FLAG_HELP_LEAF_DECAY);
			printFlagHelpFor(source, EnumRegionFlag.FIRE_SPREAD, RegionText.REGION_FLAG_HELP_FIRE_SPREAD);
			printFlagHelpFor(source, EnumRegionFlag.POTION_SPLASH, RegionText.REGION_FLAG_HELP_POTION_SPLASH);
			break;
		case 4:
			printFlagHelpFor(source, EnumRegionFlag.FALL_DAMAGE, RegionText.REGION_FLAG_HELP_FALL_DAMAGE);
			printFlagHelpFor(source, EnumRegionFlag.CAN_TP, RegionText.REGION_FLAG_HELP_CAN_TP);
			printFlagHelpFor(source, EnumRegionFlag.CAN_SPAWN, RegionText.REGION_FLAG_HELP_CAN_SPAWN);
			printFlagHelpFor(source, EnumRegionFlag.HUNGER, RegionText.REGION_FLAG_HELP_HUNGER);
			printFlagHelpFor(source, EnumRegionFlag.ENDER_CHESTS, RegionText.REGION_FLAG_HELP_ENDER_CHESTS);
			break;
		case 5:
			printFlagHelpFor(source, EnumRegionFlag.WALL_DAMAGE, RegionText.REGION_FLAG_HELP_WALL_DAMAGE);
			printFlagHelpFor(source, EnumRegionFlag.DROWN, RegionText.REGION_FLAG_HELP_DROWN);
			printFlagHelpFor(source, EnumRegionFlag.INVINCIBLE, RegionText.REGION_FLAG_HELP_INVINCIBLE);
			printFlagHelpFor(source, EnumRegionFlag.CACTUS_DAMAGE, RegionText.REGION_FLAG_HELP_CACTUS_DAMAGE);
			printFlagHelpFor(source, EnumRegionFlag.FIRE_DAMAGE, RegionText.REGION_FLAG_HELP_FIRE_DAMAGE);
			break;
		case 6:
			printFlagHelpFor(source, EnumRegionFlag.HIDE_LOCATIONS, RegionText.REGION_FLAG_HELP_HIDE_LOCATIONS);
			printFlagHelpFor(source, EnumRegionFlag.HIDE_FLAGS, RegionText.REGION_FLAG_HELP_HIDE_FLAGS);
			printFlagHelpFor(source, EnumRegionFlag.HIDE_MEMBERS, RegionText.REGION_FLAG_HELP_HIDE_MEMBERS);
			printFlagHelpFor(source, EnumRegionFlag.SEND_CHAT, RegionText.REGION_FLAG_HELP_SEND_CHAT);
			printFlagHelpFor(source, EnumRegionFlag.ENDERMAN_GRIEF, RegionText.REGION_FLAG_HELP_ENDERMAN_GRIEF);
			break;
		case 7:
			printFlagHelpFor(source, EnumRegionFlag.ENDER_DRAGON_BLOCK_DAMAGE,
					RegionText.REGION_FLAG_HELP_ENDER_DRAGON_BLOCK_DAMAGE);
			printFlagHelpFor(source, EnumRegionFlag.ENDER_DRAGON_BLOCK_DAMAGE,
					RegionText.REGION_FLAG_HELP_ENDER_DRAGON_BLOCK_DAMAGE);
			printFlagHelpFor(source, "interact", RegionText.REGION_FLAG_HELP_INTERACT);
			printFlagHelpFor(source, "vehiceplace", RegionText.REGION_FLAG_HELP_VEHICLE_PLACE);
			printFlagHelpFor(source, "vehicedestroy", RegionText.REGION_FLAG_HELP_VEHICLE_DESTROY);
			break;
		case 8:
			printFlagHelpFor(source, "explosiondamage", RegionText.REGION_FLAG_HELP_EXPLOSION_DAMAGE);
			printFlagHelpFor(source, "explosiondestroy", RegionText.REGION_FLAG_HELP_EXPLOSION_DESTROY);
			printFlagHelpFor(source, "mobspawn", RegionText.REGION_FLAG_HELP_MOB_SPAWN);
			printFlagHelpFor(source, "mobdamage", RegionText.REGION_FLAG_HELP_MOB_DAMAGE);
			printFlagHelpFor(source, "mobpve", RegionText.REGION_FLAG_HELP_MOB_PVE);
            printFlagHelpFor(source, "mobinteract", RegionText.REGION_FLAG_HELP_MOB_INTERACT);
			break;
		case 9:
			printFlagHelpFor(source, EnumRegionFlag.ITEM_PICKUP, RegionText.REGION_FLAG_HELP_ITEM_PICKUP);
			printFlagHelpFor(source, EnumRegionFlag.OTHER_LIQUIDS_FLOW, RegionText.REGION_FLAG_HELP_OTHER_LIQUIDS_FLOW);
			printFlagHelpFor(source, EnumRegionFlag.HIDE_REGION, RegionText.REGION_FLAG_HELP_HIDE_REGION);
			printFlagHelpFor(source, EnumRegionFlag.ICE_MELT, RegionText.REGION_FLAG_HELP_ICE_MELT);
			printFlagHelpFor(source, EnumRegionFlag.VINES_GROWTH, RegionText.REGION_FLAG_HELP_VINES_GROWTH);
			break;
		case 10:
			printFlagHelpFor(source, EnumRegionFlag.EXIT, RegionText.REGION_FLAG_HELP_EXIT);
			printFlagHelpFor(source, EnumRegionFlag.ENTER, RegionText.REGION_FLAG_HELP_ENTER);
            printFlagHelpFor(source, EnumRegionFlag.TRAMPLE, RegionText.REGION_FLAG_HELP_TRAMPLE);
            printFlagHelpFor(source, EnumRegionFlag.SHULKER_BOXES, RegionText.REGION_FLAG_HELP_SHULKER_BOXES);
            printFlagHelpFor(source, EnumRegionFlag.PISTONS, RegionText.REGION_FLAG_HELP_PISTONS);
			break;
        case 11:
            printFlagHelpFor(source, EnumRegionFlag.FROST_WALKER, RegionText.REGION_FLAG_HELP_FROST_WALKER);
            printFlagHelpFor(source, EnumRegionFlag.FISHING_POLE, RegionText.REGION_FLAG_HELP_FISHING_POLE);
            break;
		}
	}

	/**
	 * Get a player from it's username. Used to get datas about offline players that has been
	 * at least once on the server
	 * @param username the Player's username
	 * @return The player
	 */
	public static UUID getPlayerUUID(String username) {
		Optional<Player> onlinePlayer = Sponge.getServer().getPlayer(username);

	    if (onlinePlayer.isPresent()) {
	        return onlinePlayer.get().getUniqueId();
	    }
		
		Optional<User> user = Sponge.getServiceManager().provide(UserStorageService.class).get().get(username);

		if (user.isPresent())
			return user.get().getUniqueId();

		return null;
	}
	
	public static void setRegionScoreboard(Player player, LocalRegion region) {
		Scoreboard scoreboard = Scoreboard.builder().build();
        Objective objective = Objective.builder().name("Region").displayName(Text.of(TextColors.GOLD, "Region")).criterion(Criteria.DUMMY).build();
        scoreboard.addObjective(objective);
        scoreboard.updateDisplaySlot(objective, DisplaySlots.SIDEBAR);
        
        objective.getOrCreateScore(Text.of("Type: " + region.getType().name())).setScore(10);
        if(region.getName() != null && !region.getName().isEmpty())
        	objective.getOrCreateScore(Text.of("Name: " + region.getName())).setScore(5);
        if(region.getFirstPoint() != null)
        	objective.getOrCreateScore(Text.of("From: " + region.getFirstPoint().toString())).setScore(1);
        if(region.getSecondPoint() != null)
        	objective.getOrCreateScore(Text.of("To: " + region.getSecondPoint().toString())).setScore(0);
        
        player.setScoreboard(scoreboard);
	}

	public static ArrayList<Region> getPlayerRegions(UUID player) {
	    ArrayList<Region> playerRegions = new ArrayList<Region>();
	    for(Region region : UniverseGuard.ALL_REGIONS) {
	        if(isMemberByUUID(region, player))
	            playerRegions.add(region);
        }
	    return playerRegions;
    }

    public static int getPlayerMaxRegions(UUID uuid) {
	    Player player = getPlayer(uuid);
	    if(player != null) {
	        if(player.hasPermission("*"))
	            return UniverseGuard.MAX_PERMISSION_REGIONS.get("*");
	        for(RegionPermission permission : RegionPermission.values()) {
	            if(PermissionUtils.hasPermission(player, permission))
	                return UniverseGuard.MAX_PERMISSION_REGIONS.get(permission.getName());
            }
        }
	    return UniverseGuard.MAX_REGIONS;
    }

	/**
	 * Get the JSON file of a Region
	 * 
	 * @param region
	 *            The Region
	 * @return The JSON file of the Region
	 */
	public static File getFile(Region region) {
		if (region.getName().isEmpty()) {
			int index = 0;
			for (Region r : UniverseGuard.ALL_REGIONS) {
				if (r.getName().toLowerCase().startsWith("region"))
					index++;
			}
			region.setName("Region" + String.valueOf(index));
		}
		return new File((region.getType() == RegionType.LOCAL ? getRegionFolder() : getGlobalRegionFolder()) + "/"
				+ region.getId().toString() + ".json");
	}

	/**
	 * Get the JSON file of a Region based on it's name
	 * 
	 * @param region
	 *            The Region
	 * @return The JSON file of the Region
	 */
	public static File getFileByName(Region region) {
		if (region.getName().isEmpty()) {
			int index = 0;
			for (Region r : UniverseGuard.ALL_REGIONS) {
				if (r.getName().toLowerCase().startsWith("region"))
					index++;
			}
			region.setName("Region" + String.valueOf(index));
		}
		return new File((region.getType() == RegionType.LOCAL ? getRegionFolder() : getGlobalRegionFolder()) + "/"
				+ region.getName() + ".json");
	}

	/**
	 * Get the Local Regions folder
	 * 
	 * @return The Local Regions folder
	 */
	public static File getRegionFolder() {
		return new File(getConfigFolder() + "/regions");
	}

    public static File getTemplateFolder() {
        return new File(getConfigFolder() + "/templates");
    }

	/**
	 * Get the Global Regions folder
	 * 
	 * @return The Global Regions folder
	 */
	public static File getGlobalRegionFolder() {
		return new File(getConfigFolder() + "/globals");
	}

	/**
	 * Get the Old Local Regions folder
	 * 
	 * @return The Old Local Regions folder
	 */
	public static File getOldRegionFolder() {
		return new File(getConfigFolder() + "/old/regions");
	}

	/**
	 * Get the Old Global Regions folder
	 * 
	 * @return The Old Global Regions folder
	 */
	public static File getOldGlobalRegionFolder() {
		return new File(getConfigFolder() + "/old/globals");
	}

	/**
	 * Get the Configuration folder
	 * 
	 * @return The Configuration folder
	 */
	public static String getConfigFolder() {
		return "config/universeguard/";
	}
}