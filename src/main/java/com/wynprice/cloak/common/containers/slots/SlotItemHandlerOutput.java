package com.wynprice.cloak.common.containers.slots;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerOutput extends SlotItemHandler
{
	
	public SlotItemHandlerOutput(ContainerBasicCloakingMachine container, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

}
