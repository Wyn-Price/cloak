package com.wynprice.cloak.client.rendering.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class LiquidCaptureModel extends BaseModelProxy
{
	
	private static final FaceBakery BAKERY = new FaceBakery();
	
	public LiquidCaptureModel(IBakedModel oldModel) 
	{
		super(oldModel);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		List<BakedQuad> list = new ArrayList<>(oldModel.getQuads(state, side, rand));
		if(stack.isEmpty())
			return Lists.newArrayList();
		IBlockState renderState = NBTUtil.readBlockState(stack.getOrCreateSubCompound("capture_info"));
		if(renderState.getBlock() == Blocks.AIR) return list;
		if(!list.isEmpty())
		{
			IBakedModel renderModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState);
			ArrayList<BakedQuad> renderStateQuads = new ArrayList<>();
			for(EnumFacing facing : EnumFacing.VALUES)
				renderStateQuads.addAll(renderModel.getQuads(renderState, facing, 0L));
			renderStateQuads.addAll(renderModel.getQuads(renderState, null, 0L));
			for(EnumFacing face : EnumFacing.VALUES)
				list.add(BAKERY.makeBakedQuad(new Vector3f(4f, 5f, 7.5f), new Vector3f(12f, 10f, 8.5f), new BlockPartFace(face, 1, "",  new BlockFaceUV(new float[]{0f, 2f, 16f, 13f}, 0)),
						Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(renderState), face, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(0, 0, 0), Axis.X, 0f, false), false, true));
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
						LiquidCaptureModel.this.stack = stack;
						return super.applyOverride(stack, worldIn, entityIn);
					}
				};
	}

}
