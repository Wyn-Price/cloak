package com.wynprice.cloak.client.rendering;

import java.util.List;

import com.wynprice.cloak.client.rendering.models.ExternalCaptureModel;
import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ExternalCaptureCardRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) 
	{
		ExternalCaptureModel model = ExternalCaptureModel.instance;
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder bufferbuilder = tessellator.getBuffer();
	    bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

	    for (EnumFacing enumfacing : EnumFacing.values())
	    {
	        this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), model.getStack());
	    }

	    this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), model.getStack());
	    tessellator.draw();

		ResourceLocation location  = ExternalImageHandler.RESOURCE_MAP.get(model.getStack().getOrCreateSubCompound("capture_info").getString("external_image"));
		if(location != null)
		{
		    bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
			Minecraft.getMinecraft().renderEngine.bindTexture(location);
			this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 1L), model.getStack());
		    tessellator.draw();
		}
	}
	
	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, ItemStack stack)
    {
        int i = 0;
        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = quads.get(i);
            int k = -1;

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
    }
	
	
	public static class FakeTileEntity extends TileEntity
	{
		
	}
}
