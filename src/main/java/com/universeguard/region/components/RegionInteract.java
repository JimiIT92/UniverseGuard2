/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import com.universeguard.region.enums.EnumRegionInteract;

/**
 * Region Interact Class
 * @author Jimi
 *
 */
public class RegionInteract {
	private String BLOCK;
	private boolean USE;
	
	public RegionInteract(EnumRegionInteract interact) {
		this.BLOCK = interact.getName();
		this.USE = interact.getValue();
	}
	

	public void setBlock(String name) {
		this.BLOCK = name;
	}
	
	public String getBlock() {
		return this.BLOCK;
	}
	
	public void setEnabled(boolean value) {
		this.USE = value;
	}
	
	public boolean isEnabled() {
		return this.USE;
	}
}
