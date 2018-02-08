package com.wynprice.cloak.client.rendering.world;

import org.apache.logging.log4j.core.util.Loader;
import org.lwjgl.opengl.GLContext;

import com.wynprice.cloak.common.CommonProxy;
import com.wynprice.cloak.common.blocks.CloakBlock;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.tileentity.BasicCloakedModelTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

public class CloakChunkCache extends ChunkCache
{
	private final ChunkCache oldCache;
	
	private final BlockPos fromPos;
	private final BlockPos toPos;
	private final int sub;
	
	public CloakChunkCache(World world, BlockPos from, BlockPos to, int sub, ChunkCache oldCache)
	{
		super(world, from, to, sub);
		this.oldCache = oldCache;
		this.fromPos = from;
		this.toPos = to;
		this.sub = sub;
	}
	
	@Override
	public boolean isEmpty() 
	{
		return oldCache.isEmpty();
	}
	
	@Override
	public Biome getBiome(BlockPos pos) 
	{
		return oldCache.getBiome(pos);
	}
				
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(CommonProxy.isOptifine) return oldCache.getBlockState(pos); //If optifine is here, dont even try to do anything, it'll just cause more issues
		if(super.getBlockState(pos).getBlock() == CloakBlocks.CLOAK_BLOCK && this.getTileEntity(pos) instanceof BasicCloakedModelTileEntity)
			if(!isStackSecondMethod("rebuildChunk", "func_178581_b") && !isStackSecondMethod("shouldSideBeRendered", "func_176225_a"))
				return NBTUtil.readBlockState(((BasicCloakedModelTileEntity)this.getTileEntity(pos)).getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info"));

		return oldCache.getBlockState(pos);
	}	
	
	private boolean isStackSecondMethod(String methodName, String deobMethodName)
	{
		for(int i = 0; i < Thread.currentThread().getStackTrace().length; i++)
			if(i > 0)
			{
				try
				{
					if(ChunkCache.class.isAssignableFrom(Class.forName(Thread.currentThread().getStackTrace()[i].getClassName())))
						continue;
					else if(Thread.currentThread().getStackTrace()[i].getMethodName().equalsIgnoreCase(methodName) || Thread.currentThread().getStackTrace()[i].getMethodName().equalsIgnoreCase(deobMethodName))
						return true;
					else
						return false;
				}
				catch (Exception e) 
				{
					;
				}
			}
		
		return false;
			
	}
	
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) 
	{

		return this.oldCache.getCombinedLight(pos, lightValue);
	}

	
	@Override
	public int getLightFor(EnumSkyBlock type, BlockPos pos) 
	{
		return oldCache.getLightFor(type, pos);
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) 
	{
		return oldCache.getStrongPower(pos, direction);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos) 
	{
		return oldCache.getTileEntity(pos);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_190300_2_)
	{
		return oldCache.getTileEntity(pos, p_190300_2_);
	}
	
	@Override
	public WorldType getWorldType() 
	{
		return oldCache.getWorldType();
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos) 
	{
		return oldCache.isAirBlock(pos);
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) 
	{
		return oldCache.isSideSolid(pos, side, _default);
	}
}
