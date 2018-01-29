package com.wynprice.cloak.common.containers.slots;

import com.wynprice.cloak.common.registries.CloakItems;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotCaptureBlockOnly extends SlotItemHandler
{
	
	public SlotCaptureBlockOnly(ItemStackHandler itemHandler, int index, int xPosition, int yPosition) 
	{
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return super.isItemValid(stack) && stack.getItem() == CloakItems.BLOCKSTATE_CARD && this.enabled;
	}
	
	private boolean enabled = false;
	
	public SlotCaptureBlockOnly setEnabled(boolean enabled) 
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
		return 1;
	}

}
