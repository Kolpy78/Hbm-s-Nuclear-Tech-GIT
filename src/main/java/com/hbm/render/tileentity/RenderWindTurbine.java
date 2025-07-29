package com.hbm.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.tileentity.machine.TileEntityMachineWindTurbine;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderWindTurbine extends TileEntitySpecialRenderer implements IItemRendererProvider {

	@Override
	public Item getItemForRenderer() {
		return Item.getItemFromBlock(ModBlocks.machine_wind);
	}

	@Override
	public IItemRenderer getRenderer() {
		//FIXME item renderer bad scaled
		return new ItemRenderBase() {
			public void renderInventory() {
				GL11.glTranslated(0, -3, 0);
				GL11.glScaled(1.5, 1.5, 1.5);
			}
			public void renderCommon() {
				GL11.glScaled(0.55, 0.55, 0.55);
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				bindTexture(ResourceManager.wind_turbine_txt);
				ResourceManager.wind_turbine.renderAll();
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}};

	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float interp) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 0.25, z + 0.5D);
		switch(te.getBlockMetadata() - BlockDummyable.offset) {
			case 2: GL11.glRotatef(0, 0F, 1F, 0F); break;
			case 4: GL11.glRotatef(90, 0F, 1F, 0F); break;
			case 3: GL11.glRotatef(180, 0F, 1F, 0F); break;
			case 5: GL11.glRotatef(270, 0F, 1F, 0F); break;
		}
		GL11.glEnable(GL11.GL_LIGHTING);

		bindTexture(ResourceManager.wind_turbine_txt);
		ResourceManager.wind_turbine.renderPart("Body");
		ResourceManager.wind_turbine.renderPart("Generator");
		GL11.glPopMatrix();

		if (te instanceof TileEntityMachineWindTurbine) {
			TileEntityMachineWindTurbine windTurbine = (TileEntityMachineWindTurbine)te;
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
			float rot = windTurbine.prevSpin + (windTurbine.spin - windTurbine.prevSpin) * interp;
			switch(te.getBlockMetadata() - BlockDummyable.offset) {
				case 2:
				case 3: {GL11.glTranslated(x + 0.5D, y + 28.0D, z + 0.5D);
					GL11.glRotatef(rot, 0, 0, 1);
					GL11.glTranslated(0, -28.0D, 0);
					break;}
				case 4:
				case 5: {
					GL11.glTranslated(x + 0.5D, y + 28.0D, z + 0.5D);
					GL11.glRotatef(rot, 1, 0, 0);
					GL11.glTranslated(0, -28.0D, 0);
					break;
				}
			}
			switch(te.getBlockMetadata() - BlockDummyable.offset) {
				case 2: GL11.glRotatef(0, 0F, 1F, 0F); break;
				case 4: GL11.glRotatef(90, 0F, 1F, 0F); break;
				case 3: GL11.glRotatef(180, 0F, 1F, 0F); break;
				case 5: GL11.glRotatef(270, 0F, 1F, 0F); break;
			}
			bindTexture(ResourceManager.wind_turbine_txt);
			ResourceManager.wind_turbine.renderPart("Pivot");
			ResourceManager.wind_turbine.renderPart("Blade");
			ResourceManager.wind_turbine.renderPart("Blade2");
			ResourceManager.wind_turbine.renderPart("Blade3");
			GL11.glPopMatrix();
		}else {
			FMLLog.severe("==CRITICAL ERROR DO NOT IGNORE== hey there! i'm a Class cast check and i just saved you from an awkward moment! the error and it's information is as follows:");
			FMLLog.severe("The render expected a TileEntityMachineWindTurbine, but received a " + te.getClass().getSimpleName() + " instead. Something is wrong. Be a lamb and report this to the mod author(s).");
			new Exception().printStackTrace();
		}
	}
}
