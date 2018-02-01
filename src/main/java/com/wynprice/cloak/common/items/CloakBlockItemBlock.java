package com.wynprice.cloak.common.items;

import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
        if(worldIn.getBlockState(pos).getBlock() == CloakBlocks.CLOAK_BLOCK && worldIn.getTileEntity(pos) instanceof TileEntityCloakBlock && player.isSneaking())
        {
        	ItemStack stack = player.getHeldItem(hand);
        	if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        	stack.getTagCompound().setTag("rendering_info", ((TileEntityCloakBlock) worldIn.getTileEntity(pos)).writeRenderData(new NBTTagCompound()));
        	return EnumActionResult.SUCCESS;
        }

		EnumActionResult result =  super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.SUCCESS)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();
	        if (!block.isReplaceable(worldIn, pos))
	            pos = pos.offset(facing);
	        if(worldIn.getTileEntity(pos) instanceof TileEntityCloakBlock)
	        {
				int i = this.getMetadata(player.getHeldItem(hand).getMetadata());
		        IBlockState iblockstate1 = NBTUtil.readBlockState(((TileEntityCloakBlock)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(1).getTagCompound().getCompoundTag("capture_info").copy());
		        NBTUtil.writeBlockState(((TileEntityCloakBlock)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(1).getTagCompound().getCompoundTag("capture_info"), iblockstate1.getBlock().getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand));

	        }
	        
		}
        
        
        return result;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) 
	{
		if (!world.setBlockState(pos, newState, 11)) return false;
		
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
        	TileEntityCloakBlock te = (TileEntityCloakBlock) world.getTileEntity(pos);
        	te.readRenderData(stack.getOrCreateSubCompound("rendering_info").copy());
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }

        return true;
	}

}
