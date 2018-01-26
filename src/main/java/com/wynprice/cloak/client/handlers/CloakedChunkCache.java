package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.common.blocks.CloakBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

public class CloakedChunkCache extends ChunkCache
{

	private final ChunkCache oldCache;
	
	public CloakedChunkCache(ChunkCache oldCache, World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) 
	{
		super(worldIn, posFromIn, posToIn, subIn);
		this.oldCache = oldCache;
	}
	
	@Override
	public Biome getBiome(BlockPos pos) {
		return oldCache.getBiome(pos);
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(world.getBlockState(pos).getBlock() instanceof CloakBlock)
		{
			
		}
		return oldCache.getBlockState(pos);
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		return oldCache.getCombinedLight(pos, lightValue);
	}

	@Override
	public int getLightFor(EnumSkyBlock type, BlockPos pos) {
		return oldCache.getLightFor(type, pos);
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return oldCache.getStrongPower(pos, direction);
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return oldCache.getTileEntity(pos);
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_190300_2_) {
		return oldCache.getTileEntity(pos, p_190300_2_);
	}

	@Override
	public WorldType getWorldType() {
		return oldCache.getWorldType();
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return oldCache.isAirBlock(pos);
	}

	@Override
	public boolean isEmpty() {
		return oldCache.isEmpty();
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return oldCache.isSideSolid(pos, side, _default);
	}
	

}
