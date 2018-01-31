package com.wynprice.cloak.common.items;

import com.wynprice.cloak.common.blocks.CloakBlock;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CloakBlockItemBlock extends ItemBlock
{

	public CloakBlockItemBlock(Block block) {
		super(block);
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
        	te.readRenderData(stack.getOrCreateSubCompound("rendering_info"));
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }

        return true;
	}

}
