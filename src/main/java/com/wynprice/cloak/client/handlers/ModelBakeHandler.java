package com.wynprice.cloak.client.handlers;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.BasicCloakingMachineModel;
import com.wynprice.cloak.client.rendering.BlockCaptureModel;
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
			}

	}
}
