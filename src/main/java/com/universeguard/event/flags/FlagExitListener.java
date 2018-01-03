package com.universeguard.event.flags;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

public class FlagExitListener {

	@Listener
	public void onExit(MoveEntityEvent event) {
		Region region = RegionUtils.getRegion(event.getFromTransform().getLocation());
		Region destination = RegionUtils.getRegion(event.getToTransform().getLocation());
		if(region != null && destination != null && region.isLocal() && !region.getName().equalsIgnoreCase(destination.getName())) {
			this.handleEvent(event, event.getFromTransform().getLocation(), event.getTargetEntity() instanceof Player ? (Player)event.getTargetEntity() : null);
		}
	}
	
	private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
		return RegionUtils.handleEvent(event, EnumRegionFlag.EXIT, location, player, RegionEventType.GLOBAL);
	}
}
