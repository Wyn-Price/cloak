package com.wynprice.cloak.client.rendering.particle;

import com.wynprice.cloak.client.handlers.TextureAtlasSpriteHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleExternalDigging extends ParticleDigging
{
	
	private final ResourceLocation location;

	public ParticleExternalDigging(ResourceLocation location, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn) 
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Blocks.STONE.getDefaultState());
		this.location = location;
		this.setParticleTexture(TextureAtlasSpriteHelper.FULL_SPRITE);
	}
	
	@Override
	protected void multiplyColor(BlockPos p_187154_1_) 
	{
		;
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) 
	{
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		Minecraft.getMinecraft().renderEngine.bindTexture(location);
		Tessellator.getInstance().draw();
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

	}
	

}
