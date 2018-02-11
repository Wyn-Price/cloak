package com.wynprice.cloak.common;

import org.apache.logging.log4j.core.util.Loader;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.handlers.CloakGUIHandler;
import com.wynprice.cloak.common.handlers.UpdateHandler;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;
import com.wynprice.cloak.server.handlers.ServerExternalImageHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	
	public static boolean isOptifine = false;
	
	public void preInit(FMLPreInitializationEvent event)
	{
		CloakItems.preInit();
		CloakBlocks.preInit();
		CloakNetwork.preInit();
		
		registerTileEntities();
		registerHandlers();
		
		isOptifine = Loader.isClassAvailable("ChunkCacheOF");
	}
	
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(CloakMod.instance, new CloakGUIHandler());
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	private void registerHandlers()
	{
		Object[] handlers = 
			{
				new UpdateHandler(),
				new ServerExternalImageHandler()

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
