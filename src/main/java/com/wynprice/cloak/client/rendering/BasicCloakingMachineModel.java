package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

public class BasicCloakingMachineModel extends CloakedModel
{

	public BasicCloakingMachineModel(IBlockState modelState, IBlockState renderState) {
		super(modelState, renderState);
	}	
	
	private static long waitTimer;
	
	private static double timer;
	private static long prevTime;
	
	private static final double TIME = 10;
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		int time = 15;
		int weight = 5000;
		
		ArrayList<BakedQuad> list = Lists.newArrayList();
		IBakedModel renderModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState);
		double angle = (System.currentTimeMillis() / 200D) % 360;
		ArrayList<BakedQuad> quadList = new ArrayList<>();
		if(System.currentTimeMillis() - waitTimer < -weight / 2 || timer > 6.28319d / 2f)
			quadList.addAll(super.getQuads(state, side, rand));
		else
			quadList.addAll(this.oldModel_texure.getQuads(state, side, rand));
		
		for(BakedQuad quad : quadList)
		{
			int[] vertexData = new int[quad.getVertexData().length];
			System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
			for(int j = 0; j < 4; j++)
			{
				int i = (vertexData.length / 4) * j;
				if(System.currentTimeMillis() - waitTimer > 0)
				{
					if(System.currentTimeMillis() > prevTime)
					{
						prevTime = System.currentTimeMillis();
						timer = timer += 0.1d / time ; //0.001 = 100 seconds
						if(timer > 6.28319d)
						{
							timer = 0;
							waitTimer = System.currentTimeMillis() + weight;
						}
					}
					angle += timer;
				}
				float size = 5 / 16f;
				float xOrigin = Float.intBitsToFloat(vertexData[i + 0]) * size + 0.5f - size / 2f;
				float zOrigin = Float.intBitsToFloat(vertexData[i + 2]) * size + 0.5f - size / 2f;
				vertexData[i + 0] = Float.floatToRawIntBits((float) (0.5f + (xOrigin-0.5f)*Math.cos(angle) - (zOrigin-0.5f)*Math.sin(angle)));
				vertexData[i + 1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(vertexData[i + 1]) * size + 1f));
				vertexData[i + 2] = Float.floatToRawIntBits((float) (0.5f + (xOrigin-0.5f)*Math.sin(angle) - (zOrigin-0.5f)*Math.cos(angle)));
			}
			
			list.add(new BakedQuad(vertexData, quad.getTintIndex() + 1, quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
		}
		return list;
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

}
