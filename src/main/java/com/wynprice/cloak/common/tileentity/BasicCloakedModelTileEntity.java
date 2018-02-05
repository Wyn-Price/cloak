package com.wynprice.cloak.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

public class BasicCloakedModelTileEntity extends BasicTileEntity 
{

	protected final ItemStackHandler handler = new ItemStackHandler(5);
	protected HashMap<Integer, ItemStack> currentModificationList = new HashMap<>();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		writeRenderData(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		readRenderData(compound);
		super.readFromNBT(compound);
	}
	
	public NBTTagCompound writeRenderData(NBTTagCompound compound) 
	{
		compound.setTag("ItemHandler", handler.serializeNBT());
		compound.setTag("mod_list", writeToNBT(currentModificationList));
		return compound;
	}
	
	public void readRenderData(NBTTagCompound compound) 
	{
		handler.deserializeNBT(compound.getCompoundTag("ItemHandler"));
		this.currentModificationList = readFromNBTTag(compound.getCompoundTag("mod_list"));
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
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public boolean canRenderBreaking() {
		return true;
	}
	
	
}
