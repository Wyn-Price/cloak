package com.wynprice.cloak.client.rendering.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.common.registries.CloakBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import scala.collection.parallel.ParIterableLike.Min;

public class BasicCloakingMachineModel extends CloakedModel
{

	public BasicCloakingMachineModel(IBlockState modelState, IBlockState renderState) {
		super(modelState, renderState);
	}	
		
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{		
		ArrayList<BakedQuad> list = Lists.newArrayList();
		IBakedModel renderModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState);
		double angle = (System.currentTimeMillis() / 500D) % 360;
		HashMap<BakedQuad, IBlockState> localParentMap = new HashMap<>();
		int reset = 0;
		for(BakedQuad quad : super.getQuads(state, side, rand))
		{
			int[] vertexData = new int[quad.getVertexData().length];
			System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
			for(int j = 0; j < 4; j++)
			{
				int i = (vertexData.length / 4) * j;
				float size = 5 / 16f;
				float xOrigin = Float.intBitsToFloat(vertexData[i + 0]) * size + 0.5f - size / 2f;
				float zOrigin = Float.intBitsToFloat(vertexData[i + 2]) * size + 0.5f - size / 2f;
				vertexData[i + 0] = Float.floatToRawIntBits((float) (0.5f + (xOrigin-0.5f)*Math.cos(angle) - (zOrigin-0.5f)*Math.sin(angle)));
				vertexData[i + 1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(vertexData[i + 1]) * size + 1f));
				vertexData[i + 2] = Float.floatToRawIntBits((float) (0.5f + (xOrigin-0.5f)*Math.sin(angle) + (zOrigin-0.5f)*Math.cos(angle)));
			}
			
			BakedQuad newQuad = new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
			if(quad instanceof ExternalBakedQuad)
				newQuad = new ExternalBakedQuad(((ExternalBakedQuad)quad).getLocation(), newQuad);
			
			localParentMap.put(newQuad, currentRenderingMap.get(quad));
			list.add(newQuad);
		}
		currentRenderingMap.clear();
		currentRenderingMap.putAll(localParentMap);
		return list;
	}
	
	
	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

}
