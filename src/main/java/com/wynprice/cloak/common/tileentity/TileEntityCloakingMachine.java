package com.wynprice.cloak.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.cloak.client.rendering.CloakedRenderingFactory;
import com.wynprice.cloak.client.rendering.models.BasicCloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.CloakedModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCloakingMachine extends BasicCloakedModelTileEntity
{
	
	public TileEntityCloakingMachine() 
	{
		this.handler.setSize(0);
	}
	
	public TileEntityCloakingMachine(boolean isAdvanced) 
	{
		this.isAdvanced = isAdvanced;
		this.handler.setSize(5);
	}

	private boolean isAdvanced;
	
	public boolean isAdvanced() 
	{
		return isAdvanced;
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setBoolean("isAdvanced", this.isAdvanced);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		this.isAdvanced = compound.getBoolean("isAdvanced");		
		super.readFromNBT(compound);
	}
	
	@SideOnly(Side.CLIENT)
	public static final CloakedRenderingFactory FACTORY = new CloakedRenderingFactory() {
		
		@Override
		public CloakedModel createModel(IBlockState modelState, IBlockState renderState) 
		{
			return new BasicCloakingMachineModel(modelState, renderState);
		}
	};
}
