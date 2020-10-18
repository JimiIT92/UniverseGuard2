package com.universeguard.event.flags;

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

public class FlagPistonsListener {

    @Listener
    public void onPistons(ChangeBlockEvent.Pre event, @Root LocatableBlock block) {
        if(isPiston(block.getBlockState().getType()) ||
                block.get(Keys.EXTENDED).isPresent()) {
            Player player = event.getCause().first(Player.class).orElse(null);
            event.setCancelled(event.getLocations().stream().anyMatch(location -> this.handleEvent(event, location, player)));
        }
    }

    private boolean isPiston(BlockType type) {
        return type.equals(BlockTypes.PISTON) ||
                type.equals(BlockTypes.PISTON_EXTENSION)||
                type.equals(BlockTypes.PISTON_HEAD) ||
                type.equals(BlockTypes.STICKY_PISTON);
    }

    private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
        return RegionUtils.handleEvent(event, EnumRegionFlag.PISTONS, location, player, RegionEventType.LOCAL);
    }
}
