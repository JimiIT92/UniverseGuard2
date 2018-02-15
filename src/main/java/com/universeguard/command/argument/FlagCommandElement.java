package com.universeguard.command.argument;

import java.util.Arrays;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.universeguard.region.enums.EnumRegionSubflag;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class FlagCommandElement extends PatternMatchingCommandElement{

	private EnumRegionSubflag subFlag;
	public FlagCommandElement(Text key, EnumRegionSubflag subFlag) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		//Based on the subFlag variable will return a different list.
		//I don't know if this can be done, but is worth a try :) 
		return Arrays.asList("flag", "interact", "vehicleplace", 
				"vehicledestroy", "explosiondamage", "explosiondestroy", 
				"mobspawn", "mobdamage", "mobpve", "mobdrop");
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
	


}
