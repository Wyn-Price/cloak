package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.client.rendering.gui.CaptureCardDyeGUI;
import com.wynprice.cloak.common.items.ICaptureCard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CaptureCardHandler 
{
	@SubscribeEvent
	public void onRightClick(RightClickItem event)
	{
		if(GuiScreen.isCtrlKeyDown() && event.getItemStack().getItem() instanceof ICaptureCard)
		{
			Minecraft.getMinecraft().displayGuiScreen(new CaptureCardDyeGUI(event.getItemStack()));
			event.setCancellationResult(EnumActionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}
