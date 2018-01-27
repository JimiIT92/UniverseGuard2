/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.universeguard.command.*;
import com.universeguard.event.*;
import com.universeguard.event.flags.*;
import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.CommandUtils;
import com.universeguard.utils.EventUtils;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.LogUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.TranslationUtils;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

/**
 * 
 * Main Plugin Class
 * @author Jimi
 *
 */
@Plugin(id = UniverseGuard.ID, name = UniverseGuard.NAME, version = UniverseGuard.VERSION, description = UniverseGuard.DESCRIPTION, authors = UniverseGuard.AUTHOR)
public class UniverseGuard {
	/**
	 * Plugin Version
	 */
	public static final String VERSION = "2.5";
	/**
	 * Plugin ID
	 */
	public static final String ID = "universeguard";
	/**
	 * Plugin Name
	 */
	public static final String NAME = "Universe Guard 2";
	/**
	 * Plugin Description
	 */
	public static final String DESCRIPTION = "An easy to use world protection plugin for Sponge";
	/**
	 * Plugin Author
	 */
	public static final String AUTHOR = "Minehendrix";
	/**
	 * The Hunger Flag Timer update frequency (in seconds)
	 */
	public static int HUNGER_TIMER = 5;
	/**
	 * The Gamemode Flag Timer update frequency (in seconds)
	 */
	public static int GAMEMODE_TIMER = 5;
	/**
	 * The Enter Flag Timer update frequency (in milliseconds)
	 */
	public static int ENTER_FLAG_TIMER = 100;
	/**
	 * The Exit Flag Timer update frequency (in milliseconds)
	 */
	public static int EXIT_FLAG_TIMER = 100;
	/**
	 * Region Version Number
	 */
	public static final float REGION_VERSION = Float.valueOf(VERSION);
	/**
	 * Static Instance of the Plugin
	 */
	public static UniverseGuard INSTANCE;
	/**
	 * Logger Instance
	 */
	@Inject
	private Logger LOGGER;
	/**
	 * Configuration File
	 */
	@Inject
	@DefaultConfig(sharedRoot = true)
	private File CONFIG;
	/**
	 * Configuration Loader
	 */
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> CONFIG_LOADER;
	/**
	 * Configuration Node
	 */
	private CommentedConfigurationNode CONFIG_NODE;
	/**
	 * Game Instance
	 */
	@Inject
	private Game GAME;

	/**
	 * List of all regions, used to avoid massive reading from files
	 */
	public static ArrayList<Region> ALL_REGIONS;
	
	/**
	 * onInit Method. Called on Plugin Load
	 * @param event
	 */
	@Listener
	public void onInit(GameInitializationEvent event) {
		// Initialize the Logger
		LogUtils.init(LOGGER);
		LogUtils.print(RegionText.LOADING.getValue());
		// Set the static instance
		INSTANCE = this;
		// Load the configuration
		this.loadConfig();
		// Load the regions
		UniverseGuard.ALL_REGIONS = RegionUtils.getAllRegions();
		// Register the commands
		this.registerCommands();
		// Register the events
		this.registerEvents();
		LogUtils.print(RegionText.LOADED.getValue());
	}
	/**
	 * onGameStart Method. Called after the Plugin start
	 * @param event
	 */
	@Listener
	public void onGameStart(GameStartedServerEvent event) {
		// Check for Global regions. If one is missing then create it
		LogUtils.print(RegionText.CONFIGURATION_UPDATING_REGIONS.getValue());
		for(World w : Sponge.getServer().getWorlds()) {
			if(RegionUtils.load(w.getName()) == null)
				RegionUtils.save(new GlobalRegion(w.getName()));
		}
		// Update regions to the latest RegionVersion
		this.updateRegions();
		// Convert the old region format to the new one (from UniverseGuard to UniverseGuard2)
		if(RegionUtils.shouldConvertOldRegions()) {
			LogUtils.print(RegionText.CONFIGURATION_CONVERTING_OLD_REGIONS.getValue());
			RegionUtils.convertOldRegions();
			LogUtils.print(RegionText.CONFIGURATION_OLD_REGIONS_CONVERTED.getValue());	
		}
		LogUtils.print(RegionText.CONFIGURATION_REGIONS_UPDATED.getValue());
	}

	/**
	 * Update regions to the latest RegionVersion
	 */
	private void updateRegions() {
		for(Region region : UniverseGuard.ALL_REGIONS) {
			if(region.getVersion() != REGION_VERSION)
				RegionUtils.update(region);
		}
	}
	/**
	 * Load configuration
	 */
	private void loadConfig() {
		try {
			LogUtils.print(RegionText.LOADING_CONFIGURATION.getValue());
			// If there's no config file then create it
			if (!CONFIG.exists())
				CONFIG.createNewFile();
			// Save the configuration file
			this.saveDefaultConfig();
			CONFIG_NODE = CONFIG_LOADER.load();
			// Set permissions
			PermissionUtils.getValues(CONFIG_NODE);
			// Set flags
			FlagUtils.getValues(CONFIG_NODE);
			// Set translations
			TranslationUtils.getValues(CONFIG_NODE);
			LogUtils.print(RegionText.LOADED_CONFIGURATION.getValue());
		} catch (IOException e) {
			LogUtils.log(e.getMessage());
			LogUtils.print(TextColors.RED, RegionText.CONFIGURATION_LOADING_EXCEPTION.getValue());
		}
	}
	/**
	 * Save the configuration file
	 * If a node is not present than save the default value
	 */
	private void saveDefaultConfig() {
		try {
			CONFIG_NODE = CONFIG_LOADER.load();
			// Set permissions
			PermissionUtils.setDefaultValues(CONFIG_NODE);
			// Set flags
			FlagUtils.setDefaultValues(CONFIG_NODE);
			// Set translations
			TranslationUtils.setDefaultValues(CONFIG_NODE);
			CONFIG_LOADER.save(CONFIG_NODE);
		} catch (IOException e) {
			LogUtils.log(e.getMessage());
			LogUtils.print(TextColors.RED, RegionText.CONFIGURATION_SAVE_EXCEPTION.getValue());
		}
	}
	/**
	 * Register commands
	 */
	private void registerCommands() {
		CommandSpec regionSave = CommandUtils.buildCommandSpec("Save a region", new RegionSaveExecutor(), RegionPermission.ALL.getValue());
		CommandSpec regionName = CommandUtils.buildCommandSpec("Set the name of a region", new RegionNameExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionDelete = CommandUtils.buildCommandSpec("Delete a region", new RegionDeleteExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionEdit = CommandUtils.buildCommandSpec("Allow editing a region", new RegionEditExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionInfo = CommandUtils.buildCommandSpec("Get informations about a region", new RegionInfoExecutor(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionPriority = CommandUtils.buildCommandSpec("Set the priority of a region", new RegionPriorityExecutor(), RegionPermission.ALL.getValue(), GenericArguments.integer(Text.of("priority")));
		CommandSpec regionSetTeleport = CommandUtils.buildCommandSpec("Set the teleport location of a region", new RegionSetTeleportExecutor(), RegionPermission.ALL.getValue(), GenericArguments.location(Text.of("location")));
		CommandSpec regionSetSpawn = CommandUtils.buildCommandSpec("Set the spawn location of a region", new RegionSetSpawnExecutor(), RegionPermission.ALL.getValue(), GenericArguments.location(Text.of("location")));
		CommandSpec regionTeleport = CommandUtils.buildCommandSpec("Teleports to a region teleport location", new RegionTeleportExecutor(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionSpawn = CommandUtils.buildCommandSpec("Teleports to a region spawn location", new RegionSpawnExecutor(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionList = CommandUtils.buildCommandSpec("Show the list of all regions", new RegionListExecutor());
		CommandSpec regionGamemode = CommandUtils.buildCommandSpec("Set the gamemode of a region", new RegionGamemodeExecutor(), RegionPermission.ALL.getValue(), GenericArguments.string(Text.of("gamemode")));
		CommandSpec regionHere = CommandUtils.buildCommandSpec("Tells wich region you are currently in", new RegionHereExecutor());
		
		CommandSpec regionFlagInfo = CommandSpec.builder().description(Text.of("Get informations about a flag in a region"))
				.executor(new RegionFlagInfoExecutor())
				.arguments(GenericArguments.string(Text.of("flag")), GenericArguments.remainingJoinedStrings(Text.of("name")))
				.build();
		
		CommandSpec regionHelp = CommandSpec.builder().description(Text.of("Show help"))
				.executor(new RegionHelpExecutor())
				.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))), GenericArguments.optional(GenericArguments.bool(Text.of("flags"))))
				.build();
		
		
		CommandSpec regionAdd = CommandSpec.builder().description(Text.of("Add a player into a region"))
				.executor(new RegionAddExecutor())
				.arguments(GenericArguments.string(Text.of("role")), GenericArguments.player(Text.of("name")), GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionRemove = CommandSpec.builder().description(Text.of("Remove a player from a region"))
				.executor(new RegionRemoveExecutor())
				.arguments(GenericArguments.player(Text.of("name")), GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionFlag = CommandSpec.builder().description(Text.of("Set the flag of a region"))
				.executor(new RegionFlagExecutor())
				.arguments(GenericArguments.string(Text.of("subflag")), GenericArguments.string(Text.of("flag")), GenericArguments.bool(Text.of("value")))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionCommand = CommandSpec.builder().description(Text.of("Allow or disallow a command in a region"))
				.executor(new RegionCommandExecutor())
				.arguments(GenericArguments.bool(Text.of("value")), GenericArguments.remainingJoinedStrings(Text.of("command")))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionExpand = CommandSpec.builder().description(Text.of("Expand the selection area of a region"))
				.executor(new RegionExpandExecutor())
				.arguments(GenericArguments.string(Text.of("direction")), GenericArguments.optional(GenericArguments.integer(Text.of("value"))))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec region = CommandSpec.builder().description(Text.of("Region command"))
				.executor(new RegionExecutor())
				.child(regionSave, "save")
				.child(regionName, "name")
				.child(regionFlag, "flag")
				.child(regionDelete, "delete")
				.child(regionEdit, "edit")
				.child(regionInfo, "info")
				.child(regionPriority, "priority")
				.child(regionSetTeleport, "setteleport")
				.child(regionSetSpawn, "setspawn")
				.child(regionTeleport, "tp")
				.child(regionSpawn, "spawn")
				.child(regionList, "list")
				.child(regionAdd, "add")
				.child(regionRemove, "remove")
				.child(regionHelp, "help")
				.child(regionGamemode, "gamemode")
				.child(regionCommand, "command")
				.child(regionExpand, "expand")
				.child(regionFlagInfo, "flaginfo")
				.child(regionHere, "here")
				.build();
		Sponge.getCommandManager().register(this, region, Lists.newArrayList("region", "rg"));
	}
	/**
	 * Register events
	 */
	private void registerEvents() {
		EventUtils.init(GAME);
		EventUtils.registerEvent(new EventRegionSelect());
		EventUtils.registerEvent(new FlagPlaceListener());
		EventUtils.registerEvent(new FlagDestroyListener());
		EventUtils.registerEvent(new FlagPvpListener());
		EventUtils.registerEvent(new FlagExpDropListener());
		EventUtils.registerEvent(new FlagItemDropListener());
		EventUtils.registerEvent(new FlagItemPickupListener());
		EventUtils.registerEvent(new FlagEnderPearlListener());
		EventUtils.registerEvent(new FlagSleepListener());
		EventUtils.registerEvent(new FlagLighterListener());
		EventUtils.registerEvent(new FlagChestsListener());
		EventUtils.registerEvent(new FlagTrappedChestsListener());
		EventUtils.registerEvent(new FlagEnderChestsListener());
		EventUtils.registerEvent(new FlagWaterFlowListener());
		EventUtils.registerEvent(new FlagLavaFlowListener());
		EventUtils.registerEvent(new FlagOtherLiquidsFlowListener());
		EventUtils.registerEvent(new FlagLeafDecayListener());
		EventUtils.registerEvent(new FlagFireSpreadListener());
		EventUtils.registerEvent(new FlagPotionSplashListener());
		EventUtils.registerEvent(new FlagFallDamageListener());
		EventUtils.registerEvent(new FlagWallDamageListener());
		EventUtils.registerEvent(new FlagDrownListener());
		EventUtils.registerEvent(new FlagCactusDamageListener());
		EventUtils.registerEvent(new FlagFireDamageListener());
		EventUtils.registerEvent(new FlagInvincibleListener());
		EventUtils.registerEvent(new FlagEndermanGriefListener());
		EventUtils.registerEvent(new FlagEnderDragonBlockDamageListener());
		EventUtils.registerEvent(new FlagInteractListener());
		EventUtils.registerEvent(new FlagVehiclePlaceListener());
		EventUtils.registerEvent(new FlagVehicleDestroyListener());
		EventUtils.registerEvent(new FlagExplosionDamageListener());
		EventUtils.registerEvent(new FlagExplosionDestroyListener());
		EventUtils.registerEvent(new FlagMobSpawnListener());
		EventUtils.registerEvent(new FlagMobPveListener());
		EventUtils.registerEvent(new FlagMobDamageListener());
		EventUtils.registerEvent(new FlagMobDropListener());
		EventUtils.registerEvent(new FlagCommandListener());
		EventUtils.registerEvent(new FlagSendChatListener());
		EventUtils.registerEvent(new FlagIceMeltListener());
		EventUtils.registerEvent(new FlagVinesGrowthListener());
		
		Task.builder()
			.execute(new FlagHungerListener())
        	.interval(UniverseGuard.HUNGER_TIMER, TimeUnit.SECONDS)
        	.name("Hunger Timer Task")
        	.submit(UniverseGuard.INSTANCE);
		
		Task.builder()
			.execute(new FlagGamemodeListener())
			.interval(UniverseGuard.GAMEMODE_TIMER, TimeUnit.SECONDS)
			.name("Gamemode Timer Task")
			.submit(UniverseGuard.INSTANCE);
		
		Task.builder()
			.execute(new FlagEnterListener())
			.interval(UniverseGuard.ENTER_FLAG_TIMER, TimeUnit.MILLISECONDS)
			.name("Enter Flag Timer Task")
			.submit(UniverseGuard.INSTANCE);
		
		/*Task.builder()
		.execute(new FlagExitListener())
			.interval(UniverseGuard.EXIT_FLAG_TIMER, TimeUnit.MILLISECONDS)
			.name("Exit Flag Timer Task")
			.submit(UniverseGuard.INSTANCE);*/
		
		// Debug utility. Used internally
		boolean DEBUG = false;
		if(DEBUG)
			EventUtils.registerEvent(new EventListener());
	}
}
