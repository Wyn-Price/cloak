package com.wynprice.cloak.client.rendering.world;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class CloakedRenderChunkFactory implements IRenderChunkFactory
{


	private final IRenderChunkFactory base;
	
	public CloakedRenderChunkFactory(IRenderChunkFactory base) {
		this.base = base;
	}
	
	@Override
	public RenderChunk create(World worldIn, RenderGlobal renderGlobalIn, int index) {
		return OpenGlHelper.useVbo() ? new CloakRenderChunk(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index)) : new CloakRenderChunkListed(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index));
	}
}
