package com.wynprice.cloak.server.handlers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketClientRecieveImage;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ServerExternalImageHandler 
{
	
	public ServerExternalImageHandler() {
	}
	
	public static final HashMap<String, Pair<ResourceLocation, BufferedImage>> RESOURCE_MAP = new HashMap<>();

	public static void init()
	{
		if(FMLCommonHandler.instance().getMinecraftServerInstance() == null)
			return;
		File baseFile = new File(FMLCommonHandler.instance().getSavesDirectory(), FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/cloak-images");
		if(!baseFile.exists())
			baseFile.mkdirs();
		for(File file : baseFile.listFiles())
			try
			{
				if(RESOURCE_MAP.containsKey(file.getName()))
					continue;
				BufferedImage image = ImageIO.read(file);
				if(image.getWidth() != image.getHeight() || !Lists.newArrayList(16,32).contains(image.getWidth()))
					throw new Exception("Image must be 16x16, or 32x32");
				RESOURCE_MAP.put(file.getName(), Pair.of(new ResourceLocation("dynamic/" + file.getName() + "_1"), image));
			}
			catch (Throwable e)
			{	
				CloakMod.LOGGER.error("Unable to register file " + file.getName() + ", for reason: " + e.getMessage());
				e.printStackTrace();
			}
	}
	
	@SubscribeEvent
	public void onClientJoin(PlayerLoggedInEvent event)
	{
		if(event.player.world.isRemote) return;
		init();
		for(String fileName : RESOURCE_MAP.keySet())
			CloakNetwork.sendToPlayer(event.player, new PacketClientRecieveImage(RESOURCE_MAP.get(fileName).getRight(), fileName));
	}
	
}
