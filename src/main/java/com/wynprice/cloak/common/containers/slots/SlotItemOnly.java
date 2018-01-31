package com.wynprice.cloak.common.containers.slots;

import com.wynprice.cloak.common.registries.CloakItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemOnly extends SlotItemHandler
{
	
	private final Item item;
	private final int slotLimit;
	
	public SlotItemOnly(ItemStackHandler itemHandler, Item item, int slotLimit, int index, int xPosition, int yPosition) 
	{
		super(itemHandler, index, xPosition, yPosition);
		this.item = item;
		this.slotLimit = slotLimit;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return super.isItemValid(stack) && stack.getItem() == item && this.enabled;
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
