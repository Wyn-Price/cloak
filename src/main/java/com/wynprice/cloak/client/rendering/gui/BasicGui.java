package com.wynprice.cloak.client.rendering.gui;

import java.awt.Point;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.wynprice.cloak.client.rendering.CloakedModel;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.block.BlockStairs;
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
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.items.ItemStackHandler;

public class BasicGui extends GuiContainer
{
	public BasicGui(ItemStackHandler handler, IBlockState state, IBakedModel displayModel) {
		super(new ContainerBasicCloakingMachine(handler, state, displayModel));
	}
	
	private boolean clickedLastTick = false;
	
	private int selectedQuad = -1;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//TODO make it reliant on tilevar
		IBlockState modelState = Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
    	IBlockState renderState = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
    	ItemStack stack = new ItemStack(Blocks.STONE);
    	
    	IBakedModel bakedmodel = new CloakedModel(modelState, renderState);
    	GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(this.width / 2f, this.height / 2f, 100.0F + this.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(160F, 160F, 160F);
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0f, 0f, 0f), new Vector3f(0.7f, 0.7f, 0.7f)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(30, 0, 0), new Vector3f(0f, 0f, 0f), new Vector3f(currentZoom, currentZoom, currentZoom)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(currentRotation.y,  -currentRotation.x, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)), false);
                
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
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
        	this.selectedQuad = selectedQuad;
        
        clickedLastTick = false;
        GlStateManager.disableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
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
	
	private Point currentRotation = new Point(45, -30);
	
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
