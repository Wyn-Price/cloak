package com.wynprice.cloak.client.handlers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.wynprice.cloak.CloakMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class ExternalImageHandler 
{
	public static final HashMap<String, ResourceLocation> RESOURCE_MAP = new HashMap<>();
	
	public static void init()
	{
		File baseFile = new File(Minecraft.getMinecraft().mcDataDir, "cloak-images");
		if(!baseFile.exists())
			baseFile.mkdirs();
		for(File file : baseFile.listFiles())
			try
			{
				if(RESOURCE_MAP.containsKey(file.getName()))
					continue;
				BufferedImage image = ImageIO.read(file);
				RESOURCE_MAP.put(file.getName(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName(), new DynamicTexture(image)));
			}
			catch (Throwable e)
			{	
				CloakMod.LOGGER.error("Unable to register file " + file.getName() + ", for reason: " + e.getMessage());
				e.printStackTrace();
			}
	}
}
