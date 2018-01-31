package com.wynprice.cloak.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTUtil;

public class TileEntityCloakBlock extends BasicCloakedModelTileEntity
{
	public IBlockState getRenderState()
	{
		return NBTUtil.readBlockState(this.handler.getStackInSlot(0).getOrCreateSubCompound("capture_info"));
	}
	
	public IBlockState getModelState()
	{
		return NBTUtil.readBlockState(this.handler.getStackInSlot(1).getOrCreateSubCompound("capture_info"));

	}
}
