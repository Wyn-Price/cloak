package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.BasicCloakingMachineModel;
import com.wynprice.cloak.client.rendering.models.BlockCaptureModel;
import com.wynprice.cloak.client.rendering.models.CloakBlockItemModel;
import com.wynprice.cloak.client.rendering.models.LiquidCaptureModel;
import com.wynprice.cloak.common.blocks.BasicCloakingMachine;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeHandler 
{
	@SubscribeEvent
	public void onBaked(ModelBakeEvent event)
	{
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
			}

	}
}
