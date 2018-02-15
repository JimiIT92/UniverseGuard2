package com.universeguard.command.argument;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;

import com.universeguard.region.enums.EnumRegionExplosion;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.EnumRegionInteract;
import com.universeguard.region.enums.EnumRegionSubflag;
import com.universeguard.region.enums.EnumRegionVehicle;
import com.universeguard.utils.LogUtils;

/**
 * This class is used by the Sponge CommandSpec 
 * to build a list of region names for code completion
 */

public class FlagCommandElement extends PatternMatchingCommandElement{

	private EnumRegionSubflag subFlag;
	public FlagCommandElement(Text key) {
		super(key);
		this.subFlag = EnumRegionSubflag.MOBPVE;
	}

	@Override
	protected Iterable<String> getChoices(CommandSource source) {
		LogUtils.print(source.getActiveContexts().toString());
		//Based on the subFlag variable will return a different list.
		//I don't know if this can be done, but is worth a try :) 
		ArrayList<String> flags = new ArrayList<String>();
		if(this.subFlag != null) {
			switch(subFlag) {
			case FLAG:
				for(EnumRegionFlag flag : EnumRegionFlag.values())
					flags.add(flag.getName().toLowerCase());
				break;
			case INTERACT:
				for(EnumRegionInteract interact : EnumRegionInteract.values())
					flags.add(interact.getName().toLowerCase());
				break;
			case EXPLOSIONDAMAGE:
			case EXPLOSIONDESTROY:
				for(EnumRegionExplosion explosion : EnumRegionExplosion.values())
					flags.add(explosion.getName().toLowerCase());
				break;
			case VEHICLEPLACE:
			case VEHICLEDESTROY:
				for(EnumRegionVehicle vehicle : EnumRegionVehicle.values())
					flags.add(vehicle.getName().toLowerCase());
				break;
			case MOBSPAWN:
			case MOBPVE:
			case MOBDAMAGE:
			case MOBDROP:
					flags.addAll(Sponge.getRegistry().getAllOf(EntityType.class).stream()
						    .map(CatalogType::getId).collect(Collectors.toList()));
					flags.add("all");
			default: break;
			}
		}
		
		return flags;
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		return choice;
	}
	


}
