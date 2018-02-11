package com.wynprice.brl.addons.tblloader;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;

public class TBLUVQuad extends BakedQuad
{
	
	private final BlockFaceUV faceUV;

	public TBLUVQuad(BakedQuad quad, BlockFaceUV faceUV) {
		super(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
		this.faceUV = faceUV;
	}
	
	public BlockFaceUV getFaceUV() {
		return faceUV;
	}

}
