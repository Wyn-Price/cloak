package com.wynprice.cloak.common.containers;

import java.util.ArrayList;

import com.wynprice.cloak.common.containers.slots.SlotCaptureBlockOnly;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerBasicCloakingMachine extends Container
{
	
	private final ItemStackHandler handler;
	
	private ArrayList<int[]> list = new ArrayList<>();
	
	@SideOnly(Side.CLIENT)
	public ContainerBasicCloakingMachine(ItemStackHandler handler, IBlockState state, IBakedModel displayModel)
	{
		ArrayList<int[]> list = new ArrayList<>();
		for(EnumFacing facing : EnumFacing.values())
			for(BakedQuad quad : displayModel.getQuads(state, facing, 0))
				list.add(quad.getVertexData());
		for(BakedQuad quad : displayModel.getQuads(state, null, 0))
			list.add(quad.getVertexData());
		this.list.addAll(list);
		
		this.handler = handler;
		this.handler.setSize(list.size());
		for(int i = 0; i < list.size(); i++)
			this.addSlotToContainer(new SlotCaptureBlockOnly(handler, i, 0, 0));
		
	}
	
	public ContainerBasicCloakingMachine(ItemStackHandler handler, int amount) 
	{
		this.handler = handler;
		for(int i = 0; i < amount; i++)
			this.addSlotToContainer(new SlotCaptureBlockOnly(handler, i, 0, 0));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) 
	    {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < handler.getSlots()) 
	        	if (!this.mergeItemStack(current, handler.getSlots(), 38, true))
	        		return ItemStack.EMPTY;
	        	else
	        		;
	        else if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
	                return ItemStack.EMPTY;
	        if (current.getCount() == 0)
	            slot.putStack(ItemStack.EMPTY);
	        else
	            slot.onSlotChanged();

	        if (current.getCount() == previous.getCount())
	            return null;
	        slot.onTake(playerIn, current);
	    }
	    return previous;
	}

}
