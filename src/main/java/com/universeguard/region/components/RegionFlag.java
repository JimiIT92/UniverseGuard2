/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import com.universeguard.region.enums.EnumRegionFlag;

/**
 * Region Flag Class
 * @author Jimi
 *
 */
public class RegionFlag {
	private String name;
	private boolean value;
	
	public RegionFlag(EnumRegionFlag flag) {
		this.name = flag.getName();
		this.value = flag.getValue();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return this.value;
	}
}
