/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import java.util.ArrayList;

import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

/**
 * 
 * Utility class for gamemdoe
 * @author Jimi
 *
 */
public class GameModeUtils {

	/**
	 * Get a GameMode from name
	 * @param name The name of the GameMode
	 * @return GameMode if exists, NOT_SET otherwise
	 */
	public static GameMode getGameMode(String name) {
		for(GameMode gameMode : getAllGameModes()) {
			if(gameMode.getId().equalsIgnoreCase(name))
				return gameMode;
		}
		
		return GameModes.NOT_SET;
	}
	
	/**
	 * Get all the GameModes
	 * @return List of GameModes
	 */
	public static ArrayList<GameMode> getAllGameModes() {
		ArrayList<GameMode> gameModes = new ArrayList<GameMode>();
		gameModes.add(GameModes.ADVENTURE);
		gameModes.add(GameModes.CREATIVE);
		gameModes.add(GameModes.NOT_SET);
		gameModes.add(GameModes.SPECTATOR);
		gameModes.add(GameModes.SURVIVAL);
		return gameModes;
	}
}
