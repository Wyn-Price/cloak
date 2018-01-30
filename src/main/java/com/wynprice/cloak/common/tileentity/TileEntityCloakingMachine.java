package com.wynprice.cloak.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCloakingMachine extends BasicTileEntity
{
	
	public TileEntityCloakingMachine() 
	{
		this.handler = new ItemStackHandler(0);
	}
	
	public TileEntityCloakingMachine(boolean isAdvanced) 
	{
		this.isAdvanced = isAdvanced;
		this.handler = new ItemStackHandler(isAdvanced ? 3 : 2);
	}

	private boolean isAdvanced;

	private final ItemStackHandler handler;
	
	public boolean isAdvanced() 
	{
		return isAdvanced;
	}
	
	private HashMap<Integer, ItemStack> currentModificationList = new HashMap<>();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setTag("ItemHandler", handler.serializeNBT());
		compound.setTag("mod_list", writeToNBT(currentModificationList));
		compound.setBoolean("isAdvanced", this.isAdvanced);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		this.isAdvanced = compound.getBoolean("isAdvanced");
		handler.deserializeNBT(compound.getCompoundTag("ItemHandler"));
		this.currentModificationList = readFromNBTTag(compound.getCompoundTag("mod_list"));
		
		super.readFromNBT(compound);
	}
	
	public ItemStackHandler getHandler() 
	{
		return handler;
	}
	
	public void setCurrentModificationList(HashMap<Integer, ItemStack> currentModificationList) {
		this.currentModificationList = currentModificationList;
	}
	
	public HashMap<Integer, ItemStack> getCurrentModificationList() {
		return currentModificationList;
	}
	
	public static HashMap<Integer, ItemStack> readFromNBTTag(NBTTagCompound compound)
	{
		HashMap<Integer, ItemStack> map = new HashMap<>();
		int[] keys = compound.getIntArray("keys");
		for(int i : keys)
			map.put(i, new ItemStack(compound.getCompoundTag("itemstack_" + String.valueOf(i))));
		return map;
	}
	
	public static NBTTagCompound writeToNBT(HashMap<Integer, ItemStack> map)
	{
		NBTTagCompound compound = new NBTTagCompound();
		ArrayList<Integer> intList = new ArrayList<>();
		for(int key : map.keySet())
			intList.add(key);
		int[] keys = new int[intList.size()];
		for(int i = 0; i < keys.length; i++)
			keys[i] = intList.get(i);
		compound.setIntArray("keys", keys);
		for(int i : keys)
			compound.setTag("itemstack_" + String.valueOf(i), map.get(i).serializeNBT());
		
		return compound;
	}
}
