package com.wynprice.cloak.client.rendering.models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.wynprice.cloak.common.core.UVTransformer;
import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class ExternalCaptureModel extends BaseModelProxy
{
	
	private static final FaceBakery BAKERY = new FaceBakery();
	
	public ExternalCaptureModel(IBakedModel oldModel) 
	{
		super(oldModel);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		if(rand == 0)
			return oldModel.getQuads(state, side, rand);
		
		if(stack.isEmpty())
			return Lists.newArrayList();
		
		ResourceLocation location  = ExternalImageHandler.RESOURCE_MAP.get(stack.getOrCreateSubCompound("capture_info").getString("external_image"));
		if(location != null && rand == 1 && side == null)
		{
			ArrayList<BakedQuad> newList = new ArrayList<>();
			for(EnumFacing face : EnumFacing.VALUES)
			{
				BakedQuad quad = BAKERY.makeBakedQuad(new Vector3f(4f, 4f, 7.5f), new Vector3f(12f, 12f, 8.5f), new BlockPartFace(face, 1, "",  new BlockFaceUV(new float[]{0f, 0f, 16f, 16f}, 0)),
						Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.STONE.getDefaultState()), face, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(0, 0, 0), Axis.X, 0f, false), false, true);

				BlockFaceUV faceUV = new BlockFaceUV(new float[]{0f, 0f, 16f, 16f}, 0);
				if(quad.getFace() == EnumFacing.UP) faceUV = new BlockFaceUV(faceUV.uvs, 1); //Who knows why this has to be here. It just does
				for(int j = 0; j < 4; j++)
				{
					int i = (quad.getVertexData().length / 4) * j;
					quad.getVertexData()[i + 4] = Float.floatToRawIntBits(Lists.newArrayList(faceUV.uvs[0], faceUV.uvs[0], faceUV.uvs[2], faceUV.uvs[2]).get(j) / 16f);
					quad.getVertexData()[i + 5] = Float.floatToRawIntBits(Lists.newArrayList(faceUV.uvs[1], faceUV.uvs[3], faceUV.uvs[3], faceUV.uvs[1]).get(j) / 16f);
				}
				newList.add(quad);
			}
			return newList;
		}
		return Lists.newArrayList();
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
						ExternalCaptureModel.this.stack = stack;
						return super.applyOverride(stack, worldIn, entityIn);
					}
				};
	}
	
	public static ExternalCaptureModel instance;
	
	@Override
	public boolean isBuiltInRenderer() 
	{
		this.instance = this;
		return true;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
}
