/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Explosions
 * @author Jimi
 *
 */
public enum EnumRegionExplosion {
	TNT("tnt", true, true),
	CREEPER("creeper", true, true),
	ENDER_CRYSTAL("endercrystal", true, true),
	FIREBALL("fireball", true, true),
	ENDERDRAGON("enderdragon", true, true),
	OTHER_EXPLOSIONS("otherexplosions", true, true);

	private String NAME;
	private boolean DAMAGE;
	private boolean DESTROY;
	
	private EnumRegionExplosion(String name, boolean place, boolean destroy) {
		this.NAME = name;
		this.DAMAGE = place;
		this.DESTROY = destroy;
	}

	public String getName() {
		return NAME;
	}

	public void setName(String name) {
		this.NAME = name;
	}

	public boolean getDamage() {
		return DAMAGE;
	}

	public void setDamage(boolean place) {
		this.DAMAGE = place;
	}
	
	public boolean getDestroy() {
		return DESTROY;
	}

	public void setDestroy(boolean destroy) {
		this.DESTROY = destroy;
	}
}