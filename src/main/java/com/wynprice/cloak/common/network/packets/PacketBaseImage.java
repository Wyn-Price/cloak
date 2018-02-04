package com.wynprice.cloak.common.network.packets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class PacketBaseImage extends BasicMessagePacket<PacketBaseImage>
{
	
	public PacketBaseImage() {
	}
	
	public PacketBaseImage(BufferedImage image, String fileName) 
	{
		this.image = image;
		this.fileName = fileName;
	}

	protected BufferedImage image;
	protected String fileName;
	
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", fileName);
		nbt.setInteger("width", image.getWidth());
		nbt.setInteger("height", image.getHeight());
		nbt.setIntArray("colors", image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			CompressedStreamTools.writeCompressed(nbt, stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] array = stream.toByteArray();
		
		buf.writeInt(array.length);
		for(byte b : array)
			buf.writeByte(b);
		
		super.toBytes(buf);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		int size = buf.readInt();
		byte[] bytes = new byte[size];
		for(int i = 0; i < size; i++)
			bytes[i] = buf.readByte();
		

		try 
		{
			NBTTagCompound compound = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
			this.fileName = compound.getString("name");
			image = new BufferedImage(compound.getInteger("width"), compound.getInteger("height"), 1);
			image.setRGB(0, 0, image.getWidth(), image.getHeight(), compound.getIntArray("colors"), 0, image.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.fromBytes(buf);
	}
}
