package com.universeguard.region.components;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.item.ItemType;

import java.util.Optional;

public class RegionValue {
    private String ITEM;
    private int QUANTITY;

    public RegionValue(ItemType item, int quantity) {
        this.ITEM = item.getId();
        this.QUANTITY = quantity;
    }

    public void setItem(ItemType item) {
        this.ITEM = item.getId();
    }

    public ItemType getItem() {
        Optional<ItemType> item = Sponge.getGame().getRegistry().getType(ItemType.class, this.ITEM);
        return item.isPresent() ? item.get() : null;
    }

    public void setQuantity(int quantity) {
        this.QUANTITY = quantity;
    }

    public int getQuantity() {
        return this.QUANTITY;
    }
}
