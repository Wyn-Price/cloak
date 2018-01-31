package com.wynprice.cloak.common.containers.slots;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketRemoveModificationList;
import com.wynprice.cloak.common.registries.CloakItems;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class SlotItemOnlySendPacket extends SlotItemOnly
{

	private final ContainerBasicCloakingMachine container;
	
	public SlotItemOnlySendPacket(ContainerBasicCloakingMachine container, ItemStackHandler itemHandler, int index, int xPosition,
			int yPosition) 
	{
		super(itemHandler, CloakItems.BLOCKSTATE_CARD, 1, index, xPosition, yPosition);
		this.container = container;
	}
	
	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);
	}
	
	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) 
	{
		System.out.println(thePlayer);
		
		PacketRemoveModificationList.updateContainer(container, Minecraft.getMinecraft().player);
		CloakNetwork.sendToServer(new PacketRemoveModificationList());
		
		return super.onTake(thePlayer, stack);
	}

}
