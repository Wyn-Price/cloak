package com.wynprice.cloak.common.handlers;

import java.util.HashMap;

import com.wynprice.cloak.client.rendering.gui.AdvancedGui;
import com.wynprice.cloak.client.rendering.gui.BasicGui;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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
		BlockPos position = new BlockPos(x, y, z);
		if(ID == BASIC_CLOAKING_MACHINE && world.getTileEntity(position) instanceof TileEntityCloakingMachine) {
			return new ContainerBasicCloakingMachine(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		}
		
		else if(ID == ADVANCED_CLOAKING_MACHINE)
			return new ContainerBasicCloakingMachine(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return GUI screen
	{
		BlockPos position = new BlockPos(x, y, z);
		if(ID == BASIC_CLOAKING_MACHINE && world.getTileEntity(position) instanceof TileEntityCloakingMachine) {
			return new BasicGui(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		}
		else if(ID == ADVANCED_CLOAKING_MACHINE)
			return new AdvancedGui(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		return null;
	}

}
