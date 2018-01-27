package com.wynprice.cloak.client.rendering;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCloakingMachineRenderer extends TileEntitySpecialRenderer<TileEntityCloakingMachine>
{
	
	public static IBlockState currentRender;
	public static World currentWorld;
	public static BlockPos currentPos;
	
	@Override
	public void render(TileEntityCloakingMachine te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableCull();
	    GlStateManager.enableRescaleNormal();
	    RenderHelper.disableStandardItemLighting();
	    Minecraft.getMinecraft().entityRenderer.disableLightmap();
	    GlStateManager.enableTexture2D();
	    GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
	    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GlStateManager.depthMask(false);
        EntityPlayer entityplayer = Minecraft.getMinecraft().player;
        double d0 = (entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks);
        double d1 = (entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks);
        double d2 = (entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks);
        Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);;
        World world = getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        Block block = te.getWorld().getBlockState(te.getPos()).getBlock();
        currentRender = te.getWorld().getBlockState(te.getPos()).getActualState(te.getWorld(), te.getPos());
        currentPos = te.getPos();
        currentWorld = te.getWorld();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    	IBlockState renderState = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
    	IBlockState modelState = Blocks.SANDSTONE_STAIRS.getDefaultState();
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, new BasicCloakingMachineModel(modelState, renderState), currentRender, te.getPos(), tessellator.getBuffer(), false);
        tessellator.draw();
        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
    	GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	
	@Override
	public boolean isGlobalRenderer(TileEntityCloakingMachine te) 
	{
		return true;
	}
}
