package com.wynprice.brl.addons.tblloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class TBLModelLoader implements ICustomModelLoader
{

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) 
	{
		boolean isDirect = true;
		if(!modelLocation.getResourcePath().endsWith(".tbl"))
		{				
			try
			{
				Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath() + ".tbl")).getInputStream();
			}
			catch (IOException e2) 
			{
				try
				{
					JsonParser paraser = new JsonParser();
					JsonObject json = paraser.parse(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath() + ".ptbl")).getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
					if(json.has("tbl-parent"))
					{
						ResourceLocation loc = new ResourceLocation(json.get("tbl-parent").getAsString());
						isDirect = accepts(new ResourceLocation(loc.getResourceDomain(), "models/" + loc.getResourcePath()));
					}
				}
				catch (IOException e) 
				{
					isDirect = false;
				}
				
			}
		}
		else
		{
			try
			{
				Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath())).getInputStream();
			}
			catch (IOException e2) 
			{
				isDirect = false;
			}
		}
			
		return isDirect;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception 
	{
		return TBLZipHandler.getZipFile(modelLocation);
	}

}
