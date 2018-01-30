package com.wynprice.cloak.client.rendering;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class SingleQuadModel extends BaseModelProxy
{
	
	private final BakedQuad quad;
	private final EnumFacing face;

	public SingleQuadModel(IBakedModel oldModel, BakedQuad quad, EnumFacing face) 
	{
		super(oldModel);
		this.quad = quad;
		this.face = face;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		if(side == face)
			return Lists.newArrayList(quad);
		return Lists.newArrayList();
	}

}
