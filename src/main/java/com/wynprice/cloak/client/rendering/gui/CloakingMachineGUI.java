package com.wynprice.cloak.client.rendering.gui;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.handlers.TextureStitchHandler;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketFaceSelectionAdvancedGUI;
import com.wynprice.cloak.common.network.packets.PacketInitiateCloakingRecipe;
import com.wynprice.cloak.common.network.packets.PacketRemoveModificationList;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.items.ItemStackHandler;

public class CloakingMachineGUI extends GuiContainer
{
	
	private boolean previous36;
	private boolean previous37;
	private boolean hasOpened;
	private EntityPlayer player;
	private boolean clickedLastTick = false;
	private int selectedQuad = -1;
	private Point lastMouseClicked = new Point(0, 0);
	private Point currentRotation = new Point(315, -30);
	
	public CloakingMachineGUI(EntityPlayer player, TileEntityCloakingMachine tileEntity) 
	{
		super(new ContainerBasicCloakingMachine(player, tileEntity));
		this.xSize = 327;
		this.ySize = 219;
	}
	
	@Override
	public void updateScreen() 
	{
		if(previous36 != this.inventorySlots.getSlot(36).getHasStack() || previous37 != this.inventorySlots.getSlot(37).getHasStack())
		{
			previous36 = this.inventorySlots.getSlot(36).getHasStack();
			previous37 = this.inventorySlots.getSlot(37).getHasStack();
			
			if(hasOpened)
			{
				PacketRemoveModificationList.updateContainer((ContainerBasicCloakingMachine) this.inventorySlots, Minecraft.getMinecraft().player);
				CloakNetwork.sendToServer(new PacketRemoveModificationList());
			}
		}
		hasOpened = true;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/model-selection.png"));
		this.drawModalRectWithCustomSizedTexture(this.guiLeft + 10 + 76, this.guiTop + 15, 0, 0, 11, 12, 11, 12);
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/face-selection.png"));
		this.drawModalRectWithCustomSizedTexture(this.guiLeft + 154 + 76, this.guiTop + 15, 0, 0, 11, 12, 11, 12);
		
		if(this.inventorySlots.getSlot(40).isEnabled())
		{
			Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/one-face-selection.png"));
			this.drawModalRectWithCustomSizedTexture(this.guiLeft + 172 + 78, this.guiTop + 15, 0, 0, 11, 12, 11, 12);
		}
		
		
		GlStateManager.enableLighting();

		
		renderCenterBlock(mouseX, mouseY);
		
		this.renderHoveredToolTip(mouseX, mouseY);

	}	
	
	@Override
	public void initGui() 
	{
		addButton(new GuiButton(0, this.width / 2 + 81, this.height / 2 - 45, 79, 20, I18n.format("gui.cloak.advance")));
		super.initGui();
	}
	
	protected CloakedModel createModel(IBlockState modelState, IBlockState basicRenderState)
	{
		Pair<HashMap<Integer, IBlockState>, HashMap<Integer, ResourceLocation>> pair = ((ContainerBasicCloakingMachine)this.inventorySlots).getBlockStateMap();
		CloakedModel model = new CloakedModel(modelState, basicRenderState, pair.getLeft());
		model.setExternalOverrideList(pair.getRight());
		return model;
	}
	
	protected HashMap<Integer, ItemStack> getInventoryColors(CloakedModel model, ItemStack defualt)
	{
		HashMap<Integer, ItemStack> finalMap = new HashMap<>();
		List<BakedQuad> quadList = model.getIndentifierList();
        for(int i = 0; i < quadList.size(); i++)
        	finalMap.put(i, defualt);
        HashMap<Integer, ItemStack> map = finalMap;
		ContainerBasicCloakingMachine container = ((ContainerBasicCloakingMachine)this.inventorySlots);
		for(int i : container.modification_list.keySet())
			if(container.modification_list.get(i) != null && !container.modification_list.get(i).isEmpty())
			{
				ItemStackHandler handler = new ItemStackHandler(1);
				handler.deserializeNBT(container.modification_list.get(i).getSubCompound("capture_info").getCompoundTag("item"));
				map.put(i, handler.getStackInSlot(0));
			}
		return map;
	}
	
	private void renderCenterBlock(int mouseX, int mouseY)
	{
		
		if(!this.inventorySlots.getSlot(37).getHasStack() || !this.inventorySlots.getSlot(36).getHasStack()) return;
		NBTTagCompound stack37NBT = this.inventorySlots.getSlot(37).getStack().getSubCompound("capture_info");
		IBlockState modelState = NBTUtil.readBlockState(this.inventorySlots.getSlot(36).getStack().getSubCompound("capture_info"));
		IBlockState renderState = NBTUtil.readBlockState(stack37NBT);
		ItemStackHandler localHandler = new ItemStackHandler(1);
		localHandler.deserializeNBT(stack37NBT.getCompoundTag("item"));
		ItemStack stack = localHandler.getStackInSlot(0);
		CloakedModel bakedmodel = createModel(modelState, renderState);
		bakedmodel.setBaseTextureExternal(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(this.inventorySlots.getSlot(37).getStack().getOrCreateSubCompound("capture_info").getString("external_image")));

		HashMap<Integer, ItemStack> inventoryColors = getInventoryColors(bakedmodel, stack);
		
    	GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(this.width / 2f, this.height / 2f, 100.0F + this.zLevel);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(160F, 160F, 160F);
        GlStateManager.translate(0, 0.15F, 0);
        ForgeHooksClient.multiplyCurrentGlMatrix(new Matrix4f(-0.44194168f, 0.0f, 0.44194168f, 0.0f, 0.22097087f, 0.5412659f, 0.22097084f, 0.0f, -0.38273275f, 0.31249997f, -0.38273272f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0f, 0f, 0f), new Vector3f(0.5f, 0.5f, 0.5f)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(30, 0, 0), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)), false);
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(currentRotation.y,  -currentRotation.x, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)), false);
                
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        List<BakedQuad> quadList = bakedmodel.getFullList();
        int selectedQuad = -1;      
        int underMouseColor = -1;
        if(clickedLastTick)
        	underMouseColor = getColorUnderMouse();
        for(int i = 0; i < quadList.size(); i++)
        {
            RenderHelper.enableStandardItemLighting();
        	BakedQuad quad = quadList.get(i);
            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        	renderQuad(bufferbuilder, quad, bakedmodel.isParentSelected(quad, this.selectedQuad) ? new ItemStack(Blocks.STONE) : inventoryColors.get(bakedmodel.getParentID(quad)));
        	
        	int[] vertexData = new int[quad.getVertexData().length];
        	System.arraycopy(quad.getVertexData(), 0, vertexData, 0, vertexData.length);
        	BlockFaceUV faceUV = new BlockFaceUV(new float[]{0, 0, 1, 1}, 0);
        	
        	for(int j = 0; j < 4; j++)
        	{
        		int l = (vertexData.length / 4) * j;
        		vertexData[l + 4] = Float.floatToRawIntBits(TextureStitchHandler.blockrender_overlay.getInterpolatedU((double)faceUV.getVertexU(j) * .999 + faceUV.getVertexU((j + 2) % 4) * .001));
        		vertexData[l + 5] = Float.floatToRawIntBits(TextureStitchHandler.blockrender_overlay.getInterpolatedV((double)faceUV.getVertexV(j) * .999 + faceUV.getVertexV((j + 2) % 4) * .001));
        	}
        	
        	BakedQuad colorlessQuad = new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
			Minecraft.getMinecraft().renderEngine.bindTexture(quad instanceof ExternalBakedQuad ? ((ExternalBakedQuad)quad).getLocation() : TextureMap.LOCATION_BLOCKS_TEXTURE);

            if(bakedmodel.isParentSelected(quad, this.selectedQuad))
            {
                RenderHelper.disableStandardItemLighting();
            	for(int l = 0; l < bufferbuilder.getVertexCount() + 1; l++)
            		bufferbuilder.putColorMultiplier(1f, 0.301f, 0.129f, l);
            }
            
            tessellator.draw();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            renderQuad(bufferbuilder, colorlessQuad, new ItemStack(Blocks.STONE));
            tessellator.draw();
            if(clickedLastTick)
        	{
	        	int mouseColor = getColorUnderMouse();
	        	if(mouseColor != underMouseColor)
	        	{
	        		selectedQuad = bakedmodel.getParentID(quad);
	        		underMouseColor = mouseColor;
	        	}
        	}
        }
        if(selectedQuad != - 1 && clickedLastTick)
        {
        	int preSelectedQuad = this.selectedQuad;
        	this.selectedQuad = this.selectedQuad == selectedQuad ? -1 : selectedQuad;
    		onFaceSelected(this.selectedQuad, preSelectedQuad);
        }
        
        clickedLastTick = false;
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.shadeModel(7424);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}
	
	protected void onFaceSelected(int slotID, int OldSlotID)
	{
		PacketFaceSelectionAdvancedGUI.setContainerFace((ContainerBasicCloakingMachine) this.inventorySlots, slotID, OldSlotID);
		CloakNetwork.sendToServer(new PacketFaceSelectionAdvancedGUI(slotID, OldSlotID));
	}
	
	public static int getColorUnderMouse()
	{
        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        int[] ints = new int[1];
        GlStateManager.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, 32993, 33639, intbuffer);
        intbuffer.get(ints);
        return ints[0];
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		this.drawDefaultBackground();
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/cloaking_machine.png"));
        this.drawModalRectWithCustomSizedTexture((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(CloakMod.MODID, "textures/gui/widgits/slot_background.png"));
		for(Slot slot : this.inventorySlots.inventorySlots)
			if(slot.isEnabled())
				this.drawModalRectWithCustomSizedTexture(this.guiLeft + slot.xPos - 1, this.guiTop + slot.yPos - 1, 0, 0, 18, 18, 18, 18);
	}
	
	private void renderQuad(BufferBuilder renderer, BakedQuad bakedquad, ItemStack stack)
    {
		int k = -1;

        if (bakedquad.hasTintIndex())
        {
        	try
        	{
        		k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());
        	}
        	catch (Throwable t) 
        	{
        		;
			}
            if (EntityRenderer.anaglyphEnable)
            {
                k = TextureUtil.anaglyphColor(k);
            }

            k = k | -16777216;
        }

        net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
    }
		
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
	protected void keyTyped(char typedChar, int keyCode) throws IOException 
	{
		if(keyCode == 1)
			Minecraft.getMinecraft().displayGuiScreen(null);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		if(button.id == 0 && this.inventorySlots.getSlot(38).getHasStack())
		{
			PacketInitiateCloakingRecipe.doRecipe((ContainerBasicCloakingMachine) this.inventorySlots);
			CloakNetwork.sendToServer(new PacketInitiateCloakingRecipe());
		}
		super.actionPerformed(button);
	}
}
