package com.wynprice.cloak.client.rendering.models;

import java.util.List;

import com.google.common.collect.Lists;
import com.wynprice.cloak.common.core.UVTransformer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class TestExternalImageMode extends BaseModelProxy {

	public TestExternalImageMode(IBakedModel oldModel) {
		super(oldModel);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> list =  Lists.newArrayList();
		for(BakedQuad quad : super.getQuads(state, side, rand))
		{
			int[] vertexData = new int[quad.getVertexData().length];
			System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
			BlockFaceUV faceUV = UVTransformer.getUV(quad.getVertexData());
			if(quad.getFace() == EnumFacing.UP) faceUV = new BlockFaceUV(faceUV.uvs, 1); //Who knows why this has to be here. It just does
			for(int j = 0; j < 4; j++)
			{
				int i = (vertexData.length / 4) * j;
				vertexData[i + 4] = Float.floatToRawIntBits(Lists.newArrayList(faceUV.uvs[1], faceUV.uvs[3], faceUV.uvs[3], faceUV.uvs[1]).get(j) / 16f);
				vertexData[i + 5] = Float.floatToRawIntBits(Lists.newArrayList(faceUV.uvs[0], faceUV.uvs[0], faceUV.uvs[2], faceUV.uvs[2]).get(j) / 16f);
			}
			list.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
		}
		return list;
	}

}
