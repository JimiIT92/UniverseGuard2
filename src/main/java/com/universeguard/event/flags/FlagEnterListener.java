package com.universeguard.event.flags;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;

public class FlagEnterListener {

	@Listener
	public void onEnter(MoveEntityEvent event) {
		
		if(event.getTargetEntity() instanceof Player) {
			this.handleEvent(event, (Player)event.getTargetEntity());
		} else if(!event.getTargetEntity().getPassengers().isEmpty()) {
			for(Entity passenger : event.getTargetEntity().getPassengers()) {
				if(passenger instanceof Player) {
					this.handleEvent(event, (Player)passenger);
				}
			}
		}
	}
	
	private boolean handleEvent(MoveEntityEvent event, Player player) {
		Location<World> to = event.getToTransform().getLocation();
		Region region = RegionUtils.getRegion(to);
		if(region != null && region.isLocal() && !region.getFlag(EnumRegionFlag.ENTER)) {
			Location<World> from = event.getFromTransform().getLocation();
			if(to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ()) {
				Region destination = RegionUtils.getRegion(from);
				if(destination != null && !region.getName().equalsIgnoreCase(destination.getName())) {
					return RegionUtils.handleEvent(event, EnumRegionFlag.ENTER, to, player, RegionEventType.GLOBAL);
				}	
			}
		}
		
		return true;
	}
}
