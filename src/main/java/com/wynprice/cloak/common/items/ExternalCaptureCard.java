package com.wynprice.cloak.common.items;

import com.wynprice.cloak.client.rendering.gui.ExternalImageGui;
import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExternalCaptureCard extends Item
{
	public ExternalCaptureCard() 
	{
		setRegistryName("external_capture");
		setUnlocalizedName("external_capture");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(!playerIn.getHeldItem(handIn).hasTagCompound()) playerIn.getHeldItem(handIn).setTagCompound(new NBTTagCompound());
		if(playerIn.isSneaking())
			playerIn.getHeldItem(handIn).getTagCompound().setTag("capture_info", new NBTTagCompound());
		else if(worldIn.isRemote)
			openGui();
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@SideOnly(Side.CLIENT)
	private void openGui()
	{
		ExternalImageHandler.init();
		Minecraft.getMinecraft().displayGuiScreen(new ExternalImageGui());
	}
}
