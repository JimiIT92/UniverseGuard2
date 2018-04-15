/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg remove
 * 
 * @author Jimi
 *
 */
public class RegionRemoveExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("name")) {
			String username = args.<String>getOne("name").get();
			UUID member = RegionUtils.getPlayerUUID(username);
			if (member != null && RegionUtils.hasRegionByUUID(member)) {
				Region region = null;
				LocalRegion localRegion = null;
				if (RegionUtils.hasPendingRegion(src)) {
					region = RegionUtils.getPendingRegion(src);
				} else {
					if (args.hasAny("region")) {
						region = RegionUtils.load(args.<String>getOne("region").get());
					} else
						MessageUtils.sendErrorMessage(src, getCommandUsage());
				}
				if (region != null) {
					if (region.isLocal()) {
						localRegion = (LocalRegion) region;
						if (RegionUtils.isMemberByUUID(localRegion, member)) {
							localRegion.removeMemberByUUID(member);
							if (RegionUtils.save(localRegion)) {
								MessageUtils.sendSuccessMessage(src,RegionText.PLAYER_REMOVED_FROM_REGION.getValue() + ": " + username);
								if(RegionUtils.isOnline(member)) {
									Player onlinePlayer = RegionUtils.getPlayer(member);
									if(onlinePlayer != null)
										MessageUtils.sendMessage(onlinePlayer,
												RegionText.REMOVED_FROM_REGION.getValue() + ": " + region.getName(),
												TextColors.GOLD);
								}
							} else
								MessageUtils.sendErrorMessage(src,
										RegionText.REGION_REMOVE_MEMBER_EXCEPTION.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.PLAYER_NO_REGION.getValue());
					} else
						MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
				} else
					MessageUtils.sendErrorMessage(src, RegionText.REGION_NOT_FOUND.getValue());
			} else
				MessageUtils.sendErrorMessage(src, RegionText.PLAYER_NO_REGION.getValue());

		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg remove <player> (region)";
	}

}
