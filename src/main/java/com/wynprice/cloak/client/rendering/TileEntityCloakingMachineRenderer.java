package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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
		if(te.getHandler().getStackInSlot(0).isEmpty() || te.getHandler().getStackInSlot(1).isEmpty()) return;
		GlStateManager.pushMatrix();
		GlStateManager.enableCull();
	    GlStateManager.enableRescaleNormal();
	    RenderHelper.disableStandardItemLighting();
	    Minecraft.getMinecraft().entityRenderer.disableLightmap();
	    GlStateManager.enableTexture2D();
	    GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
	    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
        NBTTagCompound renderStackNBT = te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info");
        NBTTagCompound modelStackNBT = te.getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info");
    	IBlockState renderState = Block.REGISTRY.getObject(new ResourceLocation(renderStackNBT.getString("block"))).getStateFromMeta(renderStackNBT.getInteger("meta"));
    	IBlockState modelState = Block.REGISTRY.getObject(new ResourceLocation(modelStackNBT.getString("block"))).getStateFromMeta(modelStackNBT.getInteger("meta"));
    	BasicCloakingMachineModel model = new BasicCloakingMachineModel(modelState, renderState);
    	
    	HashMap<Integer, IBlockState> overrideList = new HashMap<>();
		for(int i : te.getCurrentModificationList().keySet())
		{
			if(te.getCurrentModificationList().get(i) != null && !te.getCurrentModificationList().get(i).isEmpty())
			{
				NBTTagCompound stackNBT = te.getCurrentModificationList().get(i).getSubCompound("capture_info");
				IBlockState stackState = Block.REGISTRY.getObject(new ResourceLocation(stackNBT.getString("block"))).getStateFromMeta(stackNBT.getInteger("meta"));
				overrideList.put(i, stackState);
			}
		}
    	model.getOverrideList().putAll(overrideList);

    	List<EnumFacing> facingList = new ArrayList<>();
    	facingList.add(null);
        for (EnumFacing enumfacing : EnumFacing.values()) facingList.add(enumfacing);
    	for(EnumFacing face : facingList)
    		for(BakedQuad quad : model.getQuads(renderState, face, 0L))
	    	{
	    		IBlockState blockstate = model.getStateFromQuad(quad);
	    		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, new SingleQuadModel(model, quad, face), blockstate, te.getPos(), tessellator.getBuffer(), false);
	    	}
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
