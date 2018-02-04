package com.wynprice.cloak.common.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCaptureBlock extends Item
{
	public ItemCaptureBlock() 
	{
		setRegistryName("block_capture");
		setUnlocalizedName("block_capture");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(!playerIn.getHeldItem(handIn).hasTagCompound()) playerIn.getHeldItem(handIn).setTagCompound(new NBTTagCompound());
		if(playerIn.isSneaking())
			playerIn.getHeldItem(handIn).getTagCompound().setTag("capture_info", new NBTTagCompound());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{		
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound nbt = new NBTTagCompound();
		IBlockState state = worldIn.getBlockState(pos).getActualState(worldIn, pos);
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, state.getBlock().getPickBlock(state, worldIn.rayTraceBlocks(player.getPositionVector(), new Vec3d(pos)), worldIn, pos, player));
		
		NBTUtil.writeBlockState(nbt, state);
		nbt.setTag("item", handler.serializeNBT());

		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("capture_info", nbt);
		return EnumActionResult.SUCCESS;
	}
}
