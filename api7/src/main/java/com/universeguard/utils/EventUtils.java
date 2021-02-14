/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.EventManager;

import com.universeguard.UniverseGuard;

/**
 * 
 * Utility class for Events
 * @author Jimi
 *
 */
public class EventUtils {
	
	private static EventManager EVENT_MANAGER;
	
	/**
	 * Init the Event Manager
	 * @param game The Game
	 */
	public static void init(Game game) {
		EVENT_MANAGER = game.getEventManager();
	}
	
	/**
	 * Register an event
	 * @param event The event
	 */
	public static void registerEvent(Object event) {
		EVENT_MANAGER.registerListeners(UniverseGuard.INSTANCE, event);
	}
}
