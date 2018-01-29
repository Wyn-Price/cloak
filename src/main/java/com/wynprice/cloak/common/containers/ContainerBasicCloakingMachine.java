package com.wynprice.cloak.common.containers;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.cloak.common.containers.slots.SlotCaptureBlockOnly;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
		
	@SideOnly(Side.CLIENT)
	public ContainerBasicCloakingMachine(EntityPlayer player, ItemStackHandler handler)
	{
//		if(!player.world.isRemote)
//			OPENMAP.put(player, this);
		this.handler = handler;
		int id = 0;
		
		for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + 20));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 161 + 20));
        }
		
		
		for(int i = 0; i < handler.getSlots() - 1; i++)
			this.addSlotToContainer(new SlotCaptureBlockOnly(handler, i, 152, 0).setEnabled(true));	
		
		this.addSlotToContainer(new SlotCaptureBlockOnly(handler, handler.getSlots() - 1, 8, 0).setEnabled(true));

	}
	
//	public static final HashMap<EntityPlayer, ContainerBasicCloakingMachine> OPENMAP = new HashMap<>();
//	
//	@Override
//	public void onContainerClosed(EntityPlayer playerIn) {
//		// TODO Auto-generated method stub
//		super.onContainerClosed(playerIn);
//	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < 36) {
	            if (!this.mergeItemStack(current, 36, handler.getSlots() + 36, true))
	                return ItemStack.EMPTY;
	        } else {
	            if (!this.mergeItemStack(current, 0, 36, false))
	                return ItemStack.EMPTY;
	        }
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
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
