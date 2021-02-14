package com.universeguard.region.components;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author JimiIT92
 */
public class RegionSell {
    private UUID owner;
    private UUID regionId;
    private RegionValue value;
    private boolean bought;

    public RegionSell(UUID owner, UUID regionId, RegionValue value) {
        this.owner = owner;
        this.regionId = regionId;
        this.value = value;
        this.bought = false;
    }


    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public RegionValue getValue() {
        return value;
    }

    public void setValue(RegionValue value) {
        this.value = value;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
