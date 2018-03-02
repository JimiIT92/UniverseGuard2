/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Texts, used for translations
 * @author Jimi
 *
 */
public enum RegionText {
	REGION("Region"),
	NO_REGION_HERE("There's no Region here!"),
	LOADING("Loading..."),
	LOADED("Loaded!"),
	LOADING_CONFIGURATION("Loading Configuration"),
	LOADED_CONFIGURATION("Configuration loaded!"),
	CONFIGURATION_UPDATING_REGIONS("Updating regions..."),
	CONFIGURATION_REGIONS_UPDATED("Regions updated!"),
	CONFIGURATION_LOADING_EXCEPTION("Exception loading configuration. See log for details"),
	CONFIGURATION_SAVE_EXCEPTION("Exception saving default configuration. See log for details"),
	CONFIGURATION_CONVERTING_OLD_REGIONS("Found old regions! Converting..."),
	CONFIGURATION_OLD_REGIONS_CONVERTED("Old regions converted! You should delete the old regions folder, otherwise they will be converted every time the server starts!"),
	MESSAGE_EMPTY("The message can't be empty!"),
	REGION_SELECTOR_ADDED("Region selector added to your inventory!"),
	REGION_SAVE_EXCEPTION("Exception saving region. See log for details"),
	REGION_SAVE_INDEX_EXCEPTION("Exception saving regions index. See log for details"),
	REGION_WRITER_CLOSE_EXCEPTION("Exception closing region file writer. See log for details"),
	REGION_WRITER_INDEX_CLOSE_EXCEPTION("Exception closing regions index file writer. See log for details"),
	REGION_LOAD_EXCEPTION("Exception loading region. See log for details"),
	REGION_READER_CLOSE_EXCEPTION("Exception closing region file reader. See log for details"),
	REGION_ADD_MEMBER_EXCEPTION("Exception adding a member to a region. See log for details"),
	REGION_REMOVE_MEMBER_EXCEPTION("Exception removing a member from a region. See log for details"),
	ROLE_NOT_FOUND("Invalid role"),
	FIRST_POINT_SET("First point set to:"),
	SECOND_POINT_SET("Second point set to:"),
	REGION_SAVED("Region saved!"),
	REGION_NOT_SAVED("Region not saved! See log for details"),
	REGION_SELECT_POINTS("You must select two points first!"),
	REGION_NAME_NOT_VALID("There's already a region with that name!"),
	REGION_NAME_UPDATED("Region name updated!"),
	REGION_PRIORITY_UPDATED("Region priority updated!"),
	REGION_GREETING_MESSAGE_UPDATED("Region greeting message updated!"),
	REGION_FAREWELL_MESSAGE_UPDATED("Region farewell message updated!"),
	REGION_LOCAL_ONLY("This command is valid for local regions only!"),
	REGION_FLAG_UPDATED("Region flag updated!"),
	REGION_MOB_NOT_FOUND("Mob not found!"),
	REGION_TELEPORT_LOCATION_UPDATED("Region teleport location updated!"),
	REGION_SPAWN_LOCATION_UPDATED("Region spawn location updated!"),
	REGION_FLAG_NOT_VALID("Region flag not found!"),
	REGION_NO_TP("You can't teleport to this region!"),
	REGION_NO_SPAWN("You can't teleport to the spawn of this region!"),
	REGION_DIRECTION_NOT_VALID("Invalid direction"),
	REGION_EXPANDED("Region expanded!"),
	REGION_NO_POINT("You must select the Region points first!"),
	REGION_LIST("Regions List"),
	REGION_REMOVED("Region removed!"),
	REGION_NOT_REMOVED("Exception removing region. See log for details"),
	REGION_NOT_FOUND("Region not found!"),
	REGION_RELOAD("Regions reloaded!"),
	PENDING_REGION("You already have a pending region!"),
	PENDING_REGION_UPDATED("Pending region updated!"),
	REGION_GAMEMODE_UPDATED("Region gamemode updated!"),
	REGION_GAMEMODE_NOT_VALID("Please specify a valid gamemode"),
	REGION_COMMAND_ENABLED("Command enabled"),
	REGION_COMMAND_DISABLED("Command disabled"),
	REGION_COMMAND_NOT_FOUND("Command not found!"),
	EDITING_GLOBAL("WARNING! You are editing a global region. Edit it may affect protection in you world!"),
	PLAYER_IN_REGION("This player is already a member of a region"),
	PLAYER_NO_REGION("This player is not in a region"),
	PLAYER_ADDED_TO_REGION("Player added to region"),
	PLAYER_REMOVED_FROM_REGION("Player removed from region"),
	ADDED_TO_REGION("You've been added to region"),
	REMOVED_FROM_REGION("You've been removed from region"),
	INTERACTS("Interacts"),
	EXPLOSIONS_DAMAGE("Explosions damage"),
	EXPLOSIONS_DESTROY("Explosions destroy"),
	VEHICLES_PLACE("Vehicles place"),
	VEHICLES_DESTROY("Vehicles destroy"),
	MOBS_SPAWN("Mobs spawn"),
	MOBS_PVE("Mobs PvE"),
	MOBS_DAMAGE("Mobs Damage"),
	MOBS_DROP("Mobs Drop"),
	COMMANDS("Commands"),
	HELP("Help"),
	FLAG_HELP("Flags Help"),
	FROM("From"),
	TO("To"),
	TELEPORT("Teleport Location"),
	SPAWN("Spawn Location"),
	FLAGS("Flags"),
	TYPE("Type"),
	PRIORITY("Priority"),
	FAREWELL_MESSAGE("Farewell Message"),
	GREETING_MESSAGE("Greeting Message"),
	EDITING("Editing region"),
	MEMBERS("Members"),
	FLAG_HIDDEN("You can't view this flag"),
	REGION_INFO("Displaying info for region"),
	REGION_HELP_RG("Get the region selector item"),
	REGION_HELP_SAVE("Save the current editing region"),
	REGION_HELP_INFO("Display the details of the specified region"),
	REGION_HELP_DELETE("Delete the specified region"),
	REGION_HELP_NAME("Set the name of the current editing region"),
	REGION_HELP_LIST("Show the list of all regions"),
	REGION_HELP_GAMEMODE("Set the gamemode of the current editing region"),
	REGION_HELP_EDIT("Allows to edit the specified region"),
	REGION_HELP_FLAG("Set the flag value of the current editing region"),
	REGION_HELP_ADD("Add a player to the current editing region or the specified one with the specified role"),
	REGION_HELP_REMOVE("Remove a player from the current editing region or the specified one"),
	REGION_HELP_SET_TELEPORT("Set the teleport location of the current editing region"),
	REGION_HELP_SET_SPAWN("Set the spawn location of the current editing region"),
	REGION_HELP_TELEPORT("Teleports the player to the specified region teleport location"),
	REGION_HELP_SPAWN("Teleports the player to the specified region spawn location"),
	REGION_HELP_PRIORITY("Set the priority of the current editing region"),
	REGION_HELP_COMMAND("Allow or disallow a command (and all it's sub-commands) in the current editing region"),
	REGION_HELP_EXPAND("Expand the selected area for creating a region in the selected direction until the world limit or by the number of specified blocks"),
	REGION_HELP_HERE("Tells wich region you are currently in"),
	REGION_HELP_RELOAD("Reload cached regions"),
	REGION_HELP_FAREWELL("Set the farewell message of a Region"),
	REGION_HELP_GREETING("Set the greeting message of a Region"),
	REGION_HELP_HELP("Shows this help or the flags help"),
	REGION_FLAG_HELP_PLACE("Sets if non-members can place blocks in the region"),
	REGION_FLAG_HELP_DESTROY("Sets if non-members can break blocks in the region"),
	REGION_FLAG_HELP_PVP("Sets if players can pvp in the region"),
	REGION_FLAG_HELP_EXP_DROP("Sets if mobs can drop experience in the region"),
	REGION_FLAG_HELP_ITEM_DROP("Sets if players are allowed to drop items in the region"),
	REGION_FLAG_HELP_ITEM_PICKUP("Sets if players are allowed to pickup items in the region"),
	REGION_FLAG_HELP_ENDERPEARL("Sets if non-members are allowed to use enderpearls in the region"),
	REGION_FLAG_HELP_SLEEP("Sets if non-members are allowed to sleep in the region"),
	REGION_FLAG_HELP_LIGHTER("Sets if non-members are allowed to use lighter in the region"),
	REGION_FLAG_HELP_CHESTS("Sets if non-members are allowed to open chests in the region"),
	REGION_FLAG_HELP_TRAPPED_CHESTS("Sets if non-members are allowed to open trapped chests in the region"),
	REGION_FLAG_HELP_WATER_FLOW("Sets if water can flow in the region"),
	REGION_FLAG_HELP_LAVA_FLOW("Sets if lava can flow in the region"),
	REGION_FLAG_HELP_OTHER_LIQUIDS_FLOW("Sets if liquids that are not water or lava can flow in the region"),
	REGION_FLAG_HELP_LEAF_DECAY("Sets if leaves will decay in the region"),
	REGION_FLAG_HELP_FIRE_SPREAD("Sets if fire will spread in the region"),
	REGION_FLAG_HELP_POTION_SPLASH("Sets if non-members are allowed to thrown potions in the region"),
	REGION_FLAG_HELP_FALL_DAMAGE("Sets if players can take fall damage in the region"),
	REGION_FLAG_HELP_CAN_TP("Sets if players can use the /rg tp command while in the region"),
	REGION_FLAG_HELP_CAN_SPAWN("Sets if players can use the /rg spawn command while in the region"),
	REGION_FLAG_HELP_HUNGER("Sets if players will loose hunger points while in the region"),
	REGION_FLAG_HELP_ENDER_CHESTS("Sets if non-members can open enderchests in the region"),
	REGION_FLAG_HELP_WALL_DAMAGE("Sets if players can take suffocate in walls in the region"),
	REGION_FLAG_HELP_DROWN("Sets if players can drown in the region"),
	REGION_FLAG_HELP_INVINCIBLE("Sets if players are invincible in the region"),
	REGION_FLAG_HELP_CACTUS_DAMAGE("Sets if player will receive damage from cactus in the region"),
	REGION_FLAG_HELP_FIRE_DAMAGE("Sets if player will receive damage from fire in the region"),
	REGION_FLAG_HELP_ICE_MELT("Sets if ice will melt in this region"),
	REGION_FLAG_HELP_VINES_GROWTH("Sets if vines can grow in this region"),
	REGION_FLAG_HELP_EXIT("Sets if players can go outside of this region"),
	REGION_FLAG_HELP_ENTER("Sets if players can get inside this region"),
	REGION_FLAG_HELP_SEND_CHAT("Sets if players can send chat messages while in the region"),
	REGION_FLAG_HELP_HIDE_LOCATIONS("Sets if region locations will not be displayed using the /rg info command"),
	REGION_FLAG_HELP_HIDE_MEMBERS("Sets if region members will not be displayed using the /rg info command"),
	REGION_FLAG_HELP_HIDE_FLAGS("Sets if region flags will not be displayed using the /rg info or the /rg flaginfo command"),
	REGION_FLAG_HELP_HIDE_REGION("Sets if region will be displayed using the /rg here command"),
	REGION_FLAG_HELP_ENDERMAN_GRIEF("Sets if enderman can place/break blocks in the region"),
	REGION_FLAG_HELP_ENDER_DRAGON_BLOCK_DAMAGE("Sets if Ender Dragon can break blocks in the region"),
	REGION_FLAG_HELP_INTERACT("Allow/disallow block interaction in the region"),
	REGION_FLAG_HELP_EXPLOSION_DAMAGE("Allow/disallow explosion damage to entities in the region"),
	REGION_FLAG_HELP_EXPLOSION_DESTROY("Allow/disallow explosion damage to blocks in the region"),
	REGION_FLAG_HELP_VEHICLE_PLACE("Allow/disallow placing vehicles in the region"),
	REGION_FLAG_HELP_VEHICLE_DESTROY("Allow/disallow destroying vehicles in the region"),
	REGION_FLAG_HELP_MOB_SPAWN("Allow/disallow mob spawn in the region"),
	REGION_FLAG_HELP_MOB_PVE("Allow/disallow players damage to mob in the region"),
	REGION_FLAG_HELP_MOB_DAMAGE("Allow/disallow mob damage to player in the region"),
	NO_PENDING_REGION("You don't have any pending region!"),
	NO_PERMISSION_ITEM("You do not have permission to use this item!"),
	NO_PERMISSION_REGION("You don't have permission to do that!"),
	NO_PERMISSION_COMMAND("You do not have permission to use this command!");
	
	private String VALUE;
	private RegionText(String value) {
		this.VALUE = value;
	}
	
	public String getName() {
		return this.name().toLowerCase();
	}
	
	public void setValue(String value) {
		this.VALUE = value;
	}
	
	public String getValue() {
		return this.VALUE;
	}
}
