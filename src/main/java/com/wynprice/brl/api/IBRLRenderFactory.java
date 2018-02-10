package com.wynprice.brl.api;

import java.util.List;

import com.wynprice.brl.api.BRLRegistry.DefaultRenderFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBRLRenderFactory 
{
	/**
	 * A default factory that renders things the vanilla way
	 */
	public IBRLRenderFactory DEFAULT_FACTORY = new DefaultRenderFactory();

	
	/**
	 * Returns a list of all custom render info you want to be rendered.<br>
	 * Please not that the position may not hold the correct IBlockState/TileEntity
	 * @param access The world being rendered in
	 * @param pos The position in the world
	 * @param inState The IBlockState passed through {@link BlockRendererDispatcher#renderBlock(IBlockState, BlockPos, IBlockAccess, net.minecraft.client.renderer.BufferBuilder)}
	 */
	default public List<BRLRenderInfo> getModels(IBlockAccess access, BlockPos pos, IBlockState inState)
	{
		return DEFAULT_FACTORY.getModels(access, pos, inState);
	}
}
