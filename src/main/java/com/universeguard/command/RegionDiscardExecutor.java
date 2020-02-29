/*
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 *
 */
package com.universeguard.command;

import com.universeguard.UniverseGuard;
import com.universeguard.region.components.RegionLocation;
import com.universeguard.utils.RegionLocationUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 *
 * Command Handler for /rg discard
 * @author Jimi
 *
 */
public class RegionDiscardExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(RegionUtils.hasPendingRegion(src)) {
            Region region = RegionUtils.getPendingRegion(src);
            if(src instanceof Player) {
                Player player = (Player)src;
                if(player.getScoreboard().getObjective("Region").isPresent())
                    player.getScoreboard().removeObjective(player.getScoreboard().getObjective("Region").get());
            }
            RegionUtils.setPendingRegion(src, null);
            RegionUtils.reloadRegion(region.getId(), region.getType());
            MessageUtils.sendSuccessMessage(src, RegionText.REGION_DISCARDED.getValue());
        } else
            MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
        return CommandResult.empty();
    }

}