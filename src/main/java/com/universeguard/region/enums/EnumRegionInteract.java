/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Interacts
 * @author Jimi
 *
 */
public enum EnumRegionInteract {
	CRAFTING_TABLE("craftingtable", true),
	ENCHANTING_TABLE("enchantingtable", true),
	ITEM_FRAME("itemframe", true),
	ARMOR_STAND("armorstand", true),
	ANVIL("anvil", true),
	HOPPER("hopper", true),
	LEVER("lever", true),
	BUTTON("button", true),
	FURNACE("furnace", true),
	DOOR("door", true),
	FENCE_GATE("fencegate", true),
	TRAPDOOR("trapdoor", true),
	SIGN("sign", true);
	
	private String NAME;
	private boolean VALUE;
	
	private EnumRegionInteract(String name, boolean value) {
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
