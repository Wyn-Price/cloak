package com.wynprice.cloak.client.rendering.models;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public abstract class BaseModelProxy implements IBakedModel
{
	
	protected final IBakedModel oldModel;
	
	public BaseModelProxy(IBakedModel oldModel) 
	{
			this.oldModel = oldModel;
	}
	
	@Override
	public boolean isAmbientOcclusion() 
	{
		return oldModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() 
	{
		return oldModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return oldModel.getParticleTexture();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return oldModel.getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		return Pair.of(this, oldModel.handlePerspective(cameraTransformType).getRight());
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return oldModel.getQuads(state, side, rand);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return oldModel.getOverrides();
	}
}
