package com.wynprice.cloak.common.handlers;

import com.wynprice.cloak.client.rendering.gui.CloakingMachineGUI;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CloakGUIHandler implements IGuiHandler 
{
	
	public final static int CLOAKING_MACHINE = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return Container
	{
		BlockPos position = new BlockPos(x, y, z);
		
		if(ID == CLOAKING_MACHINE && world.getTileEntity(position) instanceof TileEntityCloakingMachine)
			return new ContainerBasicCloakingMachine(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) //Return GUI screen
	{
		BlockPos position = new BlockPos(x, y, z);
		
		if(ID == CLOAKING_MACHINE && world.getTileEntity(position) instanceof TileEntityCloakingMachine)
			return new CloakingMachineGUI(player, (TileEntityCloakingMachine)world.getTileEntity(position));
		
		return null;
	}

}
