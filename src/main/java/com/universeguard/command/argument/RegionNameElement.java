package com.universeguard.command.argument;

import java.util.ArrayList;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;

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
		return regions;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
	


}
