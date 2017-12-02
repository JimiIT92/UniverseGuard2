/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

/**
 * Region Mob Class
 * @author Jimi
 *
 */
public class RegionMob {
	private String MOB;
	private boolean SPAWN;
	private boolean PVE;
	private boolean DAMAGE;
	private boolean DROP;
	
	public RegionMob(String name) {
		this.MOB = name.toLowerCase();
		this.SPAWN = true;
		this.PVE = true;
		this.DAMAGE = true;
		this.DROP = true;
	}

	public String getMob() {
		return MOB;
	}

	public void setMob(String mob) {
		this.MOB = mob.toLowerCase();
	}

	public boolean getSpawn() {
		return SPAWN;
	}

	public void setSpawn(boolean spawn) {
		this.SPAWN = spawn;
	}

	public boolean getPve() {
		return PVE;
	}

	public void setPve(boolean pve) {
		this.PVE = pve;
	}

	public boolean getDamage() {
		return DAMAGE;
	}

	public void setDamage(boolean damage) {
		this.DAMAGE = damage;
	}
	
	public void setDrop(boolean drop) {
		this.DROP = drop;
	}
	
	public boolean getDrop() {
		return this.DROP;
	}
}
