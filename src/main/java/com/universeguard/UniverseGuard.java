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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.universeguard.command.*;
import com.universeguard.command.argument.*;
import com.universeguard.event.EventPlayerJoin;
import com.universeguard.event.FlagEffectListener;
import com.universeguard.event.flags.*;
import com.universeguard.region.enums.*;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.World;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.universeguard.event.EventListener;
import com.universeguard.event.EventRegionSelect;
import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
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
	static final String VERSION = "2.21";
    /**
     * Region Version Number
     */
    public static final float REGION_VERSION = Float.parseFloat(VERSION);
	/**
	 * Plugin ID
	 */
	static final String ID = "universeguard";
	/**
	 * Plugin Name
	 */
	public static final String NAME = "Universe Guard 2";
	/**
	 * Plugin Description
	 */
	static final String DESCRIPTION = "An easy to use world protection plugin for Sponge";
	/**
	 * Plugin Author
	 */
	static final String AUTHOR = "JimiIT92, Brycey92, eheimer";
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
     * The Effect Timer update frequency (in milliseconds)
     */
    public static int EFFECT_TIMER = 100;
	/**
	 * Sets if players can be in more Regions
	 */
	public static boolean UNIQUE_REGIONS = true;
    /**
     * Sets if Regions must have a max size
     */
    public static boolean LIMIT_REGIONS_SIZE = false;
    /**
     * The max size a Region can be. This represents
     * the distance between the first and the second point.
     */
    public static int MAX_REGION_SIZE = 100;
    /**
     * Sets if players can be in a max amount of Regions
     * Works only if UNIQUE_REGIONS is set to false
     */
    public static boolean LIMIT_PLAYER_REGIONS = false;
    /**
     * The max number of Regions a player ca be member or owner
     */
	public static int MAX_REGIONS = 10;

    public static HashMap<String, Integer> MAX_PERMISSION_REGIONS = new HashMap<>();
    /**
     * If Regions can be purchased
     */
	public static boolean PURCHASABLE_REGIONS = false;
    /**
     * If Regions can have potion effects
     */
	public static boolean USE_EFFECTS = false;
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
	 * The item set to be used as a Region Selector.
	 * Default value is "minecraft:stick"
	 */
	public static ItemType SELECTOR_ITEM = ItemTypes.STICK;
	
	/**
	 * onInit Method. Called on Plugin Load
	 * @param event GameInitializationEvent
	 */
	@Listener
	public void onInit(GameInitializationEvent event) {
		// Initialize the Logger
		LogUtils.init(LOGGER);
		LogUtils.print(RegionText.LOADING.getValue(), "init");
		// Set the static instance
		INSTANCE = this;
		// Load the configuration
		this.loadConfig();
		// Load the regions
		UniverseGuard.ALL_REGIONS = RegionUtils.getAllRegions();
		RegionUtils.loadSellingRegions();
		// Register the commands
		this.registerCommands();
		// Register the events
		this.registerEvents();
		
		LogUtils.print(RegionText.LOADED.getValue(), "init");
	}
	/**
	 * onGameStart Method. Called after the Plugin start
	 * @param event GameStartedServerEvent
	 */
	@Listener
	public void onGameStart(GameStartedServerEvent event) {
		// Check for Global regions. If one is missing then create it
		LogUtils.print(RegionText.CONFIGURATION_UPDATING_REGIONS.getValue(), "init");
		for(World w : Sponge.getServer().getWorlds()) {
			if(RegionUtils.load(w.getName()) == null)
				RegionUtils.save(new GlobalRegion(w.getName(), false));
		}
		// Update regions to the latest RegionVersion
		this.updateRegions();
		// Convert the old region format to the new one (from UniverseGuard to UniverseGuard2)
		if(RegionUtils.shouldConvertOldRegions()) {
			LogUtils.print(RegionText.CONFIGURATION_CONVERTING_OLD_REGIONS.getValue(), "init");
			RegionUtils.convertOldRegions();
			LogUtils.print(RegionText.CONFIGURATION_OLD_REGIONS_CONVERTED.getValue(), "init");
		}
		LogUtils.print(RegionText.CONFIGURATION_REGIONS_UPDATED.getValue(), "init");
	}

	/**
	 * Update regions to the latest RegionVersion
	 */
	private void updateRegions() {
		ArrayList<Region> updatedRegions = new ArrayList<>();
		for(Region region : UniverseGuard.ALL_REGIONS) {
			if(region.getVersion() != REGION_VERSION) {
				updatedRegions.add(region);
			}
		}
		for(Region region : updatedRegions) {
			RegionUtils.update(region);
		}
		RegionUtils.saveIndex();
	}
	/**
	 * Load configuration
	 */
	private void loadConfig() {
		try {
			LogUtils.print(RegionText.LOADING_CONFIGURATION.getValue(), "init");
			// If there's no config file then create it
			if (!CONFIG.exists())
				CONFIG.createNewFile();
			// Save the configuration file
			this.saveDefaultConfig();
			CONFIG_NODE = CONFIG_LOADER.load();
			// Set permissions
			PermissionUtils.getValues(CONFIG_NODE);
			// Set flags
			FlagUtils.getValues(GAME, CONFIG_NODE);
			// Set translations
			TranslationUtils.getValues(CONFIG_NODE);
			LogUtils.print(RegionText.LOADED_CONFIGURATION.getValue(), "init");
		} catch (IOException e) {
			LogUtils.log(e.getMessage());
			LogUtils.print(TextColors.RED, RegionText.CONFIGURATION_LOADING_EXCEPTION.getValue(), "init");
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
			LogUtils.print(TextColors.RED, RegionText.CONFIGURATION_SAVE_EXCEPTION.getValue(), "init");
		}
	}
	/**
	 * Register commands
	 */
	private void registerCommands() {
		CommandSpec regionSave = CommandUtils.buildCommandSpec("Save a region", new RegionSaveExecutor(), RegionPermission.ALL.getValue());
		CommandSpec regionName = CommandUtils.buildCommandSpec("Set the name of a region", new RegionNameExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionDelete = CommandUtils.buildCommandSpec("Delete a region", new RegionDeleteExecutor(), RegionPermission.ALL.getValue(), new RegionNameElement(Text.of("name")));
		CommandSpec regionEdit = CommandUtils.buildCommandSpec("Allow editing a region", new RegionEditExecutor(), RegionPermission.ALL.getValue(), new RegionNameElement(Text.of("name")));
		CommandSpec regionInfo = CommandUtils.buildCommandSpec("Get informations about a region", new RegionInfoExecutor(), new RegionNameElement(Text.of("name")));
		CommandSpec regionPriority = CommandUtils.buildCommandSpec("Set the priority of a region", new RegionPriorityExecutor(), RegionPermission.ALL.getValue(), GenericArguments.integer(Text.of("priority")));
		CommandSpec regionSetTeleport = CommandUtils.buildCommandSpec("Set the teleport location of a region", new RegionSetTeleportExecutor(), RegionPermission.ALL.getValue(), GenericArguments.location(Text.of("location")));
		CommandSpec regionSetSpawn = CommandUtils.buildCommandSpec("Set the spawn location of a region", new RegionSetSpawnExecutor(), RegionPermission.ALL.getValue(), GenericArguments.location(Text.of("location")));
		CommandSpec regionTeleport = CommandUtils.buildCommandSpec("Teleports to a region teleport location", new RegionTeleportExecutor(), new RegionNameElement(Text.of("name")));
		CommandSpec regionSpawn = CommandUtils.buildCommandSpec("Teleports to a region spawn location", new RegionSpawnExecutor(), new RegionNameElement(Text.of("name")));
		CommandSpec regionList = CommandUtils.buildCommandSpec("Show the list of all regions", new RegionListExecutor());
		CommandSpec regionGamemode = CommandUtils.buildCommandSpec("Set the gamemode of a region", new RegionGamemodeExecutor(), RegionPermission.ALL.getValue(), GenericArguments.catalogedElement(Text.of("gamemode"), GameMode.class));
		CommandSpec regionHere = CommandUtils.buildCommandSpec("Tells wich region you are currently in", new RegionHereExecutor());
		CommandSpec regionReload = CommandUtils.buildCommandSpec("Reload cached regions", new RegionReloadExecutor(), RegionPermission.ALL.getValue());
		CommandSpec regionFarewell = CommandUtils.buildCommandSpec("Set the farewell message of a region", new RegionFarewellExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("message")));
		CommandSpec regionGreeting = CommandUtils.buildCommandSpec("Set the greeting message of a region", new RegionGreetingExecutor(), RegionPermission.ALL.getValue(), GenericArguments.remainingJoinedStrings(Text.of("message")));
		CommandSpec regionCopy = CommandUtils.buildCommandSpec("Copy a region into a new one", new RegionCopyExecutor(), RegionPermission.ALL.getValue(), new RegionNameElement(Text.of("name")), GenericArguments.remainingJoinedStrings(Text.of("newRegion")));
		CommandSpec regionAt = CommandUtils.buildCommandSpec("Tells wich region are at the give location", new RegionAtExecutor(), GenericArguments.location(Text.of("location")));
		CommandSpec regionCreate = CommandUtils.buildCommandSpec("Create a region at thge specified location", new RegionCreateExecutor(), RegionPermission.ALL.getValue(), GenericArguments.integer(Text.of("x1")), GenericArguments.integer(Text.of("y1")), GenericArguments.integer(Text.of("z1")), GenericArguments.integer(Text.of("x2")), GenericArguments.integer(Text.of("y2")), GenericArguments.integer(Text.of("z2")), GenericArguments.catalogedElement(Text.of("dimension"), DimensionType.class), GenericArguments.string(Text.of("world")), GenericArguments.remainingJoinedStrings(Text.of("name")));
		CommandSpec regionSet = CommandUtils.buildCommandSpec("Set a point of a pending region", new RegionSetExecutor(), RegionPermission.ALL.getValue(), new RegionPointCommandElement(Text.of("point")), GenericArguments.integer(Text.of("x")), GenericArguments.integer(Text.of("y")), GenericArguments.integer(Text.of("z")));
        CommandSpec regionAddEffect = CommandUtils.buildCommandSpec("Add a potion effect to a Region", new RegionAddEffectExecutor(), RegionPermission.ALL.getValue(), GenericArguments.catalogedElement(Text.of("effect"), PotionEffectType.class), GenericArguments.integer(Text.of("level")));
        CommandSpec regionRemoveEffect = CommandUtils.buildCommandSpec("Remove a potion effect to a Region", new RegionRemoveEffectExecutor(), RegionPermission.ALL.getValue(), GenericArguments.catalogedElement(Text.of("effect"), PotionEffectType.class));
        CommandSpec regionSetValue = CommandUtils.buildCommandSpec("Sets the value of a Region", new RegionSetValueExecutor(), new RegionOwnedNameElement(Text.of("region")), GenericArguments.catalogedElement(Text.of("item"), ItemType.class), GenericArguments.integer(Text.of("quantity")));
        CommandSpec regionRemoveValue = CommandUtils.buildCommandSpec("Remove a value from a Region", new RegionRemoveValueExecutor(), new RegionOwnedNameElement(Text.of("region")));
        CommandSpec regionBuy = CommandUtils.buildCommandSpec("Buy a Region", new RegionBuyExecutor(), new RegionToBuyNameElement(Text.of("region")));
        CommandSpec regionSell = CommandUtils.buildCommandSpec("Sells a Region", new RegionSellExecutor(), new RegionToSellNameElement(Text.of("region")));
        CommandSpec regionExcludeBlock = CommandUtils.buildCommandSpec("Exclude a block from being handled by the place or the destroy flag", new RegionExcludeBlockExecutor(), RegionPermission.ALL.getValue(),  GenericArguments.catalogedElement(Text.of("block"), BlockType.class),  GenericArguments.enumValue(Text.of("type"), EnumRegionBlock.class));
        CommandSpec regionIncludeBlock = CommandUtils.buildCommandSpec("Include a block from being handled by the place or the destroy flag", new RegionIncludeBlockExecutor(), RegionPermission.ALL.getValue(),  GenericArguments.catalogedElement(Text.of("block"), BlockType.class),  GenericArguments.enumValue(Text.of("type"), EnumRegionBlock.class));
        CommandSpec regionTemplate = CommandUtils.buildCommandSpec("Sets or remove a pending Region from being a Template", new RegionTemplateExecutor(), RegionPermission.ALL.getValue(), new BooleanElement(Text.of("template")));
        CommandSpec regionRemoveFarewell = CommandUtils.buildCommandSpec("Removes the farewell message from a Region", new RegionRemoveFarewellExecutor(), RegionPermission.ALL.getValue());
        CommandSpec regionRemoveGreeting = CommandUtils.buildCommandSpec("Removes the greeting message from a Region", new RegionRemoveGreetingExecutor(), RegionPermission.ALL.getValue());
        CommandSpec regionItemUse = CommandUtils.buildCommandSpec("Sets if you can or can't use an item inside a Region", new RegionItemUseExecutor(), RegionPermission.ALL.getValue(), new BooleanElement(Text.of("value")), GenericArguments.catalogedElement(Text.of("item"), ItemType.class));

		CommandSpec regionFlagInfo = CommandSpec.builder().description(Text.of("Get informations about a flag in a region"))
				.executor(new RegionFlagInfoExecutor())
				.arguments(GenericArguments.enumValue(Text.of("flag"), EnumRegionFlag.class), new RegionNameElement(Text.of("name")))
				.build();
		
		CommandSpec regionHelp = CommandSpec.builder().description(Text.of("Show help"))
				.executor(new RegionHelpExecutor())
				.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))), GenericArguments.optional(new BooleanElement(Text.of("flags"))))
				.build();
		
		CommandSpec regionAdd = CommandSpec.builder().description(Text.of("Add a player into a region"))
				.executor(new RegionAddExecutor())
				.arguments(GenericArguments.enumValue(Text.of("role"), RegionRole.class), GenericArguments.string(Text.of("name")), new RegionNameElement(Text.of("region")))
				.build();
		
		CommandSpec regionRemove = CommandSpec.builder().description(Text.of("Remove a player from a region"))
				.executor(new RegionRemoveExecutor())
				.arguments(GenericArguments.string(Text.of("name")), new RegionNameElement(Text.of("region")))
				.build();
		
		CommandSpec regionFlag = CommandSpec.builder().description(Text.of("Set the flag of a region"))
				.executor(new RegionFlagExecutor())
				.arguments(new SubflagCommandElement(Text.of("subflag")), new FlagCommandElement(Text.of("flag")), new BooleanElement(Text.of("value")))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionCommand = CommandSpec.builder().description(Text.of("Allow or disallow a command in a region"))
				.executor(new RegionCommandExecutor())
				.arguments(new BooleanElement(Text.of("value")), new CommandNameElement(Text.of("command")))
				.permission(RegionPermission.ALL.getValue())
				.build();
		
		CommandSpec regionExpand = CommandSpec.builder().description(Text.of("Expand the selection area of a region"))
				.executor(new RegionExpandExecutor())
				.arguments(GenericArguments.enumValue(Text.of("direction"), EnumDirection.class), GenericArguments.optional(GenericArguments.integer(Text.of("blocks"))))
				.permission(RegionPermission.ALL.getValue())
				.build();
		CommandSpec regionGlobalFor = CommandSpec.builder().description(Text.of("Create a global region for a dimension"))
					.executor(new RegionGlobalForExecutor())
					.arguments(GenericArguments.string(Text.of("dimension")))
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
				.child(regionReload, "reload")
				.child(regionFarewell, "farewell")
				.child(regionGreeting, "greeting")
				.child(regionCopy, "copy")
				.child(regionAt, "at")
				.child(regionCreate, "create")
				.child(regionSet, "set")
                .child(regionAddEffect, "effectadd")
                .child(regionRemoveEffect, "effectremove")
                .child(regionSetValue, "setvalue")
                .child(regionRemoveValue, "removevalue")
                .child(regionBuy, "buy")
                .child(regionSell, "sell")
                .child(regionExcludeBlock, "excludeblock")
                .child(regionIncludeBlock, "includeblock")
                .child(regionTemplate, "template")
                .child(regionRemoveFarewell, "removefarewell")
                .child(regionRemoveGreeting, "removegreeting")
                .child(regionItemUse, "itemuse")
				.child(regionGlobalFor, "globalfor")
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
        EventUtils.registerEvent(new FlagMobInteractListener());
		EventUtils.registerEvent(new FlagCommandListener());
		EventUtils.registerEvent(new FlagSendChatListener());
		EventUtils.registerEvent(new FlagIceMeltListener());
		EventUtils.registerEvent(new FlagVinesGrowthListener());
		EventUtils.registerEvent(new FlagExitListener());
		EventUtils.registerEvent(new FlagFarewellListener());
		EventUtils.registerEvent(new FlagGreetingListener());
        EventUtils.registerEvent(new FlagTrampleListener());
        EventUtils.registerEvent(new FlagShulkerBoxesListener());
        EventUtils.registerEvent(new FlagPistonsListener());
        EventUtils.registerEvent(new FlagFrostWalkerListener());
        EventUtils.registerEvent(new FlagFishingPoleListener());
        EventUtils.registerEvent(new FlagItemUseListener());
        EventUtils.registerEvent(new EventPlayerJoin());
		
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
		if(USE_EFFECTS) {
            Task.builder()
                    .execute(new FlagEffectListener())
                    .interval(UniverseGuard.EFFECT_TIMER, TimeUnit.MILLISECONDS)
                    .name("Effect Flag Timer Task")
                    .submit(UniverseGuard.INSTANCE);
        }

		//EventUtils.registerEvent(new EventListener());
	}
}