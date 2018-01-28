package com.wynprice.cloak.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCloakingMachine extends BasicTileEntity
{
	ItemStackHandler handler = new ItemStackHandler(2);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("ItemStackHandler", handler.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		handler.setSize(compound.getCompoundTag("ItemStackHandler").getInteger("Size"));
		handler.deserializeNBT(compound.getCompoundTag("ItemStackHandler"));
		super.readFromNBT(compound);
	}
	
	public ItemStackHandler getHandler() {
		return handler;
	}
}
