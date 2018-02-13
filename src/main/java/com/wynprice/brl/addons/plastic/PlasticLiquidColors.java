package com.wynprice.brl.addons.plastic;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class PlasticLiquidColors extends BlockColors
{

	private final BlockColors blockColors;
	
	public PlasticLiquidColors(BlockColors fakeColors) 
	{
		this.blockColors = fakeColors;
	}
	
	public int getColor(IBlockState state, World p_189991_2_, BlockPos p_189991_3_)
    {
        return blockColors.getColor(state, p_189991_2_, p_189991_3_);
    }

    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess blockAccess, @Nullable BlockPos pos, int renderPass)
    {
    	if(BufferedPlastic.plastic)
    		return BufferedPlastic.getImageColor(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
        return blockColors.colorMultiplier(state, blockAccess, pos, renderPass);
    }

    public void registerBlockColorHandler(IBlockColor blockColor, Block... blocksIn)
    {
    	blockColors.registerBlockColorHandler(blockColor, blocksIn);
    }
	
}
