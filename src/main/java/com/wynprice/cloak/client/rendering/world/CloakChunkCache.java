package com.wynprice.cloak.client.rendering.world;

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
			
	public int prevInt = -1;
	
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(super.getBlockState(pos).getBlock() == CloakBlocks.CLOAK_BLOCK && this.getTileEntity(pos) instanceof BasicCloakedModelTileEntity && 
				!(Thread.currentThread().getStackTrace()[2].getClassName().equals(RenderChunk.class.getName())
						|| Thread.currentThread().getStackTrace()[2].getMethodName().equals("func_176225_a") || Thread.currentThread().getStackTrace()[2].getMethodName().equals("shouldSideBeRendered")))
		{
			return NBTUtil.readBlockState(((BasicCloakedModelTileEntity)this.getTileEntity(pos)).getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info"));
		}
		return oldCache.getBlockState(pos);
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
