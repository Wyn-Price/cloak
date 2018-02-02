package com.wynprice.cloak.common.tileentity;

import com.wynprice.cloak.client.rendering.CloakedRenderingFactory;
import com.wynprice.cloak.client.rendering.models.BasicCloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.common.blocks.CloakBlock;
import com.wynprice.cloak.common.containers.inventory.ItemHandlerItemOnly;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.registries.CloakItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCloakingMachine extends BasicCloakedModelTileEntity
{
	
	protected ItemStackHandler outputHandler = new ItemStackHandler();
	protected ItemHandlerItemOnly inputHandler = new ItemHandlerItemOnly();

	
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
	
	public ItemStackHandler getOutputHandler() {
		return outputHandler;
	}
	
	public ItemStackHandler getInputHandler() {
		return inputHandler;
	}
		
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setBoolean("isAdvanced", this.isAdvanced);
		this.handler.setStackInSlot(3, this.inputHandler.getStackInSlot(0));
		this.handler.setStackInSlot(4, this.outputHandler.getStackInSlot(0));
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		this.isAdvanced = compound.getBoolean("isAdvanced");
		super.readFromNBT(compound);
		this.inputHandler = new ItemHandlerItemOnly(NonNullList.from(ItemStack.EMPTY, this.handler.getStackInSlot(3)), Item.getItemFromBlock(CloakBlocks.CLOAK_BLOCK));
		this.outputHandler = new ItemStackHandler(NonNullList.from(ItemStack.EMPTY, this.handler.getStackInSlot(4)));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if(facing == EnumFacing.UP || facing == EnumFacing.DOWN)
				return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if(facing == EnumFacing.UP)
				return (T) this.inputHandler;
			else if(facing == EnumFacing.DOWN)
				return (T) this.outputHandler;
		return super.getCapability(capability, facing);
	}
}
