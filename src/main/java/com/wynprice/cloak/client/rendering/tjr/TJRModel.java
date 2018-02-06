package com.wynprice.cloak.client.rendering.tjr;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.JsonUtils;

/**
 * Class to hold all the infomation about the model, the texturesize, the scale, stuff like that
 * @see TJRCube
 * @author Wyn Price
 *
 */
public class TJRModel 
{
	private final String modelName;
	private final String authorName;
	private final int projVersoin;
	private final int textureWidth;
	private final int textureHeight;
	private final Vector3f scale;
	private final TJRCube[] cubes;

	
	private TJRModel(String modelName, String authorName, int projVersion, int textureWidth, int textureHeight, Vector3f scale, TJRCube... cubes)
	{
		this.modelName = modelName;
		this.authorName = authorName;
		this.projVersoin = projVersion;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.scale = scale;
		this.cubes = cubes;

		for(TJRCube object : cubes)
			object.setTjrmodel(this);
	}
	
	public void render(float scale)
	{
		GlStateManager.rotate(180, 0, 0, 1); //not sure why this needs to be here. I think an issue with .tbl exporting?
		GlStateManager.scale(this.scale.x / 16f, this.scale.y / 16f, this.scale.z / 16f);
		for(TJRCube object : cubes)
			object.render(scale);
		GlStateManager.scale(this.scale.x * 16f, this.scale.y * 16f, this.scale.z * 16f);
		GlStateManager.rotate(-180, 0, 0, 1);

		
	}
	
	public int getTextureHeight() {
		return textureHeight;
	}
	
	public int getTextureWidth() {
		return textureWidth;
	}
	
	
	static class Deserializer implements JsonDeserializer<TJRModel>
	{

		@Override
		public TJRModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException 
		{
			JsonObject object = json.getAsJsonObject();            
			return new TJRModel(
					object.get("modelName").getAsString(),
					object.get("authorName").getAsString(),
					object.get("projVersion").getAsInt(),
					object.get("textureWidth").getAsInt(),
					object.get("textureHeight").getAsInt(),
					TJRCube.Deserializer.getVec3f(object, "scale"), 
					getRenderObjects(object, context));
		}
		
		private TJRCube[] getRenderObjects(JsonObject object, JsonDeserializationContext context)
		{
			 TJRCube[] aren = new TJRCube[0];
			 if(object.has("cubes"))
	         {
				JsonArray jsonarray = JsonUtils.getJsonArray(object, "cubes");
	            aren = new TJRCube[jsonarray.size()];

	            for(int i = 0; i < jsonarray.size(); i++)
	            {
	            	aren[i] = (context.deserialize(jsonarray.get(i), TJRCube.class));
	            }
	         }
			 return aren;
		}
		
	}
}
