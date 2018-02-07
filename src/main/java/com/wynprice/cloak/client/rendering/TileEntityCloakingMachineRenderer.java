package com.wynprice.cloak.client.rendering;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.CloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.tjr.TJR;
import com.wynprice.cloak.client.rendering.tjr.TJRModel;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//			MODEL.render(1f);
			GlStateManager.translate(-x, -y, -z);
		    GlStateManager.enableRescaleNormal();
		    RenderHelper.disableStandardItemLighting();
		    GlStateManager.enableTexture2D();
		    GlStateManager.enableAlpha();
	        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	        GlStateManager.enableBlend();
	        GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? 7425 : 7424);
	        EntityPlayer entityplayer = Minecraft.getMinecraft().player;
	        double d0 = (entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks);
	        double d1 = (entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks);
	        double d2 = (entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks);
	        Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(getWorld(), 
					TJR.getModel(new ResourceLocation(CloakMod.MODID, "models/block/cloaking_machine_render.json")).getBakedModel(),
					Blocks.STONE.getDefaultState(), te.getPos(), Tessellator.getInstance().getBuffer(), false);
			Tessellator.getInstance().draw();

			Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
	        GlStateManager.popMatrix();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
	}
}
