package com.hbm.world.biome;

import com.hbm.config.SpaceConfig;
import com.hbm.config.WorldConfig;
import com.hbm.dim.BiomeCollisionException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class BiomeGenCraterBase extends BiomeGenBase {

	public static final BiomeGenBase craterBiome = new BiomeGenCrater(WorldConfig.craterBiomeId).setDisableRain().setBiomeName("Crater");
	public static final BiomeGenBase craterInnerBiome = new BiomeGenCraterInner(WorldConfig.craterBiomeInnerId).setDisableRain().setBiomeName("Inner Crater");
	public static final BiomeGenBase craterOuterBiome = new BiomeGenCraterOuter(WorldConfig.craterBiomeOuterId).setDisableRain().setBiomeName("Outer Crater");

	public static void initDictionary() {
		BiomeDictionary.registerBiomeType(craterBiome,		DRY,	DEAD,	WASTELAND);
		BiomeDictionary.registerBiomeType(craterInnerBiome,	DRY,	DEAD,	WASTELAND);
		BiomeDictionary.registerBiomeType(craterOuterBiome,	DRY,	DEAD,	WASTELAND);
	}

	@Override
	public int getWaterColorMultiplier() {
		return 0x505020; //0x50d030
	}

	public BiomeGenCraterBase(int id) {
		super(checkId(id));
		this.waterColorMultiplier = 0xE0FFAE; //swamp color
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
	}

    // Tricking Java into letting us do shit before super is called
    private static int checkId(int id) {
        if(!SpaceConfig.crashOnBiomeConflict) return id;

        // If we go outside the bounds, don't crash here, it'll crash elsewhere already, with a more useful message
        if(id < biomeList.length && biomeList[id] != null && !(biomeList[id] instanceof BiomeGenCraterBase))
            throw new BiomeCollisionException("Biome ID conflict, attempted to register NTM crater biome to ID " + id + " which is already in use. Please modify hbm.cfg to fix this error. Note that the maximum biome ID is 255, if you run out you MUST install EndlessIDs!");

        return id;
    }

	public static class BiomeGenCrater extends BiomeGenCraterBase {

		public BiomeGenCrater(int id) { super(id); }

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeGrassColor(int x, int y, int z) {
			double noise = plantNoise.func_151601_a((double) x * 0.225D, (double) z * 0.225D);
			return noise < -0.1D ? 0x606060 : 0x505050;
		}

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeFoliageColor(int x, int y, int z) { return 0x6A7039; }

		@Override @SideOnly(Side.CLIENT)
		public int getSkyColorByTemp(float temp) { return 0x525A52; }
	}

	public static class BiomeGenCraterOuter extends BiomeGenCraterBase {

		public BiomeGenCraterOuter(int id) { super(id); }

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeGrassColor(int x, int y, int z) {
			double noise = plantNoise.func_151601_a((double) x * 0.225D, (double) z * 0.225D);
			return noise < -0.1D ? 0x776F59 : 0x6F6752;
		}

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeFoliageColor(int x, int y, int z) { return 0x6A7039; }

		@Override @SideOnly(Side.CLIENT)
		public int getSkyColorByTemp(float temp) { return 0x6B9189; }
	}

	public static class BiomeGenCraterInner extends BiomeGenCraterBase {

		public BiomeGenCraterInner(int id) { super(id); }

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeGrassColor(int x, int y, int z) {
			double noise = plantNoise.func_151601_a((double) x * 0.225D, (double) z * 0.225D);
			return noise < -0.1D ? 0x404040 : 0x303030;
		}

		@Override @SideOnly(Side.CLIENT)
		public int getBiomeFoliageColor(int x, int y, int z) { return 0x6A7039; }

		@Override @SideOnly(Side.CLIENT)
		public int getSkyColorByTemp(float temp) { return 0x424A42; }
	}
}
