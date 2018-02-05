package com.wynprice.cloak.common.items;

import com.wynprice.cloak.client.rendering.gui.CaptureCardDyeGUI;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICaptureCard 
{
	@SideOnly(Side.CLIENT)
	public void renderOntoColorWheel(CaptureCardDyeGUI parent, ItemStack stack, int color, int x, int y);
	
}
