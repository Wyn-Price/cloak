package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.brl.api.BRLRenderInfo;
import com.wynprice.brl.api.IBRLRenderFactory;
import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.GroupedSingleQuadModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CloakBlockBRLFactory implements IBRLRenderFactory
{

	@Override
	public List<BRLRenderInfo> getModels(IBlockAccess access, BlockPos pos, IBlockState inState) 
	{
		if(!(access.getTileEntity(pos) instanceof TileEntityCloakBlock))
			return IBRLRenderFactory.super.getModels(access, pos, inState);
		TileEntityCloakBlock te = (TileEntityCloakBlock)access.getTileEntity(pos);
		IBlockState renderState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info")).getActualState(access, pos);
    	IBlockState modelState = NBTUtil.readBlockState(te.getHandler().getStackInSlot(1).getOrCreateSubCompound("capture_info")).getActualState(access, pos);
    	
    	CloakedModel model = new CloakedModel(modelState, renderState);
    	HashMap<Integer, IBlockState> overrideList = new HashMap<>();
    	HashMap<Integer, ResourceLocation> externalOverrideList = new HashMap<>();
    	
		for(int i : te.getCurrentModificationList().keySet())
			if(te.getCurrentModificationList().get(i) != null && !te.getCurrentModificationList().get(i).isEmpty())
				if(te.getCurrentModificationList().get(i).getItem() != CloakItems.EXTERNAL_CARD)
					overrideList.put(i, NBTUtil.readBlockState(te.getCurrentModificationList().get(i).getSubCompound("capture_info")));
				else if(ExternalImageHandler.SYNCED_RESOURCE_MAP.containsKey(te.getCurrentModificationList().get(i).getSubCompound("capture_info").getString("external_image")))
					externalOverrideList.put(i, ExternalImageHandler.SYNCED_RESOURCE_MAP.get(te.getCurrentModificationList().get(i).getSubCompound("capture_info").getString("external_image")));
		model.getOverrideList().putAll(overrideList);
		model.setExternalOverrideList(externalOverrideList);
		model.setBaseTextureExternal(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(te.getHandler().getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")));
    	List<EnumFacing> facingList = new ArrayList<>();
    	HashMap<ResourceLocation, Pair<GroupedSingleQuadModel, IBlockState>> modelmap = new HashMap<>();
    	ArrayList<BRLRenderInfo> renderInfos = Lists.newArrayList();
    	facingList.add(null);
        for (EnumFacing enumfacing : EnumFacing.values()) facingList.add(enumfacing);
    	for(EnumFacing face : facingList)
    		for(BakedQuad quad : model.getQuads(renderState, face, 0L))
    		{
    			ResourceLocation location = quad instanceof ExternalBakedQuad ? ((ExternalBakedQuad)quad).getLocation() : TextureMap.LOCATION_BLOCKS_TEXTURE; 
    			if(!modelmap.containsKey(location))
    				modelmap.put(location, Pair.of(new GroupedSingleQuadModel(model), model.getStateFromQuad(quad)));
    			modelmap.get(location).getLeft().addQuad(face, quad);
    		}
    	
    	
    	for(ResourceLocation location : modelmap.keySet())
    		renderInfos.add(new BRLRenderInfo(modelmap.get(location).getLeft(), modelmap.get(location).getRight(), location));
    	Collections.reverse(renderInfos);
		return renderInfos;
	}

}
