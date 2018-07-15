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
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * 
 * Command Handler for /rg farewell
 * @author Jimi
 *
 */
public class RegionRemoveFarewellExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(RegionUtils.hasPendingRegion(src)) {
            Region region = RegionUtils.getPendingRegion(src);
            if(region != null) {
                if(region.isLocal()) {
                    LocalRegion localRegion = (LocalRegion)region;
                    localRegion.setFarewellMessage("");
                    if(RegionUtils.save(localRegion)) {
                        MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
                    } else {
                        MessageUtils.sendErrorMessage(src, RegionText.REGION_SAVE_EXCEPTION.getValue());
                    }
                } else
                    MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
            } else
                MessageUtils.sendErrorMessage(src, RegionText.REGION_NOT_FOUND.getValue());

        } else
            MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());

        return CommandResult.empty();
    }

    private String getCommandUsage() {
        return "/rg removefarewell";
    }

}