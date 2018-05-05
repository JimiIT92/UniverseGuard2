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

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.EnumRegionInteract;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.region.enums.RegionText;
import com.universeguard.utils.FlagUtils;
import com.universeguard.utils.MessageUtils;
import com.universeguard.utils.RegionUtils;

/**
 * 
 * Command Handler for /rg flag
 * 
 * @author Jimi
 *
 */
public class RegionFlagExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (RegionUtils.hasPendingRegion(src)) {
			if (args.hasAny("subflag") && args.hasAny("flag") && args.hasAny("value")) {
				Region region = RegionUtils.getPendingRegion(src);
				boolean value = Boolean.valueOf(args.<String>getOne("value").get());
				String name = args.<String>getOne("flag").get();
				String subflag = args.<String>getOne("subflag").get();
				if (!subflag.equalsIgnoreCase("flag")) {
					switch (subflag) {
					case "interact":
						EnumRegionInteract interact = FlagUtils.getInteract(name);
						if(name.equalsIgnoreCase("all")) {
							region.setAllInteract(value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (interact != null) {
							region.setInteract(interact, value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
						break;
					case "vehicleplace":
						EnumRegionVehicle vehiclePlace = FlagUtils.getVehicle(name);
						if(name.equalsIgnoreCase("all")) {
							region.setAllVehiclePlace(value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (vehiclePlace != null) {
							region.setVehiclePlace(vehiclePlace, value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
						break;
					case "vehicledestroy":
						EnumRegionVehicle vehicleDestroy = FlagUtils.getVehicle(name);
						if(name.equalsIgnoreCase("all")) {
							region.setAllVehicleDestroy(value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (vehicleDestroy != null) {
							region.setVehicleDestroy(vehicleDestroy, value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
						break;
					case "explosiondamage":
						EnumRegionExplosion explosionDamage = FlagUtils.getExplosion(name);
						if(name.equalsIgnoreCase("all")) {
							region.setAllExplosionDamage(value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (explosionDamage != null) {
							region.setExplosionDamage(explosionDamage, value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
						break;
					case "explosiondestroy":
						EnumRegionExplosion explosionDestroy = FlagUtils.getExplosion(name);
						if(name.equalsIgnoreCase("all")) {
							region.setAllExplosionDestroy(value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (explosionDestroy != null) {
							region.setExplosionDestroy(explosionDestroy, value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
						break;
					case "mobspawn":
						if(name.equalsIgnoreCase("all")) {
							region.setMobSpawn("all", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allhostile")) {
							region.setMobSpawn("allhostile", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allpassive")) {
							region.setMobSpawn("allpassive", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (FlagUtils.getMobId(name) != null) {
							region.setMobSpawn(FlagUtils.getMobId(name), value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_MOB_NOT_FOUND.getValue());
						break;
					case "mobdamage":
						if(name.equalsIgnoreCase("all")) {
							region.setMobDamage("all", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allhostile")) {
							region.setMobDamage("allhostile", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allpassive")) {
							region.setMobDamage("allpassive", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (FlagUtils.getMobId(name) != null) {
							region.setMobDamage(FlagUtils.getMobId(name), value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_MOB_NOT_FOUND.getValue());
						break;
					case "mobpve":
						if(name.equalsIgnoreCase("all")) {
							region.setMobPve("all", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allhostile")) {
							region.setMobPve("allhostile", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allpassive")) {
							region.setMobPve("allpassive", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (FlagUtils.getMobId(name) != null) {
							region.setMobPve(FlagUtils.getMobId(name), value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_MOB_NOT_FOUND.getValue());
						break;
					case "mobdrop":
						if(name.equalsIgnoreCase("all")) {
							region.setMobDrop("all", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allhostile")) {
							region.setMobDrop("allhostile", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else if(name.equalsIgnoreCase("allpassive")) {
							region.setMobDrop("allpassive", value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						}
						else if (FlagUtils.getMobId(name) != null) {
							region.setMobDrop(FlagUtils.getMobId(name), value);
							MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
						} else
							MessageUtils.sendErrorMessage(src, RegionText.REGION_MOB_NOT_FOUND.getValue());
						break;
					default:
						MessageUtils.sendErrorMessage(src, getCommandUsage());
						break;
					}
					return CommandResult.empty();
				}
				EnumRegionFlag flag = FlagUtils.getFlag(name);
				if (flag != null) {
					region.setFlag(flag, value);
					MessageUtils.sendSuccessMessage(src, RegionText.REGION_FLAG_UPDATED.getValue());
				} else
					MessageUtils.sendErrorMessage(src, RegionText.REGION_FLAG_NOT_VALID.getValue());
				RegionUtils.updatePendingRegion(src, region);
			} else {
				MessageUtils.sendErrorMessage(src, getCommandUsage());
			}

		} else
			MessageUtils.sendErrorMessage(src, RegionText.NO_PENDING_REGION.getValue());

		return CommandResult.empty();
	}

	private String getCommandUsage() {
		return "/rg flag [subflag] <name> <value>";
	}

}