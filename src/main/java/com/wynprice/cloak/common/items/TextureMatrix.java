package com.wynprice.cloak.common.items;

import com.wynprice.cloak.client.rendering.gui.UploadMatrixGui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureMatrix extends Item
{
	public TextureMatrix() 
	{
		setRegistryName("texture_matrix");
		setUnlocalizedName("texture_matrix");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(worldIn.isRemote)
			openGui();
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@SideOnly(Side.CLIENT)
	public void openGui()
	{
		Minecraft.getMinecraft().displayGuiScreen(new UploadMatrixGui());
	}
}
