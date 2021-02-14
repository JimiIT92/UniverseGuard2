/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.enums;

/**
 * Region Permissions
 * @author Jimi
 *
 */
public enum RegionPermission {
	ALL("universeguard.all", "Grant all permissions to use Universe Guard"),
	REGION("universeguard.region", "Grant the ability to bypass region restrictions");
	
	private String VALUE;
	private String COMMENT;
	
	private RegionPermission(String value, String comment) {
		this.VALUE = value;
		this.COMMENT = comment;
	}
	
	public void setValue(String value) {
		this.VALUE = value;
	}
	
	public String getValue() {
		return this.VALUE;
	}
	
	public void setComment(String comment) {
		this.COMMENT = comment;
	}
	
	public String getComment() {
		return this.COMMENT;
	}
	
	public String getName() {
		return this.name().toLowerCase();
	}
}