package com.wynprice.brl;

import com.wynprice.brl.events.PostTextureStitch;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;

@Mod(modid = "brl", name = "BetterRenderLib", version = "0.1")
public class BrlEventSubscriber 
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Object[] objects = 
			{
					new PostTextureStitch()
			};
		
		for(Object o : objects)
			MinecraftForge.EVENT_BUS.register(o);
	}
}
