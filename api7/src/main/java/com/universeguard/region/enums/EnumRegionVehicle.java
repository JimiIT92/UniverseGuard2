/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Vehicles
 * @author Jimi
 *
 */
public enum EnumRegionVehicle {
		MINECART("minecart", true, true),
		BOAT("boat", true, true);

		private String NAME;
		private boolean PLACE;
		private boolean DESTROY;
		
		private EnumRegionVehicle(String name, boolean place, boolean destroy) {
			this.NAME = name;
			this.PLACE = place;
			this.DESTROY = destroy;
		}

		public String getName() {
			return NAME;
		}

		public void setName(String name) {
			this.NAME = name;
		}

		public boolean getPlace() {
			return PLACE;
		}

		public void setPlace(boolean place) {
			this.PLACE = place;
		}
		
		public boolean getDestroy() {
			return DESTROY;
		}

		public void setDestroy(boolean destroy) {
			this.DESTROY = destroy;
		}
	}