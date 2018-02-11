package com.wynprice.brl;

import com.wynprice.brl.addons.tblloader.TBLModelLoader;
import com.wynprice.brl.events.TextureStitch;

import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "brl", name = "BetterRenderLib", version = "0.1")
public class BrlEventSubscriber 
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Object[] objects = 
			{
					new TextureStitch()
			};
		
		ModelLoaderRegistry.registerLoader(new TBLModelLoader());
				
		for(Object o : objects)
			MinecraftForge.EVENT_BUS.register(o);
	}
	
}
