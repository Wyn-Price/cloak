package com.wynprice.brl.core;

import java.util.Map;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "BRL CORE")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"com.wynprice.brl.core"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class BetterRenderCore implements IFMLLoadingPlugin {

    public static boolean isDebofEnabled = false;

    public BetterRenderCore() {
        FMLLog.info("[BetterRenderCore] Loaded core.");
    }

    @Override
    public String[] getASMTransformerClass() 
    {
    	String[] stringList = new String[] 
        		{
        				//Main
        				"com.wynprice.brl.core.BlockRendererDispatcherTransformer",
        				"com.wynprice.brl.core.RegionRenderCacheBuilderTransformer",
        				"com.wynprice.brl.core.WorldVertexBufferUploaderTransformer",
        				
        				//Addons
        				"com.wynprice.brl.addons.plastic.ForgeBlockModelRendererTransformer",
        				"com.wynprice.brl.addons.tblloader.FallbackResourceManagerTransformer"

        		};
    	
    	return stringList;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) 
    {
        isDebofEnabled = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}