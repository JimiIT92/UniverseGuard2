package com.universeguard.command.argument;

import java.util.Arrays;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class BooleanElement extends PatternMatchingCommandElement{

	public BooleanElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		return Arrays.asList("true", "false");
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
	


}
