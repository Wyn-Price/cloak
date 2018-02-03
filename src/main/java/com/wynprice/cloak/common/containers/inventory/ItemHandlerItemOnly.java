package com.wynprice.cloak.common.containers.inventory;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.wynprice.cloak.common.registries.CloakBlocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerItemOnly extends ItemStackHandler
{
		
	private final ArrayList<Item> accepted_items;
		
	public ItemHandlerItemOnly() 
	{
		this.accepted_items = Lists.newArrayList();
	}
	
	public ItemHandlerItemOnly(NonNullList<ItemStack> stackList, Item... stacks) 
	{
		super(stackList);
		accepted_items = Lists.newArrayList(stacks);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		if(!accepted_items.contains(stack.getItem()))
			return stack;
		return super.insertItem(slot, stack, simulate);
	}
}
