package com.wynprice.cloak.common.items;

import java.awt.Color;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.rendering.gui.CaptureCardDyeGUI;
import com.wynprice.cloak.client.rendering.gui.ExternalImageGui;
import com.wynprice.cloak.server.handlers.ServerExternalImageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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

public class ExternalCaptureCard extends Item implements ICaptureCard
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
		else if(!worldIn.isRemote)
			ServerExternalImageHandler.init();
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderOntoColorWheel(CaptureCardDyeGUI parent, ItemStack stack, int colorint, int x, int y)
	{
		if(!stack.getOrCreateSubCompound("capture_info").hasKey("external_image", 8))
			return;
		GlStateManager.enableBlend();
		Color color = new Color(colorint);
		GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		Minecraft.getMinecraft().renderEngine.bindTexture(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(stack.getOrCreateSubCompound("capture_info").getString("external_image")));
		parent.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 50, 50, 50, 50);
		GlStateManager.color(1f, 1f, 1f);
		GlStateManager.disableBlend();
	}
	
	@SideOnly(Side.CLIENT)
	private void openGui()
	{
		ExternalImageHandler.init();
		Minecraft.getMinecraft().displayGuiScreen(new ExternalImageGui());
	}
}
