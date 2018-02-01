package com.wynprice.cloak.common.containers.slots;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemOnly extends SlotItemHandler
{
	
	private final ArrayList<Item> items;
	private final int slotLimit;
	
	public SlotItemOnly(ItemStackHandler itemHandler, int slotLimit, int index, int xPosition, int yPosition, Item... items) 
	{
		super(itemHandler, index, xPosition, yPosition);
		this.items = Lists.newArrayList(items);
		this.slotLimit = slotLimit;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return super.isItemValid(stack) && items.contains(stack.getItem()) && this.enabled;
	}
	
	private boolean enabled = false;
	
	public SlotItemOnly setEnabled(boolean enabled) 
	{
		this.enabled = enabled;
		return this;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public int getSlotStackLimit() 
	{
		return slotLimit;
	}

}
