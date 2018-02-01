package com.wynprice.cloak.client.rendering.world;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class CloakRenderChunkListed extends ListedRenderChunk
{
	private final CloakRenderChunk interChunk;
	
	public CloakRenderChunkListed(World worldIn, RenderGlobal renderGlobalIn, int indexIn, RenderChunk render) 
	{
		super(worldIn, renderGlobalIn, indexIn);
		this.interChunk = new CloakRenderChunk(worldIn, renderGlobalIn, indexIn, render);
	}
	
	@Override
	protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) 
	{
		return interChunk.createRegionRenderCache(world, from, to, subtract);
	}
	
	private final int baseDisplayList = GLAllocation.generateDisplayLists(BlockRenderLayer.values().length);

	public int getDisplayList(BlockRenderLayer layer, CompiledChunk p_178600_2_)
	{
		return !p_178600_2_.isLayerEmpty(layer) ? this.baseDisplayList + layer.ordinal() : -1;
	}

	public void deleteGlResources()
	{
		super.deleteGlResources();
		interChunk.deleteGlResources();
		GLAllocation.deleteDisplayLists(this.baseDisplayList, BlockRenderLayer.values().length);
	}
}
