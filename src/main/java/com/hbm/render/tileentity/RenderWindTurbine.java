package com.hbm.render.tileentity;

import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
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
		return new ItemRenderBase() {
			public void renderInventory() {
				GL11.glTranslated(0, -3, 0);
				GL11.glScaled(2.75, 2.75, 2.75);
			}
			public void renderCommon() {
				GL11.glScaled(1, 1, 1);
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				bindTexture(ResourceManager.wind_turbine_body_txt);
				bindTexture(ResourceManager.wind_turbine_generator_txt);
				bindTexture(ResourceManager.wind_turbine_blades_txt);
				ResourceManager.solarp.renderAll();
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}};

	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float interp) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y, z + 0.5D);
		GL11.glEnable(GL11.GL_LIGHTING);
		bindTexture(ResourceManager.wind_turbine_body_txt);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
		ResourceManager.wind_turbine.renderPart("Cube");
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		bindTexture(ResourceManager.wind_turbine_generator_txt);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
		ResourceManager.wind_turbine.renderPart("Generator");
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		bindTexture(ResourceManager.wind_turbine_blades_txt);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
		ResourceManager.wind_turbine.renderPart("Blades");
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
	}
}
