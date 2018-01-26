package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockCaptureModel implements IBakedModel
{
	
	private static final int[] DATA_RIGHT = {1060110336, 1050673152, 1055916032, -1, 1018156810, 1034682368, 127, 1060110336, 1060110336, 1055916032, -1, 1018156810, 1033109504, 127, 1060110336, 1060110336, 1057488896, -1, 1018156810, 1033109504, 127, 1060110336, 1050673152, 1057488896, -1, 1018156810, 1034682368, 127};
	private static final int[] DATA_LEFT = {1050673152, 1050673152, 1057488896, -1, 1008751084, 1034682368, 129, 1050673152, 1060110336, 1057488896, -1, 1008751084, 1033109504, 129, 1050673152, 1060110336, 1055916032, -1, 1008751084, 1033109504, 129, 1050673152, 1050673152, 1055916032, -1, 1008751084, 1034682368, 129};
	private static final int[] DATA_UP = {1050673152, 1060110336, 1057488896, -1, 1008730112, 1033112126, 32512, 1060110336, 1060110336, 1057488896, -1, 1018167296, 1033112126, 32512, 1060110336, 1060110336, 1055916032, -1, 1018167296, 1033112126, 32512, 1050673152, 1060110336, 1055916032, -1, 1008730112, 1033112126, 32512};
	private static final int[] DATA_DOWN = {1050673152, 1050673152, 1055916032, -1, 1008730112, 1034679746, 33024, 1060110336, 1050673152, 1055916032, -1, 1018167296, 1034679746, 33024, 1060110336, 1050673152, 1057488896, -1, 1018167296, 1034679746, 33024, 1050673152, 1050673152, 1057488896, -1, 1008730112, 1034679746, 33024};
	
	private final IBakedModel oldModel;
	
	public BlockCaptureModel(IBakedModel oldModel) 
	{
		this.oldModel = oldModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		List<BakedQuad> list = new ArrayList<>(oldModel.getQuads(state, side, rand));
		if(list.isEmpty() || stack.isEmpty())
			return Lists.newArrayList();
		NBTTagCompound nbt = stack.getOrCreateSubCompound("capture_info");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(nbt.getString("block")));
		if(block == Blocks.AIR) return list;
		IBlockState renderState = block.getStateFromMeta(nbt.getInteger("meta"));
		if(!list.isEmpty())
		{
			IBakedModel renderModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState);
			ArrayList<BakedQuad> renderStateQuads = new ArrayList<>();
			for(EnumFacing facing : EnumFacing.VALUES)
				renderStateQuads.addAll(renderModel.getQuads(renderState, facing, 0L));
			renderStateQuads.addAll(renderModel.getQuads(renderState, null, 0L));
			for(BakedQuad quad : renderStateQuads)
			{
				int[] vertexData = new int[quad.getVertexData().length];
				System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
				for(int j = 0; j < 4; j++)
				{
					int i = (vertexData.length / 4) * j;//0.3125
					vertexData[i + 0] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 0]) * 0.3125f + 0.34f);
					vertexData[i + 1] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 1]) * 0.3125f + 0.34f);
					vertexData[i + 2] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 2]) * 0.3125f + 0.34f);
				}
				
				list.add(new BakedQuad(vertexData, quad.getTintIndex() + 1, quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
			}
		}
		return list;
	}


	@Override
	public boolean isAmbientOcclusion() 
	{
		return oldModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() 
	{
		return oldModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() 
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return oldModel.getParticleTexture();
	}
	
	private ItemStack stack = ItemStack.EMPTY;

	@Override
	public ItemOverrideList getOverrides() 
	{
		return new ItemOverrideList(oldModel.getOverrides().getOverrides())
				{
					@Override
					public ResourceLocation applyOverride(ItemStack stack, World worldIn, EntityLivingBase entityIn) 
					{
						BlockCaptureModel.this.stack = stack;
						return super.applyOverride(stack, worldIn, entityIn);
					}
				};
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return oldModel.getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		return Pair.of(this, oldModel.handlePerspective(cameraTransformType).getRight());
	}

}
