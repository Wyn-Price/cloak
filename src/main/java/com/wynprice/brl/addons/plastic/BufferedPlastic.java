package com.wynprice.brl.addons.plastic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;

public class BufferedPlastic 
{	
	private final static HashMap<Block, Integer> IMAGE_COLOR_MAP = new HashMap<>();
	
	public static BufferedImage image;
	
	
	public static boolean plastic = false;

	private static int textureID = -1;
	
	public static int getTextureID() 
	{
		if(textureID == -1)
		{
	        textureID = GL11.glGenTextures();
	        int[] pixels = new int[image.getWidth() * image.getHeight()];
	        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

	        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
	        
	        for(int y = 0; y < image.getHeight(); y++){
	            for(int x = 0; x < image.getWidth(); x++){
	                int pixel = pixels[y * image.getWidth() + x];
	                buffer.put((byte) 0xFF); 
	                buffer.put((byte) 0xFF);
	                buffer.put((byte) 0xFF);
	                byte alpha = (byte) ((pixel >> 24) & 0xFF);
	                buffer.put((byte) (alpha == 0 ? 0 : 255)); 
	            }
	        }

	        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

	        // You now have a ByteBuffer filled with the color data of each pixel.
	        // Now just create a texture ID and bind it. Then you can load it using 
	        // whatever OpenGL method you want, for example:

	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID
	        
	        //Setup wrap mode
//	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	        //Setup texture scaling filtering
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	        
	        //Send texel data to OpenGL
	        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	      
		}
		
		return textureID;
	}
	
	public static void setTextureID(int textureID) {
		BufferedPlastic.textureID = textureID;
	}
		
	public static int getImageColor(TextureAtlasSprite sprite)
	{
		ArrayList pixels = new ArrayList<>();
		int redBucket = 0;
		int greenBucket = 0;
		int blueBucket = 0;
		int pixelCount = 0;
		
		for(int x = (int) (sprite.getMinU() * image.getWidth()); x < sprite.getMaxU() * image.getWidth(); x++)
			for(int y = (int) (sprite.getMinV() * image.getHeight()); y < sprite.getMaxV() * image.getHeight(); y++)
			{
				Color color = new Color(image.getRGB(x, y));
				if(color.getAlpha() == 0)
					continue;
				pixelCount++;
				redBucket += color.getRed();
				greenBucket += color.getGreen();
				blueBucket += color.getBlue();
			}
		

		return new Color
		(
				redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount
		)
		.getRGB();
	}
}
