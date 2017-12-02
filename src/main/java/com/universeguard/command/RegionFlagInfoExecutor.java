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
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg flaginfo
 * @author Jimi
 *
 */
public class RegionFlagInfoExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			Player player = (Player) src;
			if (args.hasAny("flag") && args.hasAny("name")) {
				Region region = RegionUtils.load(args.<String>getOne("name").get());
				if (region != null) {
					EnumRegionFlag flag = FlagUtils.getFlag(args.<String>getOne("flag").get());
					if(flag != null) {
						boolean value = region.getFlag(flag);
						MessageUtils.sendMessage(player, String.valueOf(value), value ? TextColors.GREEN : TextColors.RED);
					} else
						MessageUtils.sendErrorMessage(player, RegionText.REGION_FLAG_NOT_VALID.getValue());
				} else
					MessageUtils.sendErrorMessage(player, RegionText.REGION_NOT_FOUND.getValue());
			} else {
				MessageUtils.sendErrorMessage(player, getCommandUsage());
			}
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg info <name>";
	}

}
