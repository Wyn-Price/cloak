package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.CloakBlockItemModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CloakingMachineItemRender extends TileEntitySpecialRenderer
{
	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) 
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/blocks/cloaking_machine_model.png"));
		TileEntityCloakingMachineRenderer.MODEL.render(1f);
	}
	
	
	public static class FakeTileEntity extends TileEntity
	{
		
	}
}