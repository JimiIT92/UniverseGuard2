package com.universeguard.command.argument;

import java.util.ArrayList;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.universeguard.region.enums.EnumRegionSubflag;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class SubflagCommandElement extends PatternMatchingCommandElement{

	public SubflagCommandElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		ArrayList<String> subFlags = new ArrayList<String>();
		for(EnumRegionSubflag subFlag : EnumRegionSubflag.values())
			subFlags.add(subFlag.toString().toLowerCase());
		return subFlags;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
}
