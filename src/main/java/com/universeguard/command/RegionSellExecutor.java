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
import com.universeguard.region.components.RegionMember;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * Command Handler for /rg add
 * @author Jimi
 *
 */
public class RegionSellExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
		    Player player = (Player)src;
            if (args.hasAny("region")) {
                Region region = RegionUtils.load(args.<String>getOne("region").get());
                if (UniverseGuard.PURCHASABLE_REGIONS && region != null) {
                    LocalRegion localRegion = (LocalRegion) region;
                    localRegion.getMembers().clear();
                    localRegion.setSold(false);
                    if(RegionUtils.save(localRegion)) {
                        MessageUtils.sendSuccessMessage(src, RegionText.REGION_SELLED.getValue());
                    } else
                        MessageUtils.sendErrorMessage(src, RegionText.REGION_SAVE_EXCEPTION.getValue());
                } else
                    MessageUtils.sendErrorMessage(src, RegionText.NO_REGIONS_SELLED.getValue());
            } else {
                MessageUtils.sendErrorMessage(src, getCommandUsage());
            }
        }
		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg buy <region>";
	}
	
}