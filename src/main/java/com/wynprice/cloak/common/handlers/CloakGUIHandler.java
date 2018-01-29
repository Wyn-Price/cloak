package com.wynprice.cloak.common.handlers;

import com.wynprice.cloak.client.rendering.gui.AdvancedGui;
import com.wynprice.cloak.client.rendering.gui.BasicGui;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CloakGUIHandler implements IGuiHandler 
{
	
	public final static int BASIC_CLOAKING_MACHINE = 0;
	public final static int ADVANCED_CLOAKING_MACHINE = 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return Container
	{
		if(ID == BASIC_CLOAKING_MACHINE)
			return new ContainerBasicCloakingMachine(player, new ItemStackHandler(2), false);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return GUI screen
	{
		if(ID == BASIC_CLOAKING_MACHINE)
			return new BasicGui(player, new ItemStackHandler(2));
		return null;
	}

}
