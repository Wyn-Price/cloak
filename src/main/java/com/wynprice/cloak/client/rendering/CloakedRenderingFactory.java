package com.wynprice.cloak.client.rendering;

import com.wynprice.cloak.client.rendering.models.CloakedModel;

import net.minecraft.block.state.IBlockState;

public interface CloakedRenderingFactory 
{
	public CloakedModel createModel(IBlockState modelState, IBlockState renderState);
}
