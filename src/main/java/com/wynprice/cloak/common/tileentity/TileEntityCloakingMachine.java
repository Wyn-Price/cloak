package com.wynprice.cloak.common.tileentity;

import com.wynprice.cloak.client.rendering.CloakedRenderingFactory;
import com.wynprice.cloak.client.rendering.models.BasicCloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.CloakedModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		public CloakedModel createModel(World world, BlockPos pos, IBlockState modelState, IBlockState renderState) 
		{
			return new BasicCloakingMachineModel(modelState, renderState);
		}
	};
}
