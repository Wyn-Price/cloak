package com.wynprice.cloak.common.containers.slots;

import com.wynprice.cloak.client.rendering.gui.AdvancedGui;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.registries.CloakItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotCaptureBlockOnlyAdvanced extends SlotCaptureBlockOnly
{
	
	private final ContainerBasicCloakingMachine container;

	public SlotCaptureBlockOnlyAdvanced(ContainerBasicCloakingMachine container, ItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.container = container;
	}
	
	@Override
	public void putStack(ItemStack stack) {
		this.container.modification_list.put(this.container.selectedContainer, stack);
		super.putStack(stack);
	}
	
	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.container.modification_list.put(this.container.selectedContainer, ItemStack.EMPTY);
		return super.onTake(thePlayer, stack);
	}
}
