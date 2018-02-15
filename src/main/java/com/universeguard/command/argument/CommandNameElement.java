package com.universeguard.command.argument;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of command names for code completion
 */
public class CommandNameElement extends PatternMatchingCommandElement{

	public CommandNameElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		// Note, on servers with a lot of plugins, this could be a pretty
		// huge list.  Not sure if we want to truncate?
		return Sponge.getCommandManager().getPrimaryAliases();
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}

}
