package com.wynprice.cloak.client.rendering;

import com.wynprice.cloak.client.rendering.models.CloakedModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CloakedRenderingFactory 
{
	public CloakedModel createModel(World world, BlockPos pos, IBlockState modelState, IBlockState renderState);
}
