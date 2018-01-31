package com.wynprice.cloak.common.network.packets;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class PacketRemoveModificationList extends BasicMessagePacket<PacketRemoveModificationList>
{
	@Override
	public void onReceived(PacketRemoveModificationList message, EntityPlayer player) 
	{
		ContainerBasicCloakingMachine container = ContainerBasicCloakingMachine.OPENMAP.get(player);
		if(container != null)
			updateContainer(container, player);
	}
	
	public static void updateContainer(ContainerBasicCloakingMachine container, EntityPlayer player)
	{
		if(!container.getTileEntity().isAdvanced())
			return;
		for(ItemStack stack : container.modification_list.values())
			spawnItemStack(player, stack);		
		container.modification_list.clear();
		container.getSlot(40).putStack(ItemStack.EMPTY);
		container.markDirty();

	}
	
	private static void spawnItemStack(EntityPlayer player, ItemStack stack)
	{
		if(!player.world.isRemote)
		{
			boolean flag = player.inventory.addItemStackToInventory(stack);
			if (flag)
            {
				player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.inventoryContainer.detectAndSendChanges();
            }
			else
            {
                EntityItem entityitem = player.dropItem(stack, false);

                if (entityitem != null)
                {
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(player.getName());
                }
            }
		}
	}

}
