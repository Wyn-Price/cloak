package com.wynprice.cloak.client.rendering.gui;

import java.awt.Color;
import java.awt.Point;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.items.ICaptureCard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

public class CaptureCardDyeGUI extends GuiScreen
{

	private Point currentColorWheel = new Point(-1, -1);
	private int prevColor = Color.WHITE.getRGB();
	
	private final ItemStack stack;
	
	public CaptureCardDyeGUI(ItemStack stack) 
	{
		this.stack = stack;
	}
	
	private GuiSlider saturation_slider;
	private GuiSlider lightness_slider;

	
	@Override
	public void initGui() 
	{
		this.saturation_slider = new GuiSlider(0, this.width / 2 - 100, this.height / 2 - 100, 200, 20, I18n.format("gui.cloak.strength"), "", 0, 100, 50, false, true);
		this.lightness_slider = new GuiSlider(1, this.width / 2 - 100, this.height / 2 + 80, 200, 20, I18n.format("gui.cloak.lightness"), "", 0, 100, 100, false, true);
		addButton(saturation_slider);
		addButton(lightness_slider);
		super.initGui();
		
		this.currentColorWheel = new Point(Minecraft.getMinecraft().displayWidth / 2, Minecraft.getMinecraft().displayHeight / 2);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();		
		
		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();
		
		int colorPre = CloakingMachineGUI.getColorUnderMouse();
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/color_wheel.png"));
		this.drawModalRectWithCustomSizedTexture(this.width / 2 - 64, this.height / 2 - 64, 0, 0, 128, 128, 128, 128);
		GlStateManager.color(1f, 1f, 1f);

		int newColor = CloakingMachineGUI.getColorUnderMouse();
		
//		
		if(colorPre != newColor && Mouse.isButtonDown(0))
		{
			this.currentColorWheel = new Point(Mouse.getX(), Mouse.getY());
			this.saturation_slider.setValue((Math.hypot(this.currentColorWheel.x - Minecraft.getMinecraft().displayWidth / 2, this.currentColorWheel.y - Minecraft.getMinecraft().displayHeight / 2)) / 128 * 100);
			this.saturation_slider.updateSlider();
		}

		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/color_wheel_border.png"));
		this.drawModalRectWithCustomSizedTexture(this.width / 2 - 65, this.height / 2 - 65, 0, 0, 130, 130, 130, 130);
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/color_wheel_cursor.png"));
		this.drawModalRectWithCustomSizedTexture(currentColorWheel.x * new ScaledResolution(this.mc).getScaledWidth() / this.mc.displayWidth - 4, 
				new ScaledResolution(this.mc).getScaledHeight() - currentColorWheel.y * new ScaledResolution(this.mc).getScaledHeight() / this.mc.displayHeight - 1 - 4, 0, 0, 8, 8, 8, 8);
		
		
		if(stack.getItem() instanceof ICaptureCard)
		{
			IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
	        int[] ints = new int[1];
	        GlStateManager.glReadPixels(currentColorWheel.x, currentColorWheel.y, 1, 1, 32993, 33639, intbuffer);
	        intbuffer.get(ints);
	        Color c = new Color(ints[0]);
			int tintOver = Color.HSBtoRGB(Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0], this.saturation_slider.getValueInt() / 100f, this.lightness_slider.getValueInt() / 100f);
			((ICaptureCard)stack.getItem()).renderOntoColorWheel(this, stack, tintOver, this.width / 2 - 150, this.height / 2 - 25);
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);		

	}

}
