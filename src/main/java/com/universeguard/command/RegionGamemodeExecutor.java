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
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
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
		if (RegionUtils.hasPendingRegion(src)) {
			if (args.hasAny("gamemode")) {
				GameMode gameMode = args.<GameMode>getOne("gamemode").get();
				if (gameMode != GameModes.NOT_SET) {
					Region region = RegionUtils.getPendingRegion(src);
					region.setGamemode(gameMode.getId());
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_GAMEMODE_UPDATED.getValue());
					RegionUtils.updatePendingRegion(src, region);
				} else {
					MessageUtils.sendErrorMessage(src, RegionText.REGION_GAMEMODE_NOT_VALID.getValue());
				}
			} else {
				MessageUtils.sendErrorMessage(src, getCommandUsage());
			}

		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg gamemode <gamemode>";
	}

}