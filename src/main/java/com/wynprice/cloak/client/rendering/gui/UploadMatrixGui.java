package com.wynprice.cloak.client.rendering.gui;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketServerRecieveImage;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class UploadMatrixGui extends ExternalImageGui
{
	
	@Override
	public void initGui() 
	{
		super.initGui();
		this.buttonList.get(1).displayString = I18n.format("gui.cloak.upload");
	}
	
	@Override
	protected HashMap<String, ResourceLocation> getMap() 
	{
		return ExternalImageHandler.LOCAL_RESOURCE_MAP;
	}
	
	@Override
	protected void closeGui(boolean save) 
	{
		if(save) //TODO send info to server
		{
			if(this.imageList.selectedPoint != -1)
			{
				String fileName = ((GuiExternalImageEntry)this.imageList.getListEntry(this.imageList.selectedPoint)).fileName;
				BufferedImage image = ExternalImageHandler.IMAGE_MAP.get(fileName);
				if(image != null)
					CloakNetwork.sendToServer(new PacketServerRecieveImage(image, fileName));
			}
		}
		
		this.mc.displayGuiScreen((GuiScreen)null);

        if (this.mc.currentScreen == null)
        {
            this.mc.setIngameFocus();
        }
	}
}
