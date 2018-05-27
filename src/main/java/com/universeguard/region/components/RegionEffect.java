package com.universeguard.region.components;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.item.ItemType;

import java.util.Optional;

public class RegionEffect {
    private String EFFECT;
    private int LEVEL;

    public RegionEffect(PotionEffectType effect, int level) {
        this.EFFECT = effect.getId();
        this.LEVEL = level;
    }

    public void setEffect(PotionEffectType effect) {
        this.EFFECT = effect.getId();
    }

    public PotionEffectType getEffect() {
        Optional<PotionEffectType> effect = Sponge.getGame().getRegistry().getType(PotionEffectType.class, this.EFFECT);
        return effect.isPresent() ? effect.get() : null;
    }

    public void setLevel(int level) {
        this.LEVEL = level;
    }

    public int getLevel() {
        return this.LEVEL;
    }
}
