/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.command;

import com.universeguard.UniverseGuard;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.components.RegionValue;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.ItemType;

/**
 * 
 * Command Handler for /rg add
 * @author Jimi
 *
 */
public class RegionRemoveValueExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("region")) {
			Region region = RegionUtils.load(args.<String>getOne("region").get());

            if(UniverseGuard.PURCHASABLE_REGIONS) {
                if(region.isLocal()) {
                    LocalRegion localRegion = (LocalRegion)region;
                    localRegion.setValue(null);
                    if(RegionUtils.save(localRegion)) {
                        MessageUtils.sendSuccessMessage(src, RegionText.REGION_SELLED_REMOVE.getValue());
                    } else
                        MessageUtils.sendErrorMessage(src, RegionText.REGION_SAVE_EXCEPTION.getValue());
                } else
                    MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
            } else
                MessageUtils.sendErrorMessage(src, RegionText.PURCHASABLE_REGIONS_DISABLED.getValue());
		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg sell <region> <item> <quantity>";
	}
	
}