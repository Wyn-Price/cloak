package com.wynprice.cloak.client.rendering.tjr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import scala.util.parsing.json.JSON;

public class TJR 
{
	
	public static final Gson SERIALIZER = (new GsonBuilder())
			.registerTypeAdapter(TJRModel.class, new TJRModel.Deserializer())
			.registerTypeAdapter(TJRCube.class, new TJRCube.Deserializer())
			.create();
	
	
	public static TJRModel getModel(ResourceLocation location)
	{
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
			return JsonUtils.gsonDeserialize(SERIALIZER, new InputStreamReader(stream, StandardCharsets.UTF_8), TJRModel.class, false);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}