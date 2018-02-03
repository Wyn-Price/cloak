package com.wynprice.cloak.client.rendering.models.quads;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ExternalBakedQuad extends BakedQuad {

	public ExternalBakedQuad(ResourceLocation location, int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn,
			boolean applyDiffuseLighting, VertexFormat format) 
	{
		super(vertexDataIn, tintIndexIn, faceIn, spriteIn, applyDiffuseLighting, format);
		this.location = location;
	}
	
	public ExternalBakedQuad(ResourceLocation location, BakedQuad quad) 
	{
		this(location, quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
	}
	
	@Override
	public int[] getVertexData() 
	{
		return super.getVertexData();
	}
	
	protected final ResourceLocation location;
	
	public ResourceLocation getLocation() {
		return location;
	}

}
