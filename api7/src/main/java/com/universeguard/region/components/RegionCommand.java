/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

/**
 * Region Command Class
 * @author Jimi
 *
 */
public class RegionCommand {
	private String COMMAND;
	private boolean ENABLED;
	
	public RegionCommand(String name, boolean value) {
		this.COMMAND = name;
		this.ENABLED = value;
	}
	
	public void setCommand(String name) {
		this.COMMAND = name;
	}
	
	public String getCommand() {
		return this.COMMAND;
	}
	
	public void setEnabled(boolean value) {
		this.ENABLED = value;
	}
	
	public boolean isEnabled() {
		return this.ENABLED;
	}
}
