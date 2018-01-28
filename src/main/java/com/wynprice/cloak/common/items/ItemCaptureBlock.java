package com.wynprice.cloak.common.items;

import com.wynprice.cloak.client.rendering.CloakedModel;
import com.wynprice.cloak.client.rendering.gui.BasicGui;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{		
		Minecraft.getMinecraft().displayGuiScreen(new BasicGui(new ItemStackHandler(2), Blocks.SANDSTONE_STAIRS.getDefaultState(), new CloakedModel(Blocks.SANDSTONE_STAIRS.getDefaultState(), Blocks.CHAIN_COMMAND_BLOCK.getDefaultState())));
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound nbt = new NBTTagCompound();
		IBlockState state = worldIn.getBlockState(pos);
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, state.getBlock().getPickBlock(state, worldIn.rayTraceBlocks(player.getPositionVector(), new Vec3d(pos)), worldIn, pos, player));
		
		nbt.setString("block", state.getBlock().getRegistryName().toString());
		nbt.setInteger("meta", state.getBlock().getMetaFromState(state));
		nbt.setTag("item", handler.serializeNBT());

		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("capture_info", nbt);
		return EnumActionResult.SUCCESS;
	}
}
