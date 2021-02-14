/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * 
 * Command Handler for /rg tp
 * @author Jimi
 *
 */
public class RegionTemplateExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.hasAny("template")) {
            Region region = RegionUtils.getPendingRegion(src);
            boolean template = Boolean.valueOf(args.<String>getOne("template").get());
            if (region != null) {
                region.setTemplate(template);
                MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
            } else
                MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
        } else {
            MessageUtils.sendErrorMessage(src, getCommandUsage());
        }

		return CommandResult.empty();
	}
	/**
	 * Command Usage
	 * @return
	 */
	private String getCommandUsage() {
		return "/rg tp <name>";
	}

}
