package com.wynprice.brl.addons.tblloader;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TBLBakedModel implements IBakedModel
{
	
	private final List<BakedQuad> quads;
	
	private final TextureAtlasSprite sprite;
	
	public TBLBakedModel(TextureAtlasSprite sprite, List<BakedQuad> quads) 
	{
		this.quads = quads;
		this.sprite = sprite;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return side == null ? quads : Lists.newArrayList();
	}

	@Override
	public boolean isAmbientOcclusion() 
	{
		return true;
	}

	@Override
	public boolean isGui3d() 
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer(){
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture(){
		return sprite == null ? Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.STONE.getDefaultState()) : sprite;
	}

	@Override
	public ItemOverrideList getOverrides(){
		return ItemOverrideList.NONE;
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() 
	{
		return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.STONE), null, null).getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		return Pair.of(this,  Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.STONE), null, null).handlePerspective(cameraTransformType).getRight());
	}

}
