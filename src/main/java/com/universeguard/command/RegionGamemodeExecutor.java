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
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.GameModeUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg gamemode
 * @author Jimi
 *
 */
public class RegionGamemodeExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if (RegionUtils.hasPendingRegion(player)) {
				if (args.hasAny("gamemode")) {
					GameMode gameMode = GameModeUtils.getGameMode(args.<String>getOne("gamemode").get());
					if (gameMode != GameModes.NOT_SET) {
						Region region = RegionUtils.getPendingRegion(player);
						region.setGamemode(gameMode.getId());
						MessageUtils.sendSuccessMessage(player, RegionText.REGION_GAMEMODE_UPDATED.getValue());
						RegionUtils.updatePendingRegion(player, region);
					} else {
						MessageUtils.sendErrorMessage(player, RegionText.REGION_GAMEMODE_NOT_VALID.getValue());
					}
				} else {
					MessageUtils.sendErrorMessage(player, getCommandUsage());
				}

			} else
				MessageUtils.sendErrorMessage(player, RegionText.NO_PENDING_REGION.getValue());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg gamemode <gamemode>";
	}

}
