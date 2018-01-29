package com.wynprice.cloak.common.network.packets;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class PacketSendRenderInfoAdvancedGUI extends BasicMessagePacket<PacketSendRenderInfoAdvancedGUI>
{
	
	public PacketSendRenderInfoAdvancedGUI() {
	}
	
	public PacketSendRenderInfoAdvancedGUI(boolean removeAll) 
	{
		this.removeAll = removeAll;
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeBoolean(removeAll);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.removeAll = buf.readBoolean();
	}
	
	private boolean removeAll;

	@Override
	public void onReceived(PacketSendRenderInfoAdvancedGUI message, EntityPlayer player) 
	{
		ContainerBasicCloakingMachine container = ContainerBasicCloakingMachine.OPENMAP.get(player);
		if(container != null)
			updateContainer(container, player, message.removeAll);
	}
	
	public static void updateContainer(ContainerBasicCloakingMachine container, EntityPlayer player, boolean removeAll)
	{
		for(ItemStack stack : container.modification_list.values())
			spawnItemStack(player, stack);
		if(removeAll)
		{
			for(int i = 36; i < 38; i++)
			{
				spawnItemStack(player, container.inventorySlots.get(i).getStack());
			}
			if(!player.world.isRemote)
				ContainerBasicCloakingMachine.OPENMAP.remove(player);
		}
				
		container.modification_list.clear();

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
