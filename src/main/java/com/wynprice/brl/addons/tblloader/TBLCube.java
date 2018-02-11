package com.wynprice.brl.addons.tblloader;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.JsonUtils;

/**
 * Holds all the infomation about each object being rendered, like its size, position, scale, children
 * @author Wyn Price
 *
 */
public class TBLCube 
{
	public final String name;
	public final Vector3f dimensions;
	public final Vector3f position;
	public final Vector3f offset;
	public final Vector3f rotation;
	public final Vector3f scale;
	public final int textureOffX;
	public final int textureOffY;
	public final boolean mirrorTX;
	public final TBLCube[] children;
	
	public Vector3f global_position;
	public Vector3f global_rotaion;

	private TBLModel tjrmodel;
	
	private int displayList;
    private boolean hasDrawn;
	
	private TBLCube(String name, Vector3f dimensions, Vector3f position,
			Vector3f offset, Vector3f rotation, Vector3f scale, int textureOffX, int textureOffY,
			boolean mirrorTX, TBLCube... children) 
	{
		this.name = name;
		this.dimensions = new Vector3f(dimensions.x, dimensions.y, dimensions.z);
		this.position = position;
		this.offset = offset;
		this.rotation = rotation;
		this.scale = scale;
		this.textureOffX = textureOffX;
		this.textureOffY = textureOffY;
		this.mirrorTX = mirrorTX;
		this.children = children;
		
		this.global_position = new Vector3f();
		this.global_rotaion = new Vector3f();
	}
	
	public void render(float scale)
	{
		if(!hasDrawn)
			draw(scale);
		
		Vector3f offset = new  Vector3f(-8, -24, 8);
				
		GlStateManager.pushMatrix();
        GlStateManager.translate(this.position.x * scale, this.position.y * scale, this.position.z * scale);
        GlStateManager.translate(offset.x * scale, offset.y * scale, offset.z * scale);
//        GlStateManager.rotate(this.rotation.z, 0.0F, 0.0F, 1.0F);
//        GlStateManager.rotate(this.rotation.y, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(this.rotation.x, 1.0F, 0.0F, 0.0F);
        GlStateManager.callList(this.displayList);
        GlStateManager.translate(-offset.x * scale, -offset.y * scale, -offset.z * scale);
        for(int k = 0; k < this.children.length; ++k)
            this.children[k].render(scale);
        

        GlStateManager.popMatrix();
	}
	
	public void addChildren(ArrayList<TBLCube> list)
	{
		for(TBLCube cube : children)
		{
			list.add(cube);
			cube.addChildren(list);
		}
	}
	
	private void draw(float scale)
	{
		 this.displayList = GLAllocation.generateDisplayLists(1);
	     GlStateManager.glNewList(this.displayList, 4864);
	     BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();	     	    		 
	     new ModelBox(new ModelRenderer(new ModelBase(){}, this.textureOffX, this.textureOffY).setTextureSize(tjrmodel.getTextureWidth(), tjrmodel.getTextureHeight()), textureOffX, textureOffY, offset.x, offset.y, offset.z, (int)dimensions.x, (int)dimensions.y, (int)dimensions.z, 0.0F, mirrorTX)
	     .render(bufferbuilder, scale);
	     GlStateManager.glEndList();
	     this.hasDrawn = true;
	}
	
	public void globalizeTransform(boolean isChild)
	{
		
		if(!isChild)
		{
			this.global_position = new Vector3f(position);
			this.global_rotaion = new Vector3f(rotation);
		}

		for(TBLCube child : children)
		{
			Vector3f.add(this.global_position, child.position, child.global_position);
			Vector3f.add(this.global_rotaion, child.rotation, child.global_rotaion);

			child.globalizeTransform(true);
		}
	}
	
	static class Deserializer implements JsonDeserializer<TBLCube>
	{

		@Override
		public TBLCube deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException 
		{
			JsonObject object = json.getAsJsonObject();
			return new TBLCube(
					object.get("name").getAsString(), 
					getVec3f(object, "dimensions"), 
					getVec3f(object, "position"),
					getVec3f(object, "offset"), 
					getVec3f(object, "rotation"), 
					getVec3f(object, "scale"), 
					getTextureOffset(object)[0], 
					getTextureOffset(object)[1],
					object.get("txMirror").getAsBoolean(), 
					getChildren(object, context));
		}
		
		public static Vector3f getVec3f(JsonObject object, String name)
		{
			if (!object.has(name))
            {
                return null;
            }
            else
            {
                JsonArray jsonarray = JsonUtils.getJsonArray(object, name);

                if (jsonarray.size() != 3)
                {
                    throw new JsonParseException("Expected 3 values, found: " + jsonarray.size());
                }
                else
                {
                    float[] afloat = new float[3];

                    for (int i = 0; i < afloat.length; ++i)
                    {
                        afloat[i] = JsonUtils.getFloat(jsonarray.get(i), "uv[" + i + "]");
                    }

                    return new Vector3f(afloat[0], afloat[1], afloat[2]);
                }
            }
		}
		
		private int[] getTextureOffset(JsonObject object)
		{
			int[] aint = new int[2];
			
			if (!object.has("txOffset"))
            {
                return new int[]{0, 0};
            }
            else
            {
                JsonArray jsonarray = JsonUtils.getJsonArray(object, "txOffset");

                if (jsonarray.size() != 2)
                {
                    throw new JsonParseException("Expected 2 values, found: " + jsonarray.size());
                }
                else
                {
                    for (int i = 0; i < aint.length; ++i)
                    {
                    	aint[i] = JsonUtils.getInt(jsonarray.get(i), "uv[" + i + "]");
                    }
                }
            }
			
			return aint;
		}
		
		private TBLCube[] getChildren(JsonObject object, JsonDeserializationContext context)
		{
			if(!object.has("children"))
				return new TBLCube[0];
			
			JsonArray jsonarray = JsonUtils.getJsonArray(object, "children");
			TBLCube[] jaint = new TBLCube[jsonarray.size()];
			
			for (int i = 0; i < jaint.length; ++i)
            {
            	jaint[i] = context.deserialize(jsonarray.get(i), TBLCube.class);
            }
			
			return jaint;
		}
		
	}
	
	
	private TBLCube parent;
	
	public TBLCube getOriginalParent() {
		return parent == null ? this : parent.getOriginalParent();
	}
	
	public void setTjrmodel(TBLCube parent, TBLModel tjrmodel) 
	{
		this.tjrmodel = tjrmodel;
		for(TBLCube child : children)
			child.setTjrmodel(this, this.tjrmodel);
		this.parent = parent;
	}
	
	
}
