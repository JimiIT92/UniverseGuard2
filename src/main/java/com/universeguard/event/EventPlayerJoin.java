package com.universeguard.event;

import com.universeguard.region.components.RegionSell;
import com.universeguard.utils.InventoryUtils;
import com.universeguard.utils.RegionUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

/**
 * @author JimiIT92
 */
public class EventPlayerJoin {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        RegionSell sell = RegionUtils.getSellingRegionForPlayer(event.getTargetEntity().getUniqueId());
        if(sell != null) {
            RegionUtils.removeSellingRegion(sell.getRegionId());
        }
    }
}
