package com.universeguard.event.flags;

import com.universeguard.region.Region;
import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlagPistonsListener {

    @Listener
    public void onPistons(ChangeBlockEvent.Pre event, @Root LocatableBlock block) {
        if(isPiston(block.getBlockState().getType()) ||
                block.get(Keys.EXTENDED).isPresent()) {
            Player player = event.getCause().first(Player.class).orElse(null);

            List<Location<World>> locations = event.getLocations();
            Location<World> eventLocation = locations.stream().findFirst().orElse(null);
            boolean cantUsePistons = this.handleEvent(event, eventLocation, player);
            List<Region> regions = locations.stream().map(RegionUtils::getRegion).distinct().collect(Collectors.toList());
            if(regions.size() == 1) {
                event.setCancelled(cantUsePistons);
            } else {
                Region eventRegion = RegionUtils.getRegion(eventLocation);
                if(eventRegion != null) {
                    boolean cantPushPistons = locations.stream().filter(l -> !Objects.equals(RegionUtils.getRegion(l), eventRegion)).anyMatch(l -> RegionUtils.handleEvent(event, EnumRegionFlag.PLACE, l, player, RegionEventType.LOCAL));
                    boolean cantRetractPistons = locations.stream().filter(l -> !Objects.equals(RegionUtils.getRegion(l), eventRegion)).anyMatch(l -> RegionUtils.handleEvent(event, EnumRegionFlag.DESTROY, l, player, RegionEventType.LOCAL));
                    event.setCancelled(cantUsePistons || cantPushPistons || cantRetractPistons);
                }
            }
        }
    }

    public static boolean isPiston(BlockType type) {
        return type.equals(BlockTypes.PISTON) ||
                isPistonExtension(type) ||
                type.equals(BlockTypes.STICKY_PISTON);
    }

    public static boolean isPistonExtension(BlockType type) {
        return type.equals(BlockTypes.PISTON_EXTENSION)||
                type.equals(BlockTypes.PISTON_HEAD);
    }

    private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
        return RegionUtils.handleEvent(event, EnumRegionFlag.PISTONS, location, player, RegionEventType.LOCAL);
    }
}
