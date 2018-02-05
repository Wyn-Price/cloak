package com.wynprice.cloak.common.tileentity;

import com.wynprice.cloak.common.containers.inventory.ItemHandlerItemOnly;
import com.wynprice.cloak.common.registries.CloakBlocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCloakingMachine extends BasicCloakedModelTileEntity
{

	public TileEntityCloakingMachine() 
	{
		this.handler.setSize(5);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if(facing == EnumFacing.DOWN)
				return (T) new ItemStackHandler(NonNullList.from(ItemStack.EMPTY, this.handler.getStackInSlot(4)));
			else
				return (T) new ItemHandlerItemOnly(NonNullList.from(ItemStack.EMPTY, this.handler.getStackInSlot(3)), Item.getItemFromBlock(CloakBlocks.CLOAK_BLOCK));
		return super.getCapability(capability, facing);
	}
}
