package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.handlers.CloakGUIHandler;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
		return new TileEntityCloakingMachine(true);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
			playerIn.openGui(CloakMod.instance, CloakGUIHandler.ADVANCED_CLOAKING_MACHINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(worldIn.getTileEntity(pos) instanceof TileEntityCloakingMachine)
		{
			TileEntityCloakingMachine tileEntity = (TileEntityCloakingMachine) worldIn.getTileEntity(pos);
			for(int i = 0; i < tileEntity.getHandler().getSlots(); i++)
				if(i != 2)
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileEntity.getHandler().getStackInSlot(i));
			
			for(ItemStack stack : tileEntity.getCurrentModificationList().values())
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);

		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return new AxisAlignedBB(0, 0, 0, 1, 0.6875, 1);
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) 
	{
		return false;
	}

}
