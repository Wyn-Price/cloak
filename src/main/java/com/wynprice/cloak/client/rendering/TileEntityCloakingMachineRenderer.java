package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.SingleQuadModel;
import com.wynprice.cloak.common.tileentity.BasicCloakedModelTileEntity;
import com.wynprice.cloak.common.world.CloakLightAccess;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityCloakingMachineRenderer<T extends BasicCloakedModelTileEntity> extends TileEntitySpecialRenderer<T>
{
	
	private final CloakedRenderingFactory factory;
	
	public TileEntityCloakingMachineRenderer(CloakedRenderingFactory factory) 
	{
		this.factory = factory;
	}
	
	
	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) 
	{
		if(te.getHandler().getStackInSlot(0).isEmpty() || te.getHandler().getStackInSlot(1).isEmpty()) return;
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
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    	IBlockState renderState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info"));
    	IBlockState modelState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info"));
    	
    	CloakedModel model = factory.createModel(world, te.getPos(), modelState, renderState);
    	
    	HashMap<Integer, IBlockState> overrideList = new HashMap<>();
		for(int i : te.getCurrentModificationList().keySet())
			if(te.getCurrentModificationList().get(i) != null && !te.getCurrentModificationList().get(i).isEmpty())
				overrideList.put(i, NBTUtil.readBlockState(te.getCurrentModificationList().get(i).getSubCompound("capture_info")));

		model.getOverrideList().putAll(overrideList);

    	List<EnumFacing> facingList = new ArrayList<>();
    	facingList.add(null);
        for (EnumFacing enumfacing : EnumFacing.values()) facingList.add(enumfacing);
    	for(EnumFacing face : facingList)
    		for(BakedQuad quad : model.getQuads(renderState, face, 0L))
	    	{
	    		IBlockState blockstate = model.getStateFromQuad(quad);
	    		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(new CloakLightAccess(world, te.getPos()), new SingleQuadModel(model, quad, face), blockstate, te.getPos(), tessellator.getBuffer(), false);
	    	}
        tessellator.draw();
        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
    	GlStateManager.disableBlend();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	@Override
	public boolean isGlobalRenderer(BasicCloakedModelTileEntity te) 
	{
		return true;
	}
}
