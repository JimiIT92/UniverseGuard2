/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import com.universeguard.region.enums.EnumRegionVehicle;

/**
 * Region Vehicle Class
 * @author Jimi
 *
 */
public class RegionVehicle {
	private String VEHICLE;
	private boolean PLACE;
	private boolean DESTROY;
	
	public RegionVehicle(EnumRegionVehicle vehicle) {
		this.VEHICLE = vehicle.getName();
		this.PLACE = vehicle.getPlace();
		this.DESTROY = vehicle.getDestroy();
	}

	public String getName() {
		return VEHICLE;
	}

	public void setName(String name) {
		this.VEHICLE = name;
	}

	public boolean getPlace() {
		return PLACE;
	}

	public void setPlace(boolean damage) {
		this.PLACE = damage;
	}

	public boolean getDestroy() {
		return DESTROY;
	}

	public void setDestroy(boolean destroy) {
		this.DESTROY = destroy;
	}
}
