/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.region.components;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

/**
 * Region Location Class
 * @author Jimi
 *
 */
public class RegionLocation {
	private int x;
	private int y;
	private int z;
	private String dimension;
	private String world;
	
	public RegionLocation(int x, int y, int z, String dimension, String world) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setDimension(dimension);
		this.setWorld(world);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}
	
	@Override
	public String toString() {
		return this.x + " " + this.y + " " + this.z;
	}
	
	public Vector3d toVector() {
		return new Vector3d(x, y, z);
	}
	
	public World getServerWorld() {
		return Sponge.getServer().getWorld(world).isPresent() ? Sponge.getServer().getWorld(world).get() : null;
	}
	
	public Location<World> getLocation() {
		World world = this.getServerWorld();
		return world != null ? new Location<World>(world, this.toVector()) : null;
	}
}
