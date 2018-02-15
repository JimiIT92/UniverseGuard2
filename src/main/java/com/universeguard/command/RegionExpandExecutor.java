/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumDirection;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionLocationUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg expand
 * @author Jimi
 *
 */
public class RegionExpandExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if(args.hasAny("direction")) {
				if(RegionUtils.hasPendingRegion(player)) {
					EnumDirection direction = args.<EnumDirection>getOne("direction").get();
					if(direction != null) {
						Region region = RegionUtils.getPendingRegion(player);
						if(region.isLocal()) {
							LocalRegion localRegion = (LocalRegion) region;
							boolean hasBlocks = args.hasAny("blocks");
							int blocks = 0;
							if(hasBlocks)
								blocks = args.<Integer>getOne("blocks").get();
							RegionLocationUtils.expandPoint(localRegion, direction, hasBlocks, blocks);
							MessageUtils.sendMessage(player, RegionText.REGION_EXPANDED.getValue());
							RegionUtils.updatePendingRegion(player, localRegion);
						}
						else
							MessageUtils.sendErrorMessage(player, RegionText.REGION_LOCAL_ONLY.getValue());
					}
					else
						MessageUtils.sendErrorMessage(player, RegionText.REGION_DIRECTION_NOT_VALID.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.NO_PENDING_REGION.getValue());				
			} else
				MessageUtils.sendErrorMessage(player, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg expand <direction> (blocks)";
	}

}
