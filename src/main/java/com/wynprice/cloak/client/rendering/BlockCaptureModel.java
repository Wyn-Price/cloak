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

public class BlockCaptureModel extends BaseModelProxy
{
	
	public BlockCaptureModel(IBakedModel oldModel) 
	{
		super(oldModel);
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
					int i = (vertexData.length / 4) * j;
					vertexData[i + 0] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 0]) * 0.3125f + 0.5f - 0.3125f / 2f);
					vertexData[i + 1] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 1]) * 0.3125f + 0.5f - 0.3125f / 2f);
					vertexData[i + 2] = Float.floatToRawIntBits(Float.intBitsToFloat(vertexData[i + 2]) * 0.3125f + 0.5f - 0.3125f / 2f);
				}
				
				list.add(new BakedQuad(vertexData, quad.getTintIndex() + 1, quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
			}
		}
		return list;
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

}
