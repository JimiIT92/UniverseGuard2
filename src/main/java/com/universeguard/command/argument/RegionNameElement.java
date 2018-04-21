package com.universeguard.command.argument;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.region.enums.RegionPermission;
import com.universeguard.utils.PermissionUtils;
import com.universeguard.utils.RegionUtils;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class RegionNameElement extends PatternMatchingCommandElement{

	public RegionNameElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		ArrayList<String> regions = new ArrayList<>();
		for (Region region : UniverseGuard.ALL_REGIONS) {
			regions.add(region.getName());
		}
		if(source instanceof Player) {
			Player player = (Player)source;
			if(!PermissionUtils.hasPermission(player, RegionPermission.ALL) && (PermissionUtils.hasPermission(player, RegionPermission.CREATE) || PermissionUtils.hasPermission(player, RegionPermission.EDIT) ||
					PermissionUtils.hasPermission(player, RegionPermission.DELETE))) {
				regions.clear();
				for (Region region : UniverseGuard.ALL_REGIONS.stream().filter(region -> RegionUtils.isMember(region, player)).collect(Collectors.toList())) {
					regions.add(region.getName());
				}
			}
		}
		return regions;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
	


}
