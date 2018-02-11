package com.wynprice.brl.addons.tblloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class TBLRegistry 
{
	
	public static final Gson SERIALIZER = (new GsonBuilder())
			.registerTypeAdapter(TBLModel.class, new TBLModel.Deserializer())
			.registerTypeAdapter(TBLCube.class, new TBLCube.Deserializer())
			.create();
	
	
	public static TBLModel getModel(ResourceLocation location)
	{
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
			return getModel(new InputStreamReader(stream, StandardCharsets.UTF_8));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static TBLModel getModel(Reader reader)
	{
		return JsonUtils.gsonDeserialize(SERIALIZER, reader, TBLModel.class, false);
	}
	
	private static IBakedModel missingNo;
	
	public static void setMissingNo(IBakedModel missingNo) 
	{
		if(TBLRegistry.missingNo == null)
			TBLRegistry.missingNo = missingNo;
	}
	
	public static IBakedModel getMissingNo() {
		return missingNo;
	}
}
