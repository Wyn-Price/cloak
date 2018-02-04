package com.wynprice.cloak.client.handlers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.common.collect.Lists;
import com.wynprice.cloak.CloakMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class ExternalImageHandler 
{
	public static final HashMap<String, ResourceLocation> LOCAL_RESOURCE_MAP = new HashMap<>();
	public static final HashMap<String, BufferedImage> IMAGE_MAP = new HashMap<>();

	
	public static final HashMap<String, ResourceLocation> SYNCED_RESOURCE_MAP = new HashMap<>();

	
	public static void init()
	{
		File baseFile = new File(Minecraft.getMinecraft().mcDataDir, "cloak-images");
		if(!baseFile.exists())
			baseFile.mkdirs();
		boolean h = false;
		for(File file : baseFile.listFiles())
			try
			{
				if(LOCAL_RESOURCE_MAP.containsKey(file.getName()))
					continue;
				BufferedImage image = ImageIO.read(file);
				if(image.getWidth() != image.getHeight() || !Lists.newArrayList(16,32).contains(image.getWidth()))
					throw new Exception("Image must be 16x16, or 32x32");
				IMAGE_MAP.put(file.getName(), image);
				LOCAL_RESOURCE_MAP.put(file.getName(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName(), new DynamicTexture(image)));
			}
			catch (Throwable e)
			{	
				CloakMod.LOGGER.error("Unable to register file " + file.getName() + ", for reason: " + e.getMessage());
				e.printStackTrace();
			}
	}
}
