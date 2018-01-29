package com.wynprice.cloak.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.collection.parallel.ParIterableLike.Min;

public abstract class BasicMessagePacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ>
{
	@Override
	public REQ onMessage(REQ message, MessageContext ctx) {
		onReceived(message, ctx.side == Side.SERVER ? ctx.getServerHandler().player : getPlayer());
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
	
	/**
	 * Write the values to bytes
	 */
	@Override
	public void toBytes(ByteBuf buf) {};
	
	/**
	 * Read the bytes, and convert to values
	 */
	@Override
	public void fromBytes(ByteBuf buf) {};
	
	
	public abstract void onReceived(REQ message, EntityPlayer player);

}