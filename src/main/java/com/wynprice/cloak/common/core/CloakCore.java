package com.wynprice.cloak.common.core;

import java.util.Map;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "Cloak-Core")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"com.wynprice.cloak.common.core"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class CloakCore implements IFMLLoadingPlugin {

    public static boolean isDebofEnabled = false;

    public CloakCore() {
        FMLLog.info("[Cloak-Core] Initialized.");
    }

    @Override
    public String[] getASMTransformerClass() {//ADD compat with secretroomsmod
        return new String[] 
        		{
        				"com.wynprice.cloak.common.core.UVTransformer"
        		};
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
    public void injectData(Map<String, Object> data) {
        isDebofEnabled = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}