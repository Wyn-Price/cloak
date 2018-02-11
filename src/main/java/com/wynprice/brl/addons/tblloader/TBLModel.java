package com.wynprice.brl.addons.tblloader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import javax.imageio.ImageIO;

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
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

/**
 * Class to hold all the infomation about the model, the texturesize, the scale, stuff like that
 * @see TBLCube
 * @author Wyn Price
 *
 */
public class TBLModel implements IModel
{
	private final String modelName;
	private final String authorName;
	private final int projVersoin;
	private final int textureWidth;
	private final int textureHeight;
	private final Vector3f scale;
	private final TBLCube[] cubes;
	
	private BufferedImage image;
	private ResourceLocation location;
	private TextureAtlasSprite sprite;
	
	public static final HashMap<String, ResourceLocation> RESOURCECACHEMAP = new HashMap<>();
	
	private TBLModel(String modelName, String authorName, int projVersion, int textureWidth, int textureHeight, Vector3f scale, TBLCube... cubes)
	{
		this.modelName = modelName;
		this.authorName = authorName;
		this.projVersoin = projVersion;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.scale = scale;
		this.cubes = cubes;

		for(TBLCube object : cubes)
		{
			object.setTjrmodel(null, this);
		}
	}
	
	public void setImage(BufferedImage image) 
	{
		this.image = image;
		if(!RESOURCECACHEMAP.containsKey(modelName))	
		{
			ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(this.modelName, new DynamicTexture(image));
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "png", byteArrayOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
			RESOURCECACHEMAP.put(modelName, new TBLImageLocation(location, byteArrayOut));
		}
		location = RESOURCECACHEMAP.get(modelName);

	}
	
	public void render(float scale)
	{
		GlStateManager.rotate(180, 0, 0, 1); //not sure why this needs to be here. I think an issue with .tbl exporting?
		GlStateManager.scale(this.scale.x / 16f, this.scale.y / 16f, this.scale.z / 16f);
		for(TBLCube object : cubes)
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
	
	/**
	 * Converts the {@link TBLModel} to an {@link IBakedModel}.
	 * Uses the texture set during texture stitching, if there is one
	 * @return the converted {@link IBakedModel}
	 */
	public IBakedModel bake()
	{
		return getBakedModel(sprite);
	}
	
	/**
	 * Converts the {@link TBLModel} to an {@link IBakedModel}.
	 * @param sprite The sprite to be rendered with, leave null if you plan to do {@link TextureManager#bindTexture(net.minecraft.util.ResourceLocation)} before rendering
	 * @return the converted {@link IBakedModel}
	 */
	public IBakedModel getBakedModel(TextureAtlasSprite sprite) 
	{
		ArrayList<TBLCube> allList = new ArrayList<>();
		for(TBLCube cube : cubes)
		{
			allList.add(cube);
			cube.addChildren(allList);
			cube.globalizeTransform(false);
		}
		ArrayList<BakedQuad> quadList = new ArrayList<>();
		for(TBLCube cube : allList)
			for(EnumFacing face : EnumFacing.VALUES)
			{				
				Vector3f pos1 = 
						Vector3f.add
						(
							Vector3f.add
							(
									cube.global_position, cube.offset, null),
							new Vector3f(8, 8, 8), null
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
										cube.dimensions, null
								), 
								new Vector3f(8, 8, 8), null
						);
						
				
				BlockFaceUV faceUV = new BlockFaceUV(new float[]{0, 0, 16, 16}, 0);
				
				switch(face.getAxis())
				{
					case X:
						faceUV = new BlockFaceUV(new float[]{0, 0, cube.dimensions.x, cube.dimensions.y}, 0);
						break;
					case Y:
						faceUV = new BlockFaceUV(new float[]{0, 0, cube.dimensions.x, cube.dimensions.z}, 0);
						break;
					case Z:
						faceUV = new BlockFaceUV(new float[]{0, 0, cube.dimensions.z, cube.dimensions.y}, 0);
						break;
				}
				
				BakedQuad newQuad = new FaceBakery().makeBakedQuad
						(
								pos1,
								pos2,
								new BlockPartFace
								(
										face,
										0,
										"tjr:blockmodel",
										faceUV
								), 
								this.sprite,
								face,
								ModelRotation.X0_Y0,
								new BlockPartRotation
								(
										new Vector3f(0, 0, 0), 
										Axis.Y, 
										0, 
										false),
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
		        	this.rotateQuad(vector3f, cube.global_position, cube.global_rotaion); 


		        	Vector3f rotationPoint = new Vector3f(0, 8, 0);
		        	this.rotateQuad(rotationPoint, new Vector3f(), cube.getOriginalParent().global_rotaion);
			        this.rotateQuad(vector3f, rotationPoint, new Vector3f(180, 0, 0)); //Tabula measures y coordinates top down, minecraft measures them top up, so just flip the model here
			        
			        int minV = face.getAxis() == Axis.Y ? 0 : (int) cube.dimensions.z;
			        int minU = 0;
			        
			        if(face != EnumFacing.EAST)	//Probally an easier way to do this. Eh, it works
			        {
			        	minU += cube.dimensions.z;
			        	
				        if(face != EnumFacing.SOUTH && face != EnumFacing.DOWN)
				        {
				        	minU += cube.dimensions.x;
				        
					        if(face != EnumFacing.EAST && face != EnumFacing.UP)
					        	minU += cube.dimensions.z;
				        }
			        }
			        	
				
				
			        int l = (newQuad.getVertexData().length / 4) * i;
			        newQuad.getVertexData()[l + 0] = Float.floatToRawIntBits(vector3f.x);
			        newQuad.getVertexData()[l + 1] = Float.floatToRawIntBits(vector3f.y);
			        newQuad.getVertexData()[l + 2] = Float.floatToRawIntBits(vector3f.z);
			        
			        float relU = (new float[]{1f, 1f, 0f, 0f}[i] * faceUV.uvs[2] + minU + cube.textureOffX) / this.textureWidth;
			        float relV = (new float[]{1f, 0f, 0f, 1f}[i] * faceUV.uvs[3] + minV + cube.textureOffY) / this.textureHeight;
			        
			        if(sprite != null)
			        {			        	
			        	newQuad.getVertexData()[l + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(relU * 16));
			        	newQuad.getVertexData()[l + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(relV * 16));
			        }
			        else
			        {
				        newQuad.getVertexData()[l + 4] = Float.floatToRawIntBits(relU);
				        newQuad.getVertexData()[l + 5] = Float.floatToRawIntBits(relV);
			        }
				}
				
				
				
				quadList.add(new TBLUVQuad(newQuad, faceUV));
			}
		TBLBakedModel model =  new TBLBakedModel(sprite, quadList);
		return model;
	}
	
	public void rotateQuad(Vector3f quadPos, Vector3f origin, Vector3f vecAngels)
	{
		origin = new Vector3f((origin.x + 8f) / 16f, (origin.y + 8f) / 16f, (origin.z + 8f) / 16f);
		float[] angels = {vecAngels.x, vecAngels.y, vecAngels.z};
		for(int i = 0; i < 3; i++)
		{
			Matrix4f matrix4f = new Matrix4f();
			matrix4f.setIdentity();
	        Matrix4f.rotate(angels[i] * 0.017453292F, new Vector3f(i == 0? 1 : 0, i == 1? 1 : 0, i == 2? 1 : 0), matrix4f, matrix4f);
	        Vector4f vector4f = new Vector4f(quadPos.x - origin.x, quadPos.y - origin.y, quadPos.z - origin.z, 1.0F);
	        Matrix4f.transform(matrix4f, vector4f, vector4f);
	        quadPos.set(vector4f.x + origin.x, vector4f.y + origin.y, vector4f.z + origin.z);
		}
	}
	
	static class Deserializer implements JsonDeserializer<TBLModel>
	{

		@Override
		public TBLModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException 
		{
			JsonObject object = json.getAsJsonObject();            
			return new TBLModel(
					object.get("modelName").getAsString(),
					object.get("authorName").getAsString(),
					object.get("projVersion").getAsInt(),
					object.get("textureWidth").getAsInt(),
					object.get("textureHeight").getAsInt(),
					TBLCube.Deserializer.getVec3f(object, "scale"), 
					getRenderObjects(object, context));
		}
		
		private TBLCube[] getRenderObjects(JsonObject object, JsonDeserializationContext context)
		{
			 TBLCube[] aren = new TBLCube[0];
			 if(object.has("cubes"))
	         {
				JsonArray jsonarray = JsonUtils.getJsonArray(object, "cubes");
	            aren = new TBLCube[jsonarray.size()];

	            for(int i = 0; i < jsonarray.size(); i++)
	            {
	            	aren[i] = (context.deserialize(jsonarray.get(i), TBLCube.class));
	            }
	         }
			 return aren;
		}
		
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) 
	{
		return bake();
	}
	
	public void registerTexture(TextureMap map)
	{
		this.sprite = map.registerSprite(location);
	}
}
