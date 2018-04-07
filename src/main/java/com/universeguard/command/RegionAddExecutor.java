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

import com.universeguard.UniverseGuard;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg add
 * @author Jimi
 *
 */
public class RegionAddExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if (args.hasAny("role") && args.hasAny("name")) {
				String username = args.<String>getOne("name").get();
				UUID member = RegionUtils.getPlayerUUID(username);
				RegionRole role = args.<RegionRole>getOne("role").get();
				if(role != null && member != null) {
					if (!RegionUtils.hasRegionByUUID(member) || !UniverseGuard.UNIQUE_REGIONS) {
						Region region = null;
						LocalRegion localRegion = null;
						if (RegionUtils.hasPendingRegion(player)) {
							region = RegionUtils.getPendingRegion(player);
						} else {
							if (args.hasAny("region")) {
								region = RegionUtils.load(args.<String>getOne("region").get());
							} else
								MessageUtils.sendErrorMessage(player, getCommandUsage());
						}
						if(region != null) {
							if(region.isLocal()) {
								localRegion = (LocalRegion) region;
								if(!RegionUtils.isMemberByUUID(localRegion, member)) {
									localRegion.addMemberByUUIDAndUsername(member, username, role);
									if(RegionUtils.save(localRegion)) {
										MessageUtils.sendSuccessMessage(player, RegionText.PLAYER_ADDED_TO_REGION.getValue() + ": " + username);
										if(RegionUtils.isOnline(member)) {
											Player onlinePlayer = RegionUtils.getPlayer(member);
											if(player != null)
												MessageUtils.sendMessage(onlinePlayer, RegionText.ADDED_TO_REGION.getValue() + ": " + region.getName(), TextColors.GOLD);	
										}
									} else
										MessageUtils.sendErrorMessage(player, RegionText.REGION_ADD_MEMBER_EXCEPTION.getValue());
								}
								else
									MessageUtils.sendErrorMessage(player, RegionText.PLAYER_IN_REGION.getValue());
							} else
								MessageUtils.sendErrorMessage(player, RegionText.REGION_LOCAL_ONLY.getValue());
						} else
							MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_FOUND.getValue());
					} else
						MessageUtils.sendErrorMessage(player, RegionText.PLAYER_IN_REGION.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.ROLE_NOT_FOUND.getValue());
				
			} else {
				MessageUtils.sendErrorMessage(player, getCommandUsage());
			}
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg add <role> <player> (region)";
	}
	
}
