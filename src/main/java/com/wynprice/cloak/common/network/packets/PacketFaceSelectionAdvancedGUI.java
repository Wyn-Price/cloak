package com.wynprice.cloak.common.network.packets;

import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.containers.slots.SlotItemOnly;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PacketFaceSelectionAdvancedGUI extends BasicMessagePacket<PacketFaceSelectionAdvancedGUI>
{
	public PacketFaceSelectionAdvancedGUI() {
	}
	
	public PacketFaceSelectionAdvancedGUI(int selectedFace, int previousFace) 
	{
		this.selectedFace = selectedFace;
		this.previousFace = previousFace;

	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(selectedFace);
		buf.writeInt(previousFace);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.selectedFace = buf.readInt();
		this.previousFace = buf.readInt();
	}
	
	private int selectedFace;
	private int previousFace;

	@Override
	public void onReceived(PacketFaceSelectionAdvancedGUI message, EntityPlayer player) 
	{
		ContainerBasicCloakingMachine container = ContainerBasicCloakingMachine.OPENMAP.get(player);
		if(container != null)
			setContainerFace(container, message.selectedFace, message.previousFace);
	}
	
	public static void setContainerFace(ContainerBasicCloakingMachine container, int newFace, int oldFace)
	{
		if(!container.getTileEntity().isAdvanced())
			return;
		container.selectedContainer = newFace;
		SlotItemOnly slot = (SlotItemOnly) container.getSlot(40);
		if(oldFace != -1)
			container.modification_list.put(oldFace, slot.getStack());
		if(newFace != -1)
		{
			ItemStack stack = container.modification_list.get(newFace);
			slot.putStack(stack == null ? ItemStack.EMPTY : stack);
		}
						
		slot.setEnabled(newFace != -1);
	}
}
