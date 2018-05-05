package com.universeguard.command.argument;

import java.util.ArrayList;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.universeguard.region.enums.RegionPoint;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class RegionPointCommandElement extends PatternMatchingCommandElement{

	public RegionPointCommandElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		ArrayList<String> points = new ArrayList<String>();
		for(RegionPoint subFlag : RegionPoint.values())
			points.add(subFlag.toString().toLowerCase());
		return points;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
}
