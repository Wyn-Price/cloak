package com.wynprice.cloak.client.rendering.gui;

import java.awt.Point;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.wynprice.cloak.client.rendering.CloakedModel;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.items.ItemStackHandler;

public class BasicGui extends GuiContainer
{
	public BasicGui(EntityPlayer player, ItemStackHandler handler) {
		super(new ContainerBasicCloakingMachine(player, handler));
	}
	
	private boolean clickedLastTick = false;
	
	private int selectedQuad = -1;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
    	
		renderCenterBlock(mouseX, mouseY);
    	
	}	
	
	private void renderCenterBlock(int mouseX, int mouseY)
	{
		if(!this.inventorySlots.getSlot(37).getHasStack() || !this.inventorySlots.getSlot(36).getHasStack()) return;
		NBTTagCompound modelStackNBT = this.inventorySlots.getSlot(this.inventorySlots.inventorySlots.size() - 1).getStack().getSubCompound("capture_info");
		NBTTagCompound stack36NBT = this.inventorySlots.getSlot(36).getStack().getSubCompound("capture_info");

		
		//TODO make it reliant on tilevar
		IBlockState modelState = Block.REGISTRY.getObject(new ResourceLocation(modelStackNBT.getString("block"))).getStateFromMeta(modelStackNBT.getInteger("meta"));
		IBlockState renderState = Block.REGISTRY.getObject(new ResourceLocation(stack36NBT.getString("block"))).getStateFromMeta(stack36NBT.getInteger("meta"));
		ItemStackHandler localHandler = new ItemStackHandler(1);
		localHandler.deserializeNBT(stack36NBT.getCompoundTag("item"));
		ItemStack stack = localHandler.getStackInSlot(0);
		//~~~~~~~~
		
		IBakedModel bakedmodel = new CloakedModel(modelState, renderState);
    	GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(this.width / 2f, this.height / 2f, 100.0F + this.zLevel);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(160F, 160F, 160F);
        ForgeHooksClient.multiplyCurrentGlMatrix(new Matrix4f(-0.44194168f, 0.0f, 0.44194168f, 0.0f, 0.22097087f, 0.5412659f, 0.22097084f, 0.0f, -0.38273275f, 0.31249997f, -0.38273272f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0f, 0f, 0f), new Vector3f(0.7f, 0.7f, 0.7f)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(30, 0, 0), new Vector3f(0f, 0f, 0f), new Vector3f(currentZoom, currentZoom, currentZoom)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(currentRotation.y,  -currentRotation.x, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)), false);
                
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        List<BakedQuad> quadList = new ArrayList<>(bakedmodel.getQuads((IBlockState)null, (EnumFacing)null, 0L));
        for (EnumFacing enumfacing : EnumFacing.values())
        	quadList.addAll(bakedmodel.getQuads(null, enumfacing, 0L));
        int selectedQuad = -1;        
        int underMouseColor = getColorUnderMouse();
        for(int i = 0; i < quadList.size(); i++)
        {
            RenderHelper.enableStandardItemLighting();
        	BakedQuad quad = quadList.get(i);
            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        	renderQuad(bufferbuilder, quad, stack, -1);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            
            if(i == this.selectedQuad)
            {
                RenderHelper.disableStandardItemLighting();
            	for(int l = 0; l < bufferbuilder.getVertexCount() + 1; l++)
            		bufferbuilder.putColorMultiplier(1f, 0.301f, 0.129f, l);
            }
            
            tessellator.draw();
            
            if(clickedLastTick)
        	{
	        	int mouseColor = getColorUnderMouse();
	        	if(mouseColor != underMouseColor)
	        	{
	        		selectedQuad = i;
	        		underMouseColor = mouseColor;
	        	}
        	}
        }
        if(selectedQuad != - 1 && clickedLastTick)
        {
        	this.selectedQuad = this.selectedQuad == selectedQuad ? -1 : selectedQuad;
        }
        
        clickedLastTick = false;
        GlStateManager.disableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.shadeModel(7424);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}
	
	private int getColorUnderMouse()
	{
		int width = Minecraft.getMinecraft().displayWidth;
        int height = Minecraft.getMinecraft().displayHeight;
        if(OpenGlHelper.isFramebufferEnabled())
        {
        	width = Minecraft.getMinecraft().getFramebuffer().framebufferTextureWidth;
        	height = Minecraft.getMinecraft().getFramebuffer().framebufferTextureWidth;
        }
        int a = width * height;
        IntBuffer intbuffer = BufferUtils.createIntBuffer(a);
        int[] ints = new int[a];
        GlStateManager.glPixelStorei(3333, 1);
        GlStateManager.glPixelStorei(3317, 1);
        if (OpenGlHelper.isFramebufferEnabled())
        {
            GlStateManager.bindTexture(Minecraft.getMinecraft().getFramebuffer().framebufferTexture);
            GlStateManager.glGetTexImage(3553, 0, 32993, 33639, intbuffer);
        }
        else
            GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, intbuffer);

        
        intbuffer.get(ints);
        return ints[Mouse.getY() * width + Mouse.getX()];
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
	}
	
	private void renderQuad(BufferBuilder renderer, BakedQuad bakedquad, ItemStack stack, int color)
    {
		int k = color;

        if (bakedquad.hasTintIndex())
        {
            k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

            if (EntityRenderer.anaglyphEnable)
            {
                k = TextureUtil.anaglyphColor(k);
            }

            k = k | -16777216;
        }

        net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
    }

	
	
	
	private Point lastMouseClicked = new Point(0, 0);
	
	private Point currentRotation = new Point(315, -30);
	
	private float currentZoom = 1f;
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException 
	{
		if(mouseButton == 0)
			clickedLastTick = true;
		if(mouseButton == 1)
			this.lastMouseClicked = new Point(mouseX, mouseY);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) 
	{
		if(clickedMouseButton == 1)
		{
			currentRotation = new Point(currentRotation.x + lastMouseClicked.x - mouseX, currentRotation.y + lastMouseClicked.y - mouseY);
			this.lastMouseClicked = new Point(mouseX, mouseY);
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		if(Mouse.getEventDWheel() > 0 && currentZoom < 4.9f)
			currentZoom += 0.1f;
		else if(Mouse.getEventDWheel() < 0 && currentZoom > 0.1f)
			currentZoom -= 0.1f;
		super.handleMouseInput();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		if(keyCode == 1)
			Minecraft.getMinecraft().displayGuiScreen(null);
		super.keyTyped(typedChar, keyCode);
	}
}
