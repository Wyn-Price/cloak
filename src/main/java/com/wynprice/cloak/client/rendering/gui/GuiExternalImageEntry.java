package com.wynprice.cloak.client.rendering.gui;

import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.util.ResourceLocation;

public class GuiExternalImageEntry implements IGuiListEntry
{

	private final ResourceLocation location;
	private final GuiExternalImageList parent;
	public final String fileName;
	
	public GuiExternalImageEntry(String fileName, GuiExternalImageList parent) 
	{
		this.fileName = fileName;
		this.parent = parent;
		this.location = ExternalImageHandler.RESOURCE_MAP.get(fileName);
	}
	
	@Override
	public void updatePosition(int slotIndex, int x, int y, float partialTicks) 
	{
		
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
			boolean isSelected, float partialTicks) 
	{
		Minecraft.getMinecraft().fontRenderer.drawString(fileName, x + 5, y + 4, 16777215);
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
		parent.parent.drawModalRectWithCustomSizedTexture(x + Minecraft.getMinecraft().fontRenderer.getStringWidth(fileName) + 10, y, 0, 0, 16, 16, 16, 16);
		
	}

	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) 
	{
		parent.selectedPoint = slotIndex;
		return false;
	}

	@Override
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		
	}

}
