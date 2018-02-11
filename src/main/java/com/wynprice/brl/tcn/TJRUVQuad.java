package com.wynprice.brl.tcn;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

public class TJRUVQuad extends BakedQuad
{
	
	private final BlockFaceUV faceUV;

	public TJRUVQuad(BakedQuad quad, BlockFaceUV faceUV) {
		super(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
		this.faceUV = faceUV;
	}
	
	public BlockFaceUV getFaceUV() {
		return faceUV;
	}

}
