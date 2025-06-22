package com.hbm.dim;

import java.util.ArrayList;
import java.util.List;

import com.hbm.config.SpaceConfig;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class BiomeGenBaseCelestial extends BiomeGenBase {

	protected ArrayList<SpawnListEntry> creatures = new ArrayList<SpawnListEntry>();
	protected ArrayList<SpawnListEntry> monsters = new ArrayList<SpawnListEntry>();
	protected ArrayList<SpawnListEntry> waterCreatures = new ArrayList<SpawnListEntry>();
	protected ArrayList<SpawnListEntry> caveCreatures = new ArrayList<SpawnListEntry>();

	public BiomeGenBaseCelestial(int id) {
		super(checkId(id));
	}

	// Tricking Java into letting us do shit before super is called
	private static int checkId(int id) {
		if(!SpaceConfig.crashOnBiomeConflict) return id;

		// If we go outside the bounds, don't crash here, it'll crash elsewhere already, with a more useful message
		if(id < biomeList.length && biomeList[id] != null && !(biomeList[id] instanceof BiomeGenBaseCelestial))
			throw new BiomeCollisionException(biomeList[id]);

		return id;
	}

	// Returns a copy of the lists to prevent them being modified
	@SuppressWarnings("rawtypes")
	@Override
	public List getSpawnableList(EnumCreatureType type) {
		switch(type) {
		case monster: return (List)monsters.clone();
		case creature: return (List)creatures.clone();
		case waterCreature: return (List)waterCreatures.clone();
		case ambient: return (List)caveCreatures.clone();
		default: return new ArrayList<SpawnListEntry>();
		}
	}

}
