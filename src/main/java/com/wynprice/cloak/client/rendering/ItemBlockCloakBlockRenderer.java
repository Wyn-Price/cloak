package com.wynprice.cloak.client.rendering;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.cloak.client.rendering.models.CloakBlockItemModel;
import com.wynprice.cloak.client.rendering.models.ExternalCaptureModel;
import com.wynprice.cloak.client.rendering.models.quads.ExternalBakedQuad;
import com.wynprice.cloak.common.handlers.ExternalImageHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ItemBlockCloakBlockRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) 
	{
		CloakBlockItemModel model = CloakBlockItemModel.instance;
		Tessellator tessellator = Tessellator.getInstance();
	    BufferBuilder bufferbuilder = tessellator.getBuffer();

	    ArrayList<BakedQuad> quadList = new ArrayList<>();
    	quadList.addAll(model.getQuads((IBlockState)null, null, 0L));
	    for (EnumFacing enumfacing : EnumFacing.values())
	    	quadList.addAll(model.getQuads((IBlockState)null, enumfacing, 0L));
	  
	    for(BakedQuad quad : quadList)
	    {
		    bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
			Minecraft.getMinecraft().renderEngine.bindTexture(quad instanceof ExternalBakedQuad ? ((ExternalBakedQuad)quad).getLocation() : TextureMap.LOCATION_BLOCKS_TEXTURE);
			renderQuad(bufferbuilder, quad, model.getStack());
		    tessellator.draw();
	    }
	}
	
	private void renderQuad(BufferBuilder renderer, BakedQuad bakedquad, ItemStack stack)
    {
		int k = -1;

        if (bakedquad.hasTintIndex() && !(bakedquad instanceof ExternalBakedQuad))
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
	
	
	public static class FakeTileEntity extends TileEntity
	{
		
	}
}
