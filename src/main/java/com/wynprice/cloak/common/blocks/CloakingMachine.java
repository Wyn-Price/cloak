package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.handlers.CloakGUIHandler;
import com.wynprice.cloak.common.network.packets.PacketInitiateCloakingRecipe;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import scala.tools.cmd.gen.AnyValReps.AnyValNum.Op;

public class CloakingMachine extends Block implements ITileEntityProvider
{

    public static final PropertyBool POWERED = PropertyBool.create("powered");

	public CloakingMachine() 
	{
		super(Material.IRON);
		setRegistryName("cloaking_machine");
		setUnlocalizedName("cloaking_machine");
		setHardness(0.5f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCloakingMachine();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
			playerIn.openGui(CloakMod.instance, CloakGUIHandler.CLOAKING_MACHINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) 
	{
		if(worldIn.isBlockPowered(pos) != state.getValue(POWERED))
		{
			if(worldIn.isBlockPowered(pos))
				PacketInitiateCloakingRecipe.doRecipe((TileEntityCloakingMachine) worldIn.getTileEntity(pos));

			NBTTagCompound tagcompound = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
			worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
			worldIn.getTileEntity(pos).readFromNBT(tagcompound);
		}
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
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
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWERED) ? 1 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(POWERED, meta == 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, POWERED);
	}

}
