/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.event.flags;

import com.universeguard.utils.LogUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * Handler for the command flag
 * @author Jimi
 *
 */
public class FlagCommandListener {

	@Listener
	public void onCommand(SendCommandEvent event, @First Player player) {
		this.handleEvent(event, event.getCommand() + (!event.getArguments().isEmpty() ? " " + event.getArguments() : ""), player);
	}
	
	private void handleEvent(SendCommandEvent event, String command, Player player) {
		Region region = RegionUtils.getRegion(player.getLocation());
		if(region != null) {
			boolean cancel = !region.isCommandEnabled(command) && !PermissionUtils.hasPermission(player, RegionPermission.REGION);
			if(cancel) {
				event.setCancelled(true);
				MessageUtils.sendHotbarErrorMessage(player, RegionText.NO_PERMISSION_REGION.getValue());
			}
		}
	}
}
