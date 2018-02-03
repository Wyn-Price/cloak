package com.wynprice.cloak.client.rendering.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.command.CommandBase;
import net.minecraft.util.math.MathHelper;

public class GuiExternalImageList extends GuiListExtended
{

	private final ArrayList<GuiExternalImageEntry> entries = new ArrayList<>();
	private final ArrayList<GuiExternalImageEntry> searchEntiries = new ArrayList<>();
	public final ExternalImageGui parent;
	public int selectedPoint = -1;
	
	public GuiExternalImageList(Minecraft mcIn, ExternalImageGui parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) 
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		for(String string : ExternalImageHandler.RESOURCE_MAP.keySet())
			entries.add(new GuiExternalImageEntry(string, this));
		
		this.parent = parent;
	}

	@Override
	public IGuiListEntry getListEntry(int index) {
		return searchEntiries.get(index);
	}
	
	@Override
	public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) 
	{
		searchEntiries.clear();
		if(parent.name.getText().isEmpty() || parent.name.getText().startsWith(" "))
			searchEntiries.addAll(entries);
		else
		{
			HashMap<String, GuiExternalImageEntry> stringMap = new HashMap<>();
			for(GuiExternalImageEntry entry : this.entries)
				stringMap.put(entry.fileName, entry);
			List<String> compleations = CommandBase.getListOfStringsMatchingLastWord(new String[]{parent.name.getText()}, stringMap.keySet());
			Collections.sort(compleations);
			Collections.reverse(compleations);
			for(String string : compleations)
				searchEntiries.add(stringMap.get(string));
		}
		if (this.visible)
        {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GuiScreen.drawRect(this.width / 2 - 135, this.top - 10, this.width / 2 + 135, this.bottom + 10, -0x50BBBBBB);
            
            GuiScreen.drawRect(this.width / 2 - 136, this.top - 11, this.width / 2 + 136, this.top-9, -0x00FFFFFF); //UP
            GuiScreen.drawRect(this.width / 2 - 136, this.bottom + 11, this.width / 2 + 136, this.bottom + 9, -0x00FFFFFF); //DOWN
            GuiScreen.drawRect(this.width / 2 - 136, this.top - 10, this.width / 2 - 134, this.bottom + 10, -0x00FFFFFF);//LEFT
            GuiScreen.drawRect(this.width / 2 + 136, this.top - 10, this.width / 2 + 134, this.bottom + 10, -0x00FFFFFF);//LEFT

            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(k, l, tessellator);
            }

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            int j1 = this.getMaxScroll();

            if (j1 > 0)
            {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
                int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                if (l1 < this.top)
                {
                    l1 = this.top;
                }
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double)i, (double)this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double)j, (double)this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double)j, (double)this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double)i, (double)this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double)i, (double)(l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double)j, (double)(l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double)j, (double)l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double)i, (double)(l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double)(j - 1), (double)(l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double)(j - 1), (double)l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double)i, (double)l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
	}
	
	
	@Override
	protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn, float partialTicks) 
	{
		 int i = this.getSize();
	     Tessellator tessellator = Tessellator.getInstance();
	     BufferBuilder bufferbuilder = tessellator.getBuffer();

	     for (int j = 0; j < i; ++j)
	     {
	         int k = insideTop + j * this.slotHeight + this.headerPadding;
	         int l = this.slotHeight - 4;

	         if (k > this.bottom || k + l < this.top)
	         {
	             this.updateItemPos(j, insideLeft, k, partialTicks);
	         }

	         if (this.showSelectionBox && j == selectedPoint)
	         {
	             int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
	             int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
	             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	             GlStateManager.disableTexture2D();
	             bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	             bufferbuilder.pos((double)i1, (double)(k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	             bufferbuilder.pos((double)j1, (double)(k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	             bufferbuilder.pos((double)j1, (double)(k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	             bufferbuilder.pos((double)i1, (double)(k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	             bufferbuilder.pos((double)(i1 + 1), (double)(k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	             bufferbuilder.pos((double)(j1 - 1), (double)(k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	             bufferbuilder.pos((double)(j1 - 1), (double)(k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	             bufferbuilder.pos((double)(i1 + 1), (double)(k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	             tessellator.draw();
	             GlStateManager.enableTexture2D();
	         }
	         if (k + l < this.bottom && k + l > this.top)
	         {
		         this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
	         }
	     }
	}
	
	
	@Override
	protected int getSize() {
		return searchEntiries.size();
	}
}
