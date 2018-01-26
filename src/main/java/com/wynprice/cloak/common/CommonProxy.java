package com.wynprice.cloak.common;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event)
	{
		CloakItems.preInit();
	}
	
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	
	
	
	private void registerTileEntities()
	{
		Class[] tileEntityClasses = {
    			TileEntityCloakBlock.class
    	};
    	for(Class clas : tileEntityClasses)
    		GameRegistry.registerTileEntity(clas, CloakMod.MODID + clas.getSimpleName());
	}
}
