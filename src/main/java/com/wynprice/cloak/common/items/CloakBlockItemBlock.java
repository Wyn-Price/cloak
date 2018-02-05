package com.wynprice.cloak.common.items;

import java.util.HashMap;

import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class CloakBlockItemBlock extends ItemBlock
{

	public CloakBlockItemBlock(Block block) {
		super(block);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(GuiScreen.isCtrlKeyDown())
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(this));
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		EnumActionResult result =  super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.SUCCESS)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();
	        if (!block.isReplaceable(worldIn, pos))
	            pos = pos.offset(facing);
	        if(worldIn.getTileEntity(pos) instanceof TileEntityCloakBlock && !player.isSneaking())
	        {
		        IBlockState iblockstate1 = NBTUtil.readBlockState(((TileEntityCloakBlock)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(1).getTagCompound().getCompoundTag("capture_info").copy());
		        NBTTagCompound nbt1 = NBTUtil.writeBlockState(((TileEntityCloakBlock)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(1).getOrCreateSubCompound("itemblock_info"), iblockstate1);
		        NBTUtil.writeBlockState(((TileEntityCloakBlock)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(1).getTagCompound().getCompoundTag("capture_info"), iblockstate1.getBlock().getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player, hand));
	        }
		}
        return result;
	}
	
	public static final HashMap<BlockPos, NBTTagCompound> PRESETTING_LIST = new HashMap<>();
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) 
	{
		NBTTagCompound compound = stack.getOrCreateSubCompound("rendering_info").copy();
		PRESETTING_LIST.put(pos, compound);
		if (!world.setBlockState(pos, newState, 11)) return false;
		
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
        	TileEntityCloakBlock te = (TileEntityCloakBlock) world.getTileEntity(pos);
        	te.readRenderData(compound);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }

        return true;
	}

}
