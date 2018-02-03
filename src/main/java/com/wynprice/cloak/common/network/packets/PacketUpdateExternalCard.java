package com.wynprice.cloak.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdateExternalCard extends BasicMessagePacket<PacketUpdateExternalCard>
{
	
	public PacketUpdateExternalCard() {
	}
	
	private String fileName;
	
	
	public PacketUpdateExternalCard(String fileName) 
	{
		this.fileName = fileName;
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, fileName);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		fileName = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void onReceived(PacketUpdateExternalCard message, EntityPlayer player) 
	{
		setNBT(player, message.fileName);
	}
	
	public static void setNBT(EntityPlayer player, String fileName)
	{
		for(EnumHand hand : EnumHand.values())
		{
			ItemStack stack = player.getHeldItem(hand);
			stack.getOrCreateSubCompound("capture_info").setString("external_image", fileName);
		}
	}

}
