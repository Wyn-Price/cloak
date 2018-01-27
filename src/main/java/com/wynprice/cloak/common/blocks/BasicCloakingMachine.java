package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BasicCloakingMachine extends Block implements ITileEntityProvider
{

	public BasicCloakingMachine() 
	{
		super(Material.IRON);
		setRegistryName("cloaking_machine");
		setUnlocalizedName("cloaking_machine");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCloakingMachine();
	}

}
