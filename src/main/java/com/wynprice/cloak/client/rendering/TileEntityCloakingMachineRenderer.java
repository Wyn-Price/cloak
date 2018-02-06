package com.wynprice.cloak.client.rendering;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.CloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.tjr.TJR;
import com.wynprice.cloak.client.rendering.tjr.TJRModel;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.MaterialLibrary;

public class TileEntityCloakingMachineRenderer extends BaseCloakingModelRenderer<TileEntityCloakingMachine>
{
	
	public static final TJRModel MODEL = TJR.getModel(new ResourceLocation(CloakMod.MODID, "models/block/cloaking_machine_render.json"));
	
	public TileEntityCloakingMachineRenderer() 
	{
		super(new CloakedRenderingFactory() {
		
			@Override
			public CloakedModel createModel(World world, BlockPos pos, IBlockState modelState, IBlockState renderState) 
			{
				return new CloakingMachineModel(modelState, renderState);
			}
		});
	}	
	
	@Override
	public void render(TileEntityCloakingMachine te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		if(destroyStage == -1)
		{
			GlStateManager.translate(x, y, z);
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/blocks/cloaking_machine_model.png"));
			MODEL.render(1f);
			GlStateManager.translate(-x, -y, -z);
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
	}
}
