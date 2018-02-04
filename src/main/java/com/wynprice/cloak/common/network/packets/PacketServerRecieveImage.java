package com.wynprice.cloak.common.network.packets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.server.handlers.ServerExternalImageHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PacketServerRecieveImage extends PacketBaseImage
{

	public PacketServerRecieveImage() {
	}
	
	public PacketServerRecieveImage(BufferedImage image, String fileName) 
	{
		super(image, fileName);
	}

	@Override
	public void onReceived(PacketBaseImage message, EntityPlayer player) 
	{
		File baseFile = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/cloak-images");
		if(!baseFile.exists())//Shouldnt happen as its called in init. Either way, if this does occur then thats fine
			baseFile.mkdirs();
		File outputfile = new File(baseFile, message.fileName);
		int curId = 1;
		String[] fileNameList = message.fileName.split("\\.");
		String fName = "";
		String fExtension = "";
		for(int i = 0; i < fileNameList.length; i++)
			if(i == fileNameList.length - 1)
				fExtension = fileNameList[i];
			else
				fName += fileNameList[i];
		
		while(outputfile.exists())
			outputfile = new File(baseFile, fName + " (" + String.valueOf(curId++) + ")." + fExtension);

		try {
			ImageIO.write(message.image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerExternalImageHandler.init();
		CloakNetwork.sendToAll(new PacketClientRecieveImage(message.image, outputfile.getName()));
	}
}
