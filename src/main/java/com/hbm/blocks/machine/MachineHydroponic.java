package com.hbm.blocks.machine;

import java.util.ArrayList;
import java.util.List;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityHydroponic;
import com.hbm.util.I18nUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineHydroponic extends BlockDummyable implements ILookOverlay {

	public MachineHydroponic(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityHydroponic();
		if(meta >= 6) return new TileEntityProxyCombo(true, false, true);
		return null;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {2, 0, 0, 0, 2, 2};
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		x += dir.offsetX * o;
		z += dir.offsetZ * o;

		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

		// Base
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 0, 0, 0, 2, 2}, this, dir);

		// Top
		MultiblockHandlerXR.fillSpace(world, x + rot.offsetX * 2, y + 2, z + rot.offsetZ * 2, new int[] {0, 0, 0, 0, 0, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x - rot.offsetX * 2, y + 2, z - rot.offsetZ * 2, new int[] {0, 0, 0, 0, 3, 0}, this, dir);

		// Sides
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, 0, 0, 0, -2, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, 0, 0, 0, 2, -2}, this, dir);

		makeExtra(world, x + rot.offsetX * 2, y, z + rot.offsetZ * 2);
		makeExtra(world, x - rot.offsetX * 2, y, z - rot.offsetZ * 2);
	}

	// Only the blocks immediately horizontally adjacent to the core can actually sustain plants, so we do a 4 block search in all cardinals
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
		if(world.getBlockMetadata(x, y, z) >= 12
		|| world.getBlockMetadata(x + 1, y, z) >= 12
		|| world.getBlockMetadata(x - 1, y, z) >= 12
		|| world.getBlockMetadata(x, y, z + 1) >= 12
		|| world.getBlockMetadata(x, y, z - 1) >= 12)
			return true;
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void printHook(Pre event, World world, int x, int y, int z) {

		int[] pos = this.findCore(world, x, y, z);

		if(pos == null)
			return;

		TileEntity te = world.getTileEntity(pos[0], pos[1], pos[2]);

		if(!(te instanceof TileEntityHydroponic))
			return;

		TileEntityHydroponic hydro = (TileEntityHydroponic) te;

		List<String> text = new ArrayList<String>();

		text.add(EnumChatFormatting.GREEN + "-> " + EnumChatFormatting.RESET + hydro.tanks[0].getTankType().getLocalizedName() + ": " + hydro.tanks[0].getFill() + "/" + hydro.tanks[0].getMaxFill() + "mB");
		text.add(EnumChatFormatting.RED + "<- " + EnumChatFormatting.RESET + hydro.tanks[1].getTankType().getLocalizedName() + ": " + hydro.tanks[1].getFill() + "/" + hydro.tanks[1].getMaxFill() + "mB");

		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}

	// TEMP
	@Override
	public int getRenderType() {
		return 0;
	}
	// TEMP

}
