package com.wynprice.cloak.common.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CloakGUIHandler implements IGuiHandler 
{
	
	public final static int BASIC_CLOAKING_MACHINE = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return Container
	{
		if(ID == BASIC_CLOAKING_MACHINE)
			;
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return GUI screen
	{
		if(ID == BASIC_CLOAKING_MACHINE)
			;
		return null;
	}

}
