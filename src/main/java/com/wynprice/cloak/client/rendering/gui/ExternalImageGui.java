package com.wynprice.cloak.client.rendering.gui;

import java.io.IOException;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketUpdateExternalCard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class ExternalImageGui extends GuiScreen
{
	private GuiTextField name;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		name = new GuiTextField(0, fontRenderer, this.width / 2 - 100, this.height / 2 + 50, 200, 20);
		name.setFocused(true);
		name.setCanLoseFocus(false);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		name.drawTextBox();
		int pos = 0;
		for(String fileName : ExternalImageHandler.RESOURCE_MAP.keySet())
			this.fontRenderer.drawString(fileName, pos++ * 10 + 10, pos * 10 + 10, 16777215);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		super.actionPerformed(button);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		if(keyCode == 28)
		{
			ResourceLocation location = ExternalImageHandler.RESOURCE_MAP.get(name.getText());
			if(location != null)
			{
				PacketUpdateExternalCard.setNBT(Minecraft.getMinecraft().player, name.getText());
				CloakNetwork.sendToServer(new PacketUpdateExternalCard(name.getText()));
			}
			
			this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
		}
		super.keyTyped(typedChar, keyCode);
		name.textboxKeyTyped(typedChar, keyCode);
	}
}
