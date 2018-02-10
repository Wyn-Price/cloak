package com.wynprice.brl.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

/***
 * Used to store all the information about how a model will be rendered
 * @author Wyn Price
 *
 */
public class BRLRenderInfo 
{
	/**
	 * The model being rendered
	 */
	private final IBakedModel model;
	
	/**
	 * The blockstate linked to it. This is used for mainly blockcolors
	 */
	private final IBlockState state;
	
	/**
	 * The location of the texture that will be rendered with the model and blockcolor
	 */
	private final ResourceLocation location;
	
	
	/**
	 * @param model The model to render
	 * @param state The state to use for blockcolors
	 * @param location The location of the texture to use
	 */
	public BRLRenderInfo(IBakedModel model, IBlockState state, ResourceLocation location) 
	{
		this.model = model;
		this.state = state;
		this.location = location;

	}
	
	public ResourceLocation getLocation() {
		return location;
	}
	
	public IBakedModel getModel() {
		return model;
	}
	
	public IBlockState getState() {
		return state;
	}
		
}
