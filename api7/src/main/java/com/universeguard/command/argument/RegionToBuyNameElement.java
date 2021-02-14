package com.universeguard.command.argument;

import com.universeguard.UniverseGuard;
import com.universeguard.region.LocalRegion;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class RegionToBuyNameElement extends PatternMatchingCommandElement{

	public RegionToBuyNameElement(Text key) {
		super(key);
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		ArrayList<String> regions = new ArrayList<>();
		for (Region region : UniverseGuard.ALL_REGIONS) {
			if(region.isLocal()) {
                LocalRegion localRegion = (LocalRegion)region;
                if(!localRegion.getSold() && localRegion.getValue() != null)
                    regions.add(localRegion.getName());
            }
		}
		
		return regions;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
}