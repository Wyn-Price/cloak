package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.client.rendering.gui.BasicGui;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.collection.parallel.ParIterableLike.Min;

public class CloakBlock extends Block implements ITileEntityProvider
{
	public CloakBlock() {
		super(Material.ROCK);
		setRegistryName("cloak_block");
		setUnlocalizedName("cloak_block");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityCloakBlock();
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) 
	{
		if(blockAccess.getTileEntity(pos) instanceof TileEntityCloakBlock && !(((TileEntityCloakBlock)blockAccess.getTileEntity(pos)).getRenderState().getBlock() == Blocks.AIR || ((TileEntityCloakBlock)blockAccess.getTileEntity(pos)).getModelState().getBlock() == Blocks.AIR))
			return false;
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
