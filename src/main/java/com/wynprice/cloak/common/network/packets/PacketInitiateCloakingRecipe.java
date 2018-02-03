package com.wynprice.cloak.common.network.packets;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class PacketInitiateCloakingRecipe extends BasicMessagePacket<PacketInitiateCloakingRecipe>
{

	@Override
	public void onReceived(PacketInitiateCloakingRecipe message, EntityPlayer player) 
	{
		ContainerBasicCloakingMachine container = ContainerBasicCloakingMachine.OPENMAP.get(player);
		if(container != null)
			doRecipe(container);
	}
	
	public static void doRecipe(ContainerBasicCloakingMachine container)
	{
		Slot input = container.getSlot(38);
		Slot output = container.getSlot(39);
		
		ItemStack stack = input.getStack();		
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound data = container.getTileEntity().writeRenderData(new NBTTagCompound());
		
		ItemStackHandler handler = new ItemStackHandler();
		ItemStackHandler handler2 = new ItemStackHandler(5);
		handler.deserializeNBT(data.getCompoundTag("ItemHandler"));
		handler2.setStackInSlot(0, handler.getStackInSlot(0));
		handler2.setStackInSlot(1, handler.getStackInSlot(1));
		handler2.setStackInSlot(2, handler.getStackInSlot(2));
		data.setTag("ItemHandler", handler2.serializeNBT());
		
		ItemStack testStack = stack.copy();
		testStack.getTagCompound().setTag("rendering_info", data.copy());
		
		ItemStackHandler outputhandler = new ItemStackHandler();
		outputhandler.setStackInSlot(0, output.getStack().copy());
		
		ItemStack leftOverStack = outputhandler.insertItem(0, testStack, true);
		int leftOverAmount = leftOverStack.isEmpty() ? 0 : leftOverStack.getCount();
		
		ItemStack inputSlotStack = stack.copy();
		inputSlotStack.setCount(leftOverAmount);
		input.putStack(inputSlotStack);
		
		ItemStack outputSlotStack = stack.copy();
		outputSlotStack.setTagInfo("rendering_info", data.copy());
		outputSlotStack.setCount(Math.min(leftOverAmount + stack.getCount(), 64));
		output.putStack(outputSlotStack);
	}

	
	public static void doRecipe(TileEntityCloakingMachine te)
	{
		
		ItemStackHandler itemhandler = te.getHandler();
		
		ItemStack stack = itemhandler.getStackInSlot(3);
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound data = te.writeRenderData(new NBTTagCompound());
		
		ItemStackHandler handler = new ItemStackHandler();
		ItemStackHandler handler2 = new ItemStackHandler(5);
		handler.deserializeNBT(data.getCompoundTag("ItemHandler"));
		handler2.setStackInSlot(0, handler.getStackInSlot(0));
		handler2.setStackInSlot(1, handler.getStackInSlot(1));
		handler2.setStackInSlot(2, handler.getStackInSlot(2));
		data.setTag("ItemHandler", handler2.serializeNBT());
		
		ItemStack testStack = stack.copy();
		testStack.getTagCompound().setTag("rendering_info", data.copy());
		
		ItemStack leftOverStack = itemhandler.insertItem(4, testStack, true);
		int leftOverAmount = leftOverStack.isEmpty() ? 0 : leftOverStack.getCount();
		
		ItemStack inputSlotStack = stack.copy();
		inputSlotStack.setCount(leftOverAmount);
		itemhandler.setStackInSlot(3, inputSlotStack);
		
		ItemStack outputSlotStack = stack.copy();
		outputSlotStack.setTagInfo("rendering_info", data.copy());
		outputSlotStack.setCount(Math.min(leftOverAmount + stack.getCount(), 64));
		itemhandler.setStackInSlot(4, outputSlotStack);
	}

}
