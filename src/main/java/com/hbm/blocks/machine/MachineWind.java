package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ILookOverlay;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineWindTurbine;
import com.hbm.util.BobMathUtil;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineWind extends BlockDummyable implements ILookOverlay {

	public MachineWind(Material mat) {
		super(mat);
	}

	@Override
  	public int[] getDimensions() {
		return new int[] {28, 0, 0, 0, 0, 0}; //The last number sets height, the others idk
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if (meta >= 12){
			return new TileEntityMachineWindTurbine();
		}
		if (meta >= extra){
			return new TileEntityProxyCombo(false, false, true);
		}
		return null;
	}

	@Override
	public void printHook(RenderGameOverlayEvent.Pre event, World world, int x, int y, int z) {

		int[] pos = this.findCore(world, x, y, z);

		if(pos == null)
			return;

		TileEntity te = world.getTileEntity(pos[0], pos[1], pos[2]);

		if(!(te instanceof TileEntityMachineWindTurbine)) return;

		List<String> text = new ArrayList();
		TileEntityMachineWindTurbine wind = (TileEntityMachineWindTurbine) te;
		switch (wind.isAbleToProvidePower()){
			case VACUUM: {
				text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! VACUUM ! ! !");
				text.add(EnumChatFormatting.RED + "Inactive");
				break;
			}
			case TOO_HIGH: {
				text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! ALTITUDE ! ! !");
				text.add(EnumChatFormatting.RED + "Inactive");
				break;
			}
			case SKY_BLOCKED: {
				text.add("&[" + (BobMathUtil.getBlink() ? 0xff0000 : 0xffff00) + "&]! ! ! TURBINE BLOCKED ! ! !");
				text.add(EnumChatFormatting.RED + "Inactive");
				break;
			}
			default: text.add(EnumChatFormatting.GREEN + "Active");

		}
		ILookOverlay.printGeneric(event, I18nUtil.resolveKey(getUnlocalizedName() + ".name"), 0xffff00, 0x404000, text);
	}
}
