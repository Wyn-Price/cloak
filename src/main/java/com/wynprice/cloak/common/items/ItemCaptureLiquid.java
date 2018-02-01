package com.wynprice.cloak.common.items;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCaptureLiquid extends Item
{
	public ItemCaptureLiquid() 
	{
		setUnlocalizedName("liquid_capture");
		setRegistryName("liquid_capture");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTTagCompound nbt = new NBTTagCompound();
		Vec3d startPos = new Vec3d(playerIn.posX, playerIn.posY + playerIn.getEyeHeight(), playerIn.posZ);
        Vec3d endPos = startPos.add(new Vec3d(playerIn.getLookVec().x * 4f, playerIn.getLookVec().y * 4f, playerIn.getLookVec().z * 4f));
		RayTraceResult raytrace = worldIn.rayTraceBlocks(startPos, endPos, true);
		if(raytrace == null || raytrace.typeOfHit != RayTraceResult.Type.BLOCK) 
			return super.onItemRightClick(worldIn, playerIn, handIn);
		IBlockState state = worldIn.getBlockState(raytrace.getBlockPos());
		if(!(state.getBlock() instanceof BlockFluidBase) && !(state.getBlock() instanceof BlockLiquid) && !(state.getBlock() instanceof IFluidBlock))
			return super.onItemRightClick(worldIn, playerIn, handIn);
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, ItemStack.EMPTY);
		NBTUtil.writeBlockState(nbt, state);
		nbt.setTag("item", handler.serializeNBT());
		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("capture_info", nbt);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}
