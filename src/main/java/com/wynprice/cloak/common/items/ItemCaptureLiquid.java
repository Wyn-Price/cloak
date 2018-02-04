package com.wynprice.cloak.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
		RayTraceResult raytrace = rayTrace(worldIn, playerIn);
		if(raytrace == null || raytrace.typeOfHit != RayTraceResult.Type.BLOCK) 
		{
			if(!playerIn.getHeldItem(handIn).hasTagCompound()) playerIn.getHeldItem(handIn).setTagCompound(new NBTTagCompound());
			if(playerIn.isSneaking())
				playerIn.getHeldItem(handIn).getTagCompound().setTag("capture_info", new NBTTagCompound());
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
		IBlockState state = worldIn.getBlockState(raytrace.getBlockPos());
		if(!(state.getBlock() instanceof BlockFluidBase) && !(state.getBlock() instanceof BlockLiquid) && !(state.getBlock() instanceof IFluidBlock))
			return super.onItemRightClick(worldIn, playerIn, handIn);
		playerIn.swingArm(handIn);
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, ItemStack.EMPTY);
		NBTUtil.writeBlockState(nbt, state);
		nbt.setTag("item", handler.serializeNBT());
		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("capture_info", nbt);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	private RayTraceResult rayTrace(World world, EntityPlayer playerIn)
	{
		Vec3d vecStart = new Vec3d(playerIn.posX, playerIn.posY + playerIn.getEyeHeight(), playerIn.posZ);
        Vec3d vecEnd = vecStart.add(new Vec3d(playerIn.getLookVec().x * 4f, playerIn.getLookVec().y * 4f, playerIn.getLookVec().z * 4f));

		if (!Double.isNaN(vecStart.x) && !Double.isNaN(vecStart.y) && !Double.isNaN(vecStart.z))
        {
            if (!Double.isNaN(vecEnd.x) && !Double.isNaN(vecEnd.y) && !Double.isNaN(vecEnd.z))
            {
                int i = MathHelper.floor(vecEnd.x);
                int j = MathHelper.floor(vecEnd.y);
                int k = MathHelper.floor(vecEnd.z);
                int l = MathHelper.floor(vecStart.x);
                int i1 = MathHelper.floor(vecStart.y);
                int j1 = MathHelper.floor(vecStart.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (block.canCollideCheck(iblockstate, true))
                {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vecStart, vecEnd);

                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vecStart.x) || Double.isNaN(vecStart.y) || Double.isNaN(vecStart.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vecEnd.x - vecStart.x;
                    double d7 = vecEnd.y - vecStart.y;
                    double d8 = vecEnd.z - vecStart.z;

                    if (flag2)
                    {
                        d3 = (d0 - vecStart.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vecStart.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vecStart.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vecStart = new Vec3d(d0, vecStart.y + d7 * d3, vecStart.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vecStart = new Vec3d(vecStart.x + d6 * d4, d1, vecStart.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vecStart = new Vec3d(vecStart.x + d6 * d5, vecStart.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vecStart.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vecStart.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vecStart.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();
                    RayTraceResult raytraceresult1 = null;
                    if(block1 != Blocks.AIR)
                    	raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vecStart, vecEnd);
                    if (raytraceresult1 != null)
                    {
                        return raytraceresult1;
                    }
                }

                return null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
	}
}
