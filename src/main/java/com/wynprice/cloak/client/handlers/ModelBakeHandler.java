package com.wynprice.cloak.client.handlers;

import com.wynprice.brl.tcn.TCNZipHandler;
import com.wynprice.brl.tcn.TJR;
import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.TileEntityCloakingMachineRenderer;
import com.wynprice.cloak.client.rendering.models.BaseModelProxy;
import com.wynprice.cloak.client.rendering.models.BlockCaptureModel;
import com.wynprice.cloak.client.rendering.models.CloakBlockItemModel;
import com.wynprice.cloak.client.rendering.models.ExternalCaptureModel;
import com.wynprice.cloak.client.rendering.models.LiquidCaptureModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeHandler 
{
	
	private static final ResourceLocation CLOAKING_MACHINE_MODEL_LOCATION = new ResourceLocation(CloakMod.MODID, "models/block/cloaking_machine_render.json");
	
	@SubscribeEvent
	public void onBaked(ModelBakeEvent event)
	{
		for(ModelResourceLocation location : event.getModelRegistry().getKeys())
			if(location.equals(new ModelResourceLocation(new ResourceLocation("minecraft:builtin/missing"), "missing")))
				TJR.setMissingNo(event.getModelRegistry().getObject(location));
		for(ModelResourceLocation location : event.getModelRegistry().getKeys())
			if(location.getResourceDomain().equals(CloakMod.MODID))
			{
				String loc = location.getResourcePath();
				if(loc.equals("block_capture"))
					event.getModelRegistry().putObject(location, new BlockCaptureModel(event.getModelRegistry().getObject(location)));
				if(loc.equals("liquid_capture"))
					event.getModelRegistry().putObject(location, new LiquidCaptureModel(event.getModelRegistry().getObject(location)));
				if(loc.equals("cloak_block"))
					event.getModelRegistry().putObject(location, new CloakBlockItemModel(event.getModelRegistry().getObject(location)));
				if(loc.equals("external_capture"))
					event.getModelRegistry().putObject(location, new ExternalCaptureModel(event.getModelRegistry().getObject(location)));
//				if(loc.equals("cloaking_machine"))
//					event.getModelRegistry().putObject(location, TCNZipHandler.getZipFile(new ResourceLocation("cloak:block/cloaking_machine"))
//								.bake());
			}

	}
}
