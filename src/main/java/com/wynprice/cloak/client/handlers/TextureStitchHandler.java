package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.CloakMod;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitchHandler 
{
	
	public static TextureAtlasSprite blockrender_overlay;
	
	@SubscribeEvent
	public void onTextureStitchedPre(TextureStitchEvent.Pre event)
	{
		blockrender_overlay = event.getMap().registerSprite(new ResourceLocation(CloakMod.MODID, "gui/blockrender_overlay"));
	}
}
