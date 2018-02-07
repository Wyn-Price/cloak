package com.wynprice.cloak.client.rendering.tjr;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TJRBakedModel implements IBakedModel
{
	
	private final List<BakedQuad> quads;
	
	public TJRBakedModel(List<BakedQuad> quads) 
	{
		this.quads = quads;
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
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.STONE.getDefaultState());
	}

	@Override
	public ItemOverrideList getOverrides(){
		return ItemOverrideList.NONE;
	}

}
