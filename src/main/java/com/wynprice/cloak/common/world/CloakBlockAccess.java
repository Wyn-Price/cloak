package com.wynprice.cloak.common.world;

import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.tileentity.BasicCloakedModelTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

public class CloakBlockAccess implements IBlockAccess
{
	
	private final IBlockAccess access;
	
	public CloakBlockAccess(IBlockAccess access) 
	{
		this.access = access;
	}
	
	@Override
	public Biome getBiome(BlockPos pos) 
	{
		return access.getBiome(pos);
	}
				
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(access.getBlockState(pos).getBlock() == CloakBlocks.CLOAK_BLOCK && this.getTileEntity(pos) instanceof BasicCloakedModelTileEntity)
			return NBTUtil.readBlockState(((BasicCloakedModelTileEntity)this.getTileEntity(pos)).getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info"));

		return access.getBlockState(pos);
	}	
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) 
	{
		return this.access.getCombinedLight(pos, lightValue);
	}

	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) 
	{
		return access.getStrongPower(pos, direction);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos) 
	{
		return access.getTileEntity(pos);
	}
	
	@Override
	public WorldType getWorldType() 
	{
		return access.getWorldType();
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos) 
	{
		return access.isAirBlock(pos);
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) 
	{
		return access.isSideSolid(pos, side, _default);
	}
}
