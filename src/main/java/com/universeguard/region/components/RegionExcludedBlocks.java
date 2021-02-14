/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.effect.potion.PotionEffectType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Region Command Class
 * @author Jimi
 *
 */
public class RegionExcludedBlocks {
	private ArrayList<String> PLACE;
    private ArrayList<String> DESTROY;

	public RegionExcludedBlocks() {
		this.PLACE = new ArrayList<String>();
		this.DESTROY = new ArrayList<String>();
	}

	public void setPlace(ArrayList<String> place) {
	    this.PLACE = place;
    }

    public ArrayList<String> getPlace() {
	    return this.PLACE;
    }

    public void setDestroy(ArrayList<String> destroy) {
	    this.DESTROY = destroy;
    }

    public ArrayList<String> getDestroy() {
	    return this.DESTROY;
    }

    public void addPlace(BlockType block) {
	    if(!this.PLACE.contains(block.getId()))
	        this.PLACE.add(block.getId());
    }

    public void removePlace(BlockType block) {
	    if(this.PLACE.contains(block.getId()))
	        this.PLACE.remove(block.getId());
    }

    public void addDestroy(BlockType block) {
        if(!this.DESTROY.contains(block.getId()))
            this.DESTROY.add(block.getId());
    }

    public void removeDestroy(BlockType block) {
        if(this.DESTROY.contains(block.getId()))
            this.DESTROY.remove(block.getId());
    }
}
