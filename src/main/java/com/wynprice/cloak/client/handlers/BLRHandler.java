package com.wynprice.cloak.client.handlers;

import com.wynprice.brl.events.BRLGetStateEvent;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.world.CloakBlockAccess;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BLRHandler 
{
	@SubscribeEvent
	public void getState(BRLGetStateEvent event)
	{	
		
		event.setAccess(new CloakBlockAccess(event.getAccess()));
		
		if(Minecraft.getMinecraft().world.getBlockState(event.getPos()).getBlock() == CloakBlocks.CLOAK_BLOCK)
			event.setState(Minecraft.getMinecraft().world.getBlockState(event.getPos()));
	}
}
