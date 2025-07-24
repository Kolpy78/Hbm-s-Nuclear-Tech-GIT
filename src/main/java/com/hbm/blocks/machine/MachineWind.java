package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.machine.TileEntityMachineWindTurbine;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MachineWind extends BlockDummyable {

	public MachineWind(Material mat) {
		super(mat);
	}

	@Override
  	public int[] getDimensions() {
		return new int[6];
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityMachineWindTurbine();
	}
}
