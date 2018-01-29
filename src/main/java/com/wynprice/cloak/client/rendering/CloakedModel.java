package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.cloak.common.core.UVTransformer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CloakedModel implements IBakedModel
{
	protected final IBakedModel oldModel_model;
	protected final IBakedModel oldModel_texure;
	
	protected final IBlockState modelState;
	protected final IBlockState renderState;
	
	private HashMap<BakedQuad, BakedQuad> parentQuadMap = new HashMap<>();
	
	protected final HashMap<Integer, IBlockState> overrideList;
	
	public CloakedModel(IBlockState modelState, IBlockState renderState) 
	{
		this(modelState, renderState, new HashMap());
	}

	
	public CloakedModel(IBlockState modelState, IBlockState renderState, HashMap<Integer, IBlockState> overrideList) 
	{
		this.overrideList = overrideList;
		this.modelState = modelState;
		this.renderState = renderState;
		this.oldModel_model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(modelState);
		this.oldModel_texure = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		ArrayList<BakedQuad> list = Lists.newArrayList();
		for(BakedQuad modelQuad : oldModel_model.getQuads(modelState, side, rand))
		{
			int l = getIndentifierList().indexOf(modelQuad);
			List<BakedQuad> textureQuads = overrideList.containsKey(l) ? Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(overrideList.get(l)).getQuads(overrideList.get(l), modelQuad.getFace(), rand) : oldModel_texure.getQuads(renderState, modelQuad.getFace(), rand);
			for(BakedQuad renderQuad : textureQuads)
			{
				int[] modelVertex = new int[modelQuad.getVertexData().length];
				System.arraycopy(modelQuad.getVertexData(), 0, modelVertex, 0, modelVertex.length);
				BlockFaceUV faceUV = UVTransformer.getUV(modelQuad.getVertexData());
				if(side == EnumFacing.UP) faceUV = new BlockFaceUV(faceUV.uvs, 1); //Who knows why this has to be here. It just does
				for(int j = 0; j < 4; j++)
				{
					int i = (modelVertex.length / 4) * j;
					modelVertex[i + 4] = Float.floatToRawIntBits(renderQuad.getSprite().getInterpolatedU((double)faceUV.getVertexU(j) * .999 + faceUV.getVertexU((j + 2) % 4) * .001));
					modelVertex[i + 4 + 1] = Float.floatToRawIntBits(renderQuad.getSprite().getInterpolatedV((double)faceUV.getVertexV(j) * .999 + faceUV.getVertexV((j + 2) % 4) * .001));
				}
				BakedQuad newQuad = new BakedQuad(modelVertex, renderQuad.getTintIndex(), renderQuad.getFace(), renderQuad.getSprite(), renderQuad.shouldApplyDiffuseLighting(), renderQuad.getFormat());
				parentQuadMap.put(newQuad, modelQuad);
				list.add(newQuad);
			}
		}
		return list;
	}
	
	public boolean isParentSelected(BakedQuad currentQuad, int selected)
	{ 
        return getParentID(currentQuad) == selected && selected != -1;
	}
	
	public int getParentID(BakedQuad selectedQuad)
	{
		return getIndentifierList().indexOf(parentQuadMap.get(selectedQuad));
	}
	
	public List<BakedQuad> getFullList()
	{
		List<BakedQuad> quadList = new ArrayList<>(this.getQuads((IBlockState)null, (EnumFacing)null, 0L));
        for (EnumFacing enumfacing : EnumFacing.values())
        	quadList.addAll(this.getQuads(null, enumfacing, 0L));
        return quadList;
	}
	
	public List<BakedQuad> getIndentifierList()
	{
		List<BakedQuad> quadList = new ArrayList<>(oldModel_model.getQuads(modelState, (EnumFacing)null, 0L));
        for (EnumFacing enumfacing : EnumFacing.values())
        	quadList.addAll(oldModel_model.getQuads(modelState, enumfacing, 0L));
        return quadList;
	}
		
	@Override
	public boolean isAmbientOcclusion() 
	{
		return oldModel_model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() 
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return oldModel_model.getParticleTexture();
	}
	
	private ItemStack stack = ItemStack.EMPTY;

	@Override
	public ItemOverrideList getOverrides() 
	{
		return new ItemOverrideList(oldModel_model.getOverrides().getOverrides())
				{
					@Override
					public ResourceLocation applyOverride(ItemStack stack, World worldIn, EntityLivingBase entityIn) 
					{
						CloakedModel.this.stack = stack;
						return super.applyOverride(stack, worldIn, entityIn);
					}
				};
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return oldModel_model.getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		return Pair.of(this, oldModel_model.handlePerspective(cameraTransformType).getRight());
	}
}
