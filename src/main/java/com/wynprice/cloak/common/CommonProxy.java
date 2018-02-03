package com.wynprice.cloak.common;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.handlers.ModelBakeHandler;
import com.wynprice.cloak.client.handlers.ParticleHandler;
import com.wynprice.cloak.client.handlers.TextureStitchHandler;
import com.wynprice.cloak.common.handlers.CloakGUIHandler;
import com.wynprice.cloak.common.handlers.ExternalImageHandler;
import com.wynprice.cloak.common.handlers.UpdateHandler;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event)
	{
		CloakItems.preInit();
		CloakBlocks.preInit();
		CloakNetwork.preInit();
		
		registerTileEntities();
		registerHandlers();
	}
	
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(CloakMod.instance, new CloakGUIHandler());
		ExternalImageHandler.init();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	private void registerHandlers()
	{
		Object[] handlers = 
			{
				new UpdateHandler()
			};
		
		for(Object o : handlers)
    		MinecraftForge.EVENT_BUS.register(o);
	}
	
	private void registerTileEntities()
	{
		Class[] tileEntityClasses = {
    			TileEntityCloakBlock.class,
    			TileEntityCloakingMachine.class
    	};
    	for(Class clas : tileEntityClasses)
    		GameRegistry.registerTileEntity(clas, CloakMod.MODID + clas.getSimpleName());
	}
}
