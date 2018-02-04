package com.wynprice.cloak.common.network.packets;

import java.awt.image.BufferedImage;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;

public class PacketClientRecieveImage extends PacketBaseImage
{
	
	public PacketClientRecieveImage() {
	}
	
	public PacketClientRecieveImage(BufferedImage image, String fileName) 
	{
		super(image, fileName);
	}

	@Override
	public void onReceived(PacketBaseImage message, EntityPlayer player) 
	{
		if(message.image == null)
			return;
		Minecraft.getMinecraft().addScheduledTask(new Runnable() { //Need to run on the main thread
			
			@Override
			public void run() {
				ExternalImageHandler.SYNCED_RESOURCE_MAP.put(message.fileName, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(message.fileName, new DynamicTexture(message.image)));
			}
		});
	}

}
