package com.wynprice.cloak.common.containers;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.common.containers.slots.SlotCaptureBlockOnlyAdvanced;
import com.wynprice.cloak.common.containers.slots.SlotItemHandlerOutput;
import com.wynprice.cloak.common.containers.slots.SlotItemOnly;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerBasicCloakingMachine extends Container
{
	
	private final ItemStackHandler handler;
	
	public final HashMap<Integer, ItemStack> modification_list;
	
	public int selectedContainer = -1;
	
	private final TileEntityCloakingMachine tileEntity;
		
	public ContainerBasicCloakingMachine(EntityPlayer player, TileEntityCloakingMachine machine)
	{
		this.tileEntity = machine;
		if(!player.world.isRemote)
			OPENMAP.put(player, this);
		boolean advanced = machine.isAdvanced();
		if(!advanced)
			modification_list = new HashMap<>();
		else
			this.modification_list = machine.getCurrentModificationList();
		this.handler = machine.getHandler();
		
		int id = 0;
		
		for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18 + 76, 103 + l * 18 + 35));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18 + 76, 161 + 35));
        }
				
		this.addSlotToContainer(new SlotItemOnly(handler, 1, 1, 84, 30, CloakItems.BLOCKSTATE_CARD).setEnabled(true)); //36
		this.addSlotToContainer(new SlotItemOnly(handler, 1, 0, 228, 30, CloakItems.BLOCKSTATE_CARD, CloakItems.LIQUDSTATE_CARD, CloakItems.EXTERNAL_CARD).setEnabled(true)); //37
		
		this.addSlotToContainer(new SlotItemOnly(tileEntity.getHandler(), 64, 3, 245, 90, Item.getItemFromBlock(CloakBlocks.CLOAK_BLOCK)).setEnabled(true)); //38
		this.addSlotToContainer(new SlotItemHandlerOutput(this, tileEntity.getHandler(), 4, 306, 90)); //39
		
		if(advanced)
			this.addSlotToContainer(new SlotCaptureBlockOnlyAdvanced(this, handler, 2, 248, 30)); //40
	}
		
	public static final HashMap<EntityPlayer, ContainerBasicCloakingMachine> OPENMAP = new HashMap<>();
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < 36) {
	            if (!this.mergeItemStack(current, 36, handler.getSlots() + 36 - (this.tileEntity.isAdvanced() ? 0 : 1), false))
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
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		markDirty();
	}
	
	public void markDirty()
	{
		tileEntity.setCurrentModificationList(modification_list);
		if(!tileEntity.getWorld().isRemote)
			tileEntity.markDirty();
	}
	
	public TileEntityCloakingMachine getTileEntity() {
		return tileEntity;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	public Pair<HashMap<Integer, IBlockState>, HashMap<Integer, ResourceLocation>> getBlockStateMap()
	{
		HashMap<Integer, IBlockState> overrideList = new HashMap<>();
		HashMap<Integer, ResourceLocation> externalOverrideList = new HashMap<>();
		
		for(int i : this.modification_list.keySet())
			if(this.modification_list.get(i) != null && !this.modification_list.get(i).isEmpty())
				if(this.modification_list.get(i).getItem() != CloakItems.EXTERNAL_CARD)
					overrideList.put(i, NBTUtil.readBlockState(this.modification_list.get(i).getSubCompound("capture_info")));
				else if(ExternalImageHandler.RESOURCE_MAP.containsKey(this.modification_list.get(i).getSubCompound("capture_info").getString("external_image")))
					externalOverrideList.put(i, ExternalImageHandler.RESOURCE_MAP.get(this.modification_list.get(i).getSubCompound("capture_info").getString("external_image")));
		
		return Pair.of(overrideList, externalOverrideList);
	}

}
