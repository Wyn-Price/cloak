package com.wynprice.cloak.common.items;

import java.awt.Color;

import javax.vecmath.Matrix4f;

import org.lwjgl.util.vector.Vector3f;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.gui.CaptureCardDyeGUI;
import com.wynprice.cloak.client.rendering.tjr.TJR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class ItemCaptureBlock extends Item implements ICaptureCard
{
	public ItemCaptureBlock() 
	{
		setRegistryName("block_capture");
		setUnlocalizedName("block_capture");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		if(!playerIn.getHeldItem(handIn).hasTagCompound()) playerIn.getHeldItem(handIn).setTagCompound(new NBTTagCompound());
		if(playerIn.isSneaking())
			playerIn.getHeldItem(handIn).getTagCompound().setTag("capture_info", new NBTTagCompound());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{		
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound nbt = new NBTTagCompound();
		IBlockState state = worldIn.getBlockState(pos).getActualState(worldIn, pos);
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, state.getBlock().getPickBlock(state, worldIn.rayTraceBlocks(player.getPositionVector(), new Vec3d(pos)), worldIn, pos, player));
		
		NBTUtil.writeBlockState(nbt, state);
		nbt.setTag("item", handler.serializeNBT());

		if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("capture_info", nbt);
		return EnumActionResult.SUCCESS;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderOntoColorWheel(CaptureCardDyeGUI parent, ItemStack stack, int color, int x, int y)
	{
				
		IBakedModel bakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(NBTUtil.readBlockState(stack.getOrCreateSubCompound("capture_info")));
		GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(x, y, 100.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(50F, 50F, 50F);
        GlStateManager.translate(0, -0.4F, 0);
        ForgeHooksClient.multiplyCurrentGlMatrix(new Matrix4f(-0.44194168f, 0.0f, 0.44194168f, 0.0f, 0.22097087f, 0.5412659f, 0.22097084f, 0.0f, -0.38273275f, 0.31249997f, -0.38273272f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
        ItemCameraTransforms.applyTransformSide(new ItemTransformVec3f(new Vector3f(0, System.currentTimeMillis() / 10l % 360, 0), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)), false);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        EnumFacing[] facingList = new EnumFacing[EnumFacing.VALUES.length + 1];
        for(int i = 0; i < EnumFacing.VALUES.length; i++)
        	facingList[i] = EnumFacing.VALUES[i];
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
        for(EnumFacing facing : facingList)
        	for(BakedQuad quad : bakedmodel.getQuads(null, facing, 0L))
        	{
        		int retColor = color;
            	int color2 = -1;
                if (quad.hasTintIndex())
                {
                	try
                	{
                		try
                		{
                			color2 = Minecraft.getMinecraft().getBlockColors().colorMultiplier(NBTUtil.readBlockState(stack.getOrCreateSubCompound("capture_info")), Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), quad.getTintIndex());
                		}
                		catch (Throwable t) 
                    	{
                			ItemStackHandler handler = new ItemStackHandler();
                    		handler.deserializeNBT(stack.getOrCreateSubCompound("capture_info").getCompoundTag("item"));
                    		if(handler.getSlots() > 0)
    	                		color2 = Minecraft.getMinecraft().getItemColors().colorMultiplier(handler.getStackInSlot(0), quad.getTintIndex());
                    	}
                		
                	}
                	catch (Throwable t) 
                	{
                		;
        			}
                    if (EntityRenderer.anaglyphEnable)
                    	color2 = TextureUtil.anaglyphColor(color2);

                    color2 = color2 | -16777216;
                    if(color == Color.WHITE.getRGB())
                    	retColor = color2;
                    else
                    {
	                    int a1 = (color >> 24 & 0xff);
	                    int r1 = ((color & 0xff0000) >> 16);
	                    int g1 = ((color & 0xff00) >> 8);
	                    int b1 = (color & 0xff);
	                    int a2 = (color2 >> 24 & 0xff);
	                    int r2 = ((color2 & 0xff0000) >> 16);
	                    int g2 = ((color2 & 0xff00) >> 8);
	                    int b2 = (color2 & 0xff);
	                    int a = (int)((a1 * 0.5f) + (a2 * 0.5f));
	                    int r = (int)((r1 * 0.5f) + (r2 * 0.5f));
	                    int g = (int)((g1 * 0.5f) + (g2 * 0.5f));
	                    int b = (int)((b1 * 0.5f) + (b2 * 0.5f));
	                    retColor = new Color( a << 24 | r << 16 | g << 8 | b).getRGB();
                    }
                }
                net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(bufferbuilder, quad, retColor);
                
        	}
        tessellator.draw();
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
}
