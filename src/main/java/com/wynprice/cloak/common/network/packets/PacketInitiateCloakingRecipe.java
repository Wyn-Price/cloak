package com.wynprice.cloak.common.network.packets;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketInitiateCloakingRecipe extends BasicMessagePacket<PacketInitiateCloakingRecipe>
{

	@Override
	public void onReceived(PacketInitiateCloakingRecipe message, EntityPlayer player) 
	{
		ContainerBasicCloakingMachine container = ContainerBasicCloakingMachine.OPENMAP.get(player);
		if(container != null)
			setOutputNBT(container);
	}
	
	public static void setOutputNBT(ContainerBasicCloakingMachine container)
	{
		ItemStack stack = container.getSlot(38).getStack();
		container.getSlot(38).putStack(ItemStack.EMPTY);
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("rendering_info", container.getTileEntity().writeRenderData(new NBTTagCompound()));
		container.getSlot(39).putStack(stack);
	}

}
