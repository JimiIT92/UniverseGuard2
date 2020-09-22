package com.universeguard.event.flags;

import com.universeguard.region.enums.EnumRegionFlag;
import com.universeguard.region.enums.RegionEventType;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class FlagPistonsListener {

    @Listener
    public void onPiston(ChangeBlockEvent.Pre event, @Root LocatableBlock block, @First Player player) {
        if(block.getBlockState().getType().equals(BlockTypes.PISTON) ||
                block.getBlockState().getType().equals(BlockTypes.PISTON_EXTENSION)||
                block.getBlockState().getType().equals(BlockTypes.PISTON_HEAD) ||
                block.getBlockState().getType().equals(BlockTypes.STICKY_PISTON) ||
                block.get(Keys.EXTENDED).isPresent()) {
            this.handleEvent(event, block.getLocation(), player);
        }
    }

    private boolean handleEvent(Cancellable event, Location<World> location, Player player) {
        return RegionUtils.handleEvent(event, EnumRegionFlag.PISTONS, location, player, RegionEventType.LOCAL);
    }
}
