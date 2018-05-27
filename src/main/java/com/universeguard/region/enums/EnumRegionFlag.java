/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Flags
 * @author Jimi
 *
 */
public enum EnumRegionFlag {
	PLACE("place", true),
	DESTROY("destroy", true),
	PVP("pvp", true),
	EXP_DROP("expdrop", true),
	ITEM_DROP("itemdrop", true),
	ITEM_PICKUP("itempickup", true),
	ENDERPEARL("enderpearl", true),
	SLEEP("sleep", true),
	LIGHTER("lighter", true),
	CHESTS("chests", false),
	TRAPPED_CHESTS("trappedchests", false),
	WATER_FLOW("waterflow", true),
	LAVA_FLOW("lavaflow", true),
	OTHER_LIQUIDS_FLOW("otherliquidsflow", true),
	LEAF_DECAY("leafdecay", true),
	FIRE_SPREAD("firespread", true),
	POTION_SPLASH("potionsplash", true),
	FALL_DAMAGE("falldamage", true),
	CAN_TP("cantp", true),
	CAN_SPAWN("canspawn", true),
	HUNGER("hunger", true),
	ENDER_CHESTS("enderchests", true),
	WALL_DAMAGE("walldamage", true),
	DROWN("drown", true),
	INVINCIBLE("invincible", false),
	CACTUS_DAMAGE("cactusdamage", true),
	FIRE_DAMAGE("firedamage", true),
	ENDERMAN_GRIEF("endermangrief", true),
	ENDER_DRAGON_BLOCK_DAMAGE("enderdragonblockdamage", true),
	HIDE_LOCATIONS("hidelocation", false),
	HIDE_FLAGS("hideflags", false),
	HIDE_MEMBERS("hidemembers", false),
	HIDE_REGION("hideregion", false),
	ICE_MELT("icemelt", true),
	EXIT("exit", true),
	ENTER("enter", true),
	VINES_GROWTH("vinesgrowth", true),
	SEND_CHAT("sendchat", true),
    TRAMPLE("trample", true),
    SHULKER_BOXES("shulkerboxes", true),
    PISTONS("pistons", true);
	
	private String NAME;
	private boolean VALUE;
	
	private EnumRegionFlag(String name, boolean value) {
		this.NAME = name;
		this.VALUE = value;
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public void setValue(boolean value) {
		this.VALUE = value;
	}
	
	public boolean getValue() {
		return this.VALUE;
	}
}
