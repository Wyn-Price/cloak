package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.CloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.SingleQuadModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.BasicCloakedModelTileEntity;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;
import com.wynprice.cloak.common.world.CloakLightAccess;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCloakingMachineRenderer extends TileEntitySpecialRenderer<TileEntityCloakingMachine>
{
	
	@Override
	public void render(TileEntityCloakingMachine te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		if(te.getHandler().getStackInSlot(0).isEmpty() || te.getHandler().getStackInSlot(1).isEmpty()) return;
		if(destroyStage > -1)
		{
			renderBreakingProgress(getWorld(), te.getPos(), te, Minecraft.getMinecraft().player, partialTicks, destroyStage);
			return;
		}
		GlStateManager.pushMatrix();
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
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);;
        World world = getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        Block block = te.getWorld().getBlockState(te.getPos()).getBlock();
    	IBlockState renderState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info"));
    	IBlockState modelState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info"));
    	CloakedModel model = new CloakingMachineModel(modelState, renderState);
    	
    	HashMap<Integer, IBlockState> overrideList = new HashMap<>();
    	HashMap<Integer, ResourceLocation> externalOverrideList = new HashMap<>();
    	
		for(int i : te.getCurrentModificationList().keySet())
			if(te.getCurrentModificationList().get(i) != null && !te.getCurrentModificationList().get(i).isEmpty())
				if(te.getCurrentModificationList().get(i).getItem() != CloakItems.EXTERNAL_CARD)
					overrideList.put(i, NBTUtil.readBlockState(te.getCurrentModificationList().get(i).getSubCompound("capture_info")));
				else if(ExternalImageHandler.SYNCED_RESOURCE_MAP.containsKey(te.getCurrentModificationList().get(i).getSubCompound("capture_info").getString("external_image")))
					externalOverrideList.put(i, ExternalImageHandler.SYNCED_RESOURCE_MAP.get(te.getCurrentModificationList().get(i).getSubCompound("capture_info").getString("external_image")));

		model.getOverrideList().putAll(overrideList);
		model.setExternalOverrideList(externalOverrideList);
		model.setBaseTextureExternal(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")));
		
    	List<EnumFacing> facingList = new ArrayList<>();
    	ArrayList<BakedQuad> quadList = new ArrayList<>();
    	facingList.add(null);
        for (EnumFacing enumfacing : EnumFacing.values()) facingList.add(enumfacing);
    	for(EnumFacing face : facingList)
    		for(BakedQuad quad : model.getQuads(renderState, face, 0L))
    			quadList.add(quad);
    	for(BakedQuad quad : quadList)
	    	{
            	tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    			Minecraft.getMinecraft().renderEngine.bindTexture(quad instanceof ExternalBakedQuad ? ((ExternalBakedQuad)quad).getLocation() : TextureMap.LOCATION_BLOCKS_TEXTURE);
	    		IBlockState blockstate = model.getStateFromQuad(quad);
	    		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(new CloakLightAccess(world, te.getPos()), new SingleQuadModel(model, quad, quad.getFace()), blockstate, te.getPos(), tessellator.getBuffer(), false);
	            tessellator.draw();
	    	}
        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
    	GlStateManager.disableBlend();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		
		
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	
	@Override
	public boolean isGlobalRenderer(TileEntityCloakingMachine te) 
	{
		return true;
	}
	
	public void renderBreakingProgress(World world, BlockPos pos, TileEntityCloakingMachine te, Entity entityIn, float partialTicks, int destorystage)
    {
        double d3 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d4 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d5 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.doPolygonOffset(-3.0F, -3.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        
        BufferBuilder bufferBuilderIn = Tessellator.getInstance().getBuffer();
        bufferBuilderIn.begin(7, DefaultVertexFormats.BLOCK);
        bufferBuilderIn.setTranslation(-d3, -d4, -d5);
        bufferBuilderIn.noColor();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockDamage(NBTUtil.readBlockState(te.getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info")), pos, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/destroy_stage_" + destorystage), world);
        Tessellator.getInstance().draw();
        bufferBuilderIn.setTranslation(0.0D, 0.0D, 0.0D);
        
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
