package com.wynprice.cloak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wynprice.cloak.common.CloakCreativeTab;
import com.wynprice.cloak.common.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CloakMod.MODID, name = CloakMod.NAME, version = CloakMod.VERSION)
public class CloakMod
{
    public static final String MODID = "cloak";
    public static final String NAME = "Cloak";
    public static final String VERSION = "0.0.1";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final CreativeTabs TAB = new CloakCreativeTab();
    
    @Instance(MODID)
    public static CloakMod instance;
    
    @SidedProxy(clientSide="com.wynprice.cloak.client.ClientProxy", serverSide="com.wynprice.cloak.server.ServerProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
}
