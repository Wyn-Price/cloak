package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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

}
