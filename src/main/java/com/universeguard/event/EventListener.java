/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.ConstructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.AnimateHandEvent;
import org.spongepowered.api.event.network.ChannelRegistrationEvent;
import org.spongepowered.api.event.statistic.ChangeStatisticEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.utils.LogUtils;

/**
 * Debug class, used to log events
 * @author Jimi
 *
 */
public class EventListener {
	@Listener
	public void onEvent(Event event) {
		if(!(event instanceof LoadChunkEvent) && !(event instanceof LoadWorldEvent) &&
				!(event instanceof UnloadChunkEvent) && !(event instanceof SaveWorldEvent) &&
				!(event instanceof ChannelRegistrationEvent) && !(event instanceof MoveEntityEvent) &&
				!(event instanceof CollideEntityEvent) && !(event instanceof AnimateHandEvent) &&
				!(event instanceof CollideBlockEvent) && !(event instanceof ConstructEntityEvent) &&
				!(event instanceof ChangeBlockEvent.Pre) && !(event instanceof ChangeStatisticEvent.TargetPlayer) &&
				!(event instanceof ChangeBlockEvent.Post) && !(event instanceof ChangeBlockEvent.Decay))
			LogUtils.print(TextColors.GREEN, event.getClass().getSimpleName());
	}
}
