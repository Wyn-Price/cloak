package com.wynprice.cloak.client.rendering;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

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

public class TileEntityCloakBlockRenderer extends TileEntitySpecialRenderer<TileEntityCloakBlock>
{
	
	public static IBlockState currentRender;
	public static World currentWorld;
	public static BlockPos currentPos;
	
	@Override
	public void render(TileEntityCloakBlock te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		GlStateManager.pushMatrix();
		
    	GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        EntityPlayer entityplayer = Minecraft.getMinecraft().player;
        double d0 = (entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks);
        double d1 = (entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks);
        double d2 = (entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks);
        Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        World world = getWorld();
        Tessellator.getInstance().getBuffer().noColor();
        Tessellator tessellator = Tessellator.getInstance();
        Block block = te.getWorld().getBlockState(te.getPos()).getBlock();
        currentRender = te.getWorld().getBlockState(te.getPos()).getActualState(te.getWorld(), te.getPos());
        currentPos = te.getPos();
        currentWorld = te.getWorld();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? 7425 : 7424);
        if(te.getState().getBlock() != Blocks.AIR)
        {
        	//RENDER
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
    	GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
}
