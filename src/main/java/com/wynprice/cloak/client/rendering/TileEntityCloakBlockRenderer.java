package com.wynprice.cloak.client.rendering;

import org.lwjgl.opengl.GL11;

import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;
import com.wynprice.cloak.common.world.CloakBlockAccess;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCloakBlockRenderer extends TileEntityCloakingMachineRenderer<TileEntityCloakBlock>
{
	public TileEntityCloakBlockRenderer() 
	{
		super(new CloakedRenderingFactory() {
			
			@Override
			public CloakedModel createModel(World world, BlockPos pos, IBlockState modelState, IBlockState renderState) 
			{
				return new CloakedModel(modelState.getActualState(new CloakBlockAccess(world), pos), renderState);
			}
		});
	}
}
