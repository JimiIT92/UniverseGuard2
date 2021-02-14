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
import com.universeguard.region.components.RegionEffect;
import com.universeguard.region.enums.RegionRole;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

/**
 * 
 * Command Handler for /rg add
 * @author Jimi
 *
 */
public class RegionAddEffectExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("effect") && args.hasAny("level")) {
            PotionEffectType effect = args.<PotionEffectType>getOne("effect").get();
            int level = args.<Integer>getOne("level").get();
            if(level >= 0) {
                if(RegionUtils.hasPendingRegion(src)) {
                    Region pendingRegion = RegionUtils.getPendingRegion(src);
                    if(pendingRegion.isLocal()) {
                        LocalRegion region = (LocalRegion)pendingRegion;
                        region.addEffect(effect, level);
                        MessageUtils.sendSuccessMessage(src, RegionText.TEXT_EFFECT_ADDED.getValue());
                    } else {
                        MessageUtils.sendErrorMessage(src, RegionText.REGION_LOCAL_ONLY.getValue());
                        return CommandResult.empty();
                    }
                } else {
                    MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());
                    return CommandResult.empty();
                }
            } else {
                MessageUtils.sendErrorMessage(src, RegionText.TEXT_WRONG_EFFECT_LEVEL.getValue());
                return CommandResult.empty();
            }
		} else {
			MessageUtils.sendErrorMessage(src, getCommandUsage());
		}

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg add <role> <player> (region)";
	}
	
}