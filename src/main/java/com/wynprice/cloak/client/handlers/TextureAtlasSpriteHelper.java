package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.CloakMod;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureAtlasSpriteHelper 
{
	public static final TextureAtlasSprite FULL_SPRITE = new FakeAtlasSprite();
	
	
	private static class FakeAtlasSprite extends TextureAtlasSprite
	{

		public FakeAtlasSprite() 
		{
			super(CloakMod.MODID + "faketexture");
			this.setIconWidth(1);
			this.setIconHeight(1);
			this.initSprite(1, 1, 0, 0, false);
		}
		
	}
	
}
