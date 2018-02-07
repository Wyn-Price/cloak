package com.wynprice.cloak.client.rendering.tjr;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
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
		{
			object.setTjrmodel(this);
		}
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
	
	public IBakedModel getBakedModel() 
	{
		ArrayList<TJRCube> allList = new ArrayList<>();
		for(TJRCube cube : cubes)
		{
			allList.add(cube);
			cube.addChildren(allList);
			cube.globalizeTransform(false);
		}
		ArrayList<BakedQuad> quadList = new ArrayList<>();
		for(TJRCube cube : allList)
			for(EnumFacing face : EnumFacing.VALUES)
			{				
				Vector3f pos1 = 
						Vector3f.add
						(
								Vector3f.add
								(
									Vector3f.add
									(
											cube.global_position, cube.offset, null),
									new Vector3f(8, 8, 8), null
								),
								new Vector3f(0, 0, 0), null
						);
				
				Vector3f pos2 = 
						Vector3f.add
						(
								Vector3f.add
								(
										Vector3f.add
										(
												cube.global_position, cube.offset, null
										),
										new Vector3f(cube.dimensions.x, cube.dimensions.y, cube.dimensions.z), null
								), 
								new Vector3f(8, 8, 8), null
						);
						
				BakedQuad newQuad = new FaceBakery().makeBakedQuad
						(
								pos1,
								pos2,
								new BlockPartFace(face, 0, "tjr:blockmodel", new BlockFaceUV(new float[]{0, 0, 16, 16}, 1)), 
								Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.STONE.getDefaultState()),
								face,
								ModelRotation.X0_Y0,
								new BlockPartRotation(new Vector3f(0, 0, 0), Axis.Y, 0, false),
								false, 
								false
								);
				
				
				float[] afloat = new float[EnumFacing.values().length];
		        afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0F;
		        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0F;
		        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0F;
		        afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0F;
		        afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0F;
		        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0F;
				
				for(int i = 0; i < 4; i++)
				{	
			        VertexInformation vi = EnumFaceDirection.getFacing(face).getVertexInformation(i);
			        Vector3f vector3f = new Vector3f(afloat[vi.xIndex], afloat[vi.yIndex], afloat[vi.zIndex]);
			        this.rotateQuad(vector3f, new Vector3f(8, 16, 8), new Vector3f(180, 0, 0)); //For some reason, taulba flips the model during export.
			        int l = (newQuad.getVertexData().length / 4) * i;
			        newQuad.getVertexData()[l + 0] = Float.floatToRawIntBits(vector3f.x);
			        newQuad.getVertexData()[l + 1] = Float.floatToRawIntBits(vector3f.y);
			        newQuad.getVertexData()[l + 2] = Float.floatToRawIntBits(vector3f.z);
				}
				
				
				
				quadList.add(newQuad);
			}
		return new TJRBakedModel(quadList);
	}
	
	public void rotateQuad(Vector3f quadPos, Vector3f origin, Vector3f angles)
	{
		origin = new Vector3f(origin.x / 16f, origin.y / 16f, origin.z / 16f);
		
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.setIdentity();
        Matrix4f.rotate(angles.x * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
        Matrix4f.rotate(angles.y * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f, matrix4f);
        Matrix4f.rotate(angles.z * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4f, matrix4f);
        Vector4f vector4f = new Vector4f(quadPos.x - origin.x, quadPos.y - origin.y, quadPos.z - origin.z, 1.0F);
        Matrix4f.transform(matrix4f, vector4f, vector4f);
        quadPos.set(vector4f.x + origin.x, vector4f.y + origin.y, vector4f.z + origin.z);
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
