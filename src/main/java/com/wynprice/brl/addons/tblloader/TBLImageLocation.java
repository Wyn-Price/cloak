package com.wynprice.brl.addons.tblloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

public class TBLImageLocation extends ResourceLocation
{
	
	public TBLImageLocation(ResourceLocation location, ByteArrayOutputStream actualStream) 
	{
		super(location.getResourceDomain(), location.getResourcePath());
		STREAMS.put(this, actualStream);
	}
	
	private static final HashMap<ResourceLocation, ByteArrayOutputStream> STREAMS = new HashMap<>();
	
	
	public static InputStream getStream(ResourceLocation location)
	{
		location = new ResourceLocation(location.getResourceDomain(), location.getResourcePath()); //Make sure not another class
		for(ResourceLocation key : STREAMS.keySet())
			if(new ResourceLocation(key.getResourceDomain(), "textures/" + key.getResourcePath() + ".png").equals(location))
				return new ByteArrayInputStream(STREAMS.get(key).toByteArray());

		return null;
	}
	
	public static boolean hasStream(ResourceLocation location)
	{
		return getStream(location) != null;
	}
}
