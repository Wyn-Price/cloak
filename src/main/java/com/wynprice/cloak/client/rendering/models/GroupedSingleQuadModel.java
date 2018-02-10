package com.wynprice.cloak.client.rendering.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class GroupedSingleQuadModel extends BaseModelProxy
{
	
	private final HashMap<EnumFacing, ArrayList<BakedQuad>> quadMap = new HashMap<>();

	public GroupedSingleQuadModel(IBakedModel oldModel) 
	{
		super(oldModel);
		for(EnumFacing face : EnumFacing.VALUES)
			quadMap.put(face, new ArrayList<>());
		quadMap.put(null, new ArrayList<>());
	}
	
	public void addQuad(EnumFacing face, BakedQuad quad)
	{
		this.quadMap.get(face).add(quad);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		return this.quadMap.get(side);
	}

}
