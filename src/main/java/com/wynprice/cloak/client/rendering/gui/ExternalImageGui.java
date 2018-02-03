package com.wynprice.cloak.client.rendering.gui;

import java.io.IOException;

import com.wynprice.cloak.common.handlers.ExternalImageHandler;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketUpdateExternalCard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ExternalImageGui extends GuiScreen
{
	public GuiTextField name;
	
	private GuiExternalImageList imageList;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		imageList = new GuiExternalImageList(this.mc, this, this.width, this.height, 32, this.height - 64, 20);
		name = new GuiTextField(0, fontRenderer, this.width / 2 - 100, this.height - 40, 200, 20);
		name.setFocused(true);
		name.setCanLoseFocus(false);
		addButton(new GuiButton(0, this.width / 2 - 210, this.height - 40, 100, 20,  I18n.format("gui.cancel")));
		addButton(new GuiButton(1, this.width / 2 + 110, this.height - 40, 100, 20,  I18n.format("gui.cloak.save")));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.imageList.drawScreen(mouseX, mouseY, partialTicks);
		name.drawTextBox();

	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		imageList.actionPerformed(button);
		super.actionPerformed(button);
		if(button.id == 0)
			closeGui(false);
		else if(button.id == 1)
			closeGui(true);
	}
	
	protected void closeGui(boolean save)
	{
		if(this.imageList.selectedPoint != -1)
		{
			String fileName = ((GuiExternalImageEntry)this.imageList.getListEntry(this.imageList.selectedPoint)).fileName;
			ResourceLocation location = ExternalImageHandler.RESOURCE_MAP.get(fileName);
			if(location != null && save)
			{
				PacketUpdateExternalCard.setNBT(Minecraft.getMinecraft().player, fileName);
				CloakNetwork.sendToServer(new PacketUpdateExternalCard(fileName));
			}
		}
		
		this.mc.displayGuiScreen((GuiScreen)null);

        if (this.mc.currentScreen == null)
        {
            this.mc.setIngameFocus();
        }
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		if(keyCode == 28)
			closeGui(true);
		super.keyTyped(typedChar, keyCode);
		name.textboxKeyTyped(typedChar, keyCode);
	}
	
	private long lastClick;
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(System.currentTimeMillis() - lastClick < 300 && this.imageList.getSlotIndexFromScreenCoords(mouseX, mouseY) == this.imageList.selectedPoint)
			closeGui(true);
		else
			this.imageList.mouseClicked(mouseX, mouseY, mouseButton);
		lastClick = System.currentTimeMillis();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
	    this.imageList.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	public void handleMouseInput() throws IOException 
	{
		super.handleMouseInput();
		this.imageList.handleMouseInput();
	}
	 
	 
}
