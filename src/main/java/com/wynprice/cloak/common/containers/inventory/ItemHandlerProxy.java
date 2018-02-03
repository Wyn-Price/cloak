package com.wynprice.cloak.common.containers.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerProxy extends ItemStackHandler
{
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return super.getSlotLimit(slot);
	}
	
	@Override
	public int getSlots() {
		return super.getSlots();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		return super.serializeNBT();
	}
	
	@Override
	public void setSize(int size) {
		super.setSize(size);
	}
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		super.setStackInSlot(slot, stack);
	}
	
	
}
