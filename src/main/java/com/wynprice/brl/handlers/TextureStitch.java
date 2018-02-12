package com.wynprice.brl.handlers;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_HEIGHT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_INTERNAL_FORMAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.glGetTexLevelParameteri;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.wynprice.brl.addons.plastic.BufferedPlastic;
import com.wynprice.brl.addons.tblloader.TBLZipHandler;
import com.wynprice.brl.addons.tblloader.TBLModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitch 
{
	
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		for(TBLModel model : TBLZipHandler.LOADED_MODELS)
			model.registerTexture(event.getMap());
	}
	
	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		
		BufferedPlastic.setTextureID(-1);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		int format = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT);
		int width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
		int height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
		
		int channels = 4;
		if (format == GL_RGB)
		    channels = 3;

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * channels);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		glGetTexImage(GL_TEXTURE_2D, 0, format, GL_UNSIGNED_BYTE, buffer);

		for (int x = 0; x < width; ++x) {
		    for (int y = 0; y < height; ++y) {
		        int i = (x + y * width) * channels;

		        int r = buffer.get(i + 0) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        int a = 255;
		        if (channels == 4)
		            a = buffer.get(i + 3) & 0xFF;

		        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		
		BufferedPlastic.image = image;
		
	}
}
