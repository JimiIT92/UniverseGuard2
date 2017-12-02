/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import com.universeguard.region.enums.EnumRegionExplosion;

/**
 * Region Explosion Class
 * @author Jimi
 *
 */
public class RegionExplosion {
	private String EXPLOSION;
	private boolean DAMAGE;
	private boolean DESTROY;
	
	public RegionExplosion(EnumRegionExplosion explosion) {
		this.EXPLOSION = explosion.getName();
		this.DAMAGE = explosion.getDamage();
		this.DESTROY = explosion.getDestroy();
	}

	public String getExplosion() {
		return this.EXPLOSION;
	}

	public void setExplosion(String explosion) {
		this.EXPLOSION = explosion;
	}

	public boolean getDamage() {
		return this.DAMAGE;
	}

	public void setDamage(boolean damage) {
		this.DAMAGE = damage;
	}

	public boolean getDestroy() {
		return this.DESTROY;
	}

	public void setDestroy(boolean destroy) {
		this.DESTROY = destroy;
	}
	
	
}
