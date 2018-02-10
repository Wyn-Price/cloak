package com.wynprice.brl.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.registries.IRegistryDelegate;

/**
 * The registry class of everything BLR related
 * @author Wyn Price
 *
 */
public class BRLRegistry 
{
	
	/**
	 * A map containing all the custom factories
	 */
	private static final Map<IRegistryDelegate<Block>, IBRLRenderFactory> FACTORY_MAP = Maps.newHashMap();
	
	/**
	 * Used to help solve errors
	 */
	private static final Map<Block, StackTraceElement[]> ERROR_FALLBACK_LIST = Maps.newHashMap();
	
	/**
	 * Get the factory for a block
	 * @return The custom factory, or {@link IBRLRenderFactory#DEFAULT_FACTORY} if there is not one
	 */
	public static IBRLRenderFactory getFactory(Block block)
	{
		return FACTORY_MAP.containsKey(block.delegate) ? FACTORY_MAP.get(block.delegate) : IBRLRenderFactory.DEFAULT_FACTORY;
	}

	/**
	 * Register an {@link IBRLRenderFactory} to a block
	 * @param block the block to register to 
	 * @param factory the factory to register 
	 * @throws IllegalArgumentException if the block already has been registered
	 */
	public static void registerFactory(Block block, IBRLRenderFactory factory)
	{
		if(ERROR_FALLBACK_LIST.containsKey(block)) //Means the blocks already been registered before. Throw an error
		{
			String errorMessage = "Error regitering block (" + block + ") factory, as it seems to be already registered.\nGuilty stacktraces:";
			for(StackTraceElement element : Thread.currentThread().getStackTrace())
				errorMessage += "\n" + element;
			errorMessage += "\n";
			for(StackTraceElement element : ERROR_FALLBACK_LIST.get(block))
				errorMessage += "\n" + element;
			
			throw new IllegalArgumentException(errorMessage);
		}
		ERROR_FALLBACK_LIST.put(block, Arrays.copyOf(Thread.currentThread().getStackTrace(), Thread.currentThread().getStackTrace().length)); //Get it so I can store when a block was called, so if there's an issue its easier to diagnose 
	
		FACTORY_MAP.put(block.delegate, factory);
	}
	
	/**
	 * The default factory class
	 * @author Wyn Price
	 *
	 */
	public static class DefaultRenderFactory implements IBRLRenderFactory
	{

		@Override
		public List<BRLRenderInfo> getModels(IBlockAccess access, BlockPos pos, IBlockState inState) 
		{
			return Lists.newArrayList(new BRLRenderInfo(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(inState), inState, TextureMap.LOCATION_BLOCKS_TEXTURE));
		}
		
	}
}
