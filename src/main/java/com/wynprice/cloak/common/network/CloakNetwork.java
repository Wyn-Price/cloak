package com.wynprice.cloak.common.network;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.network.packets.PacketFaceSelectionAdvancedGUI;
import com.wynprice.cloak.common.network.packets.PacketInitiateCloakingRecipe;
import com.wynprice.cloak.common.network.packets.PacketRemoveModificationList;
import com.wynprice.cloak.common.network.packets.PacketUpdateExternalCard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CloakNetwork 
{
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CloakMod.MODID);
	
	public static void preInit()
	{
		registerMessage(PacketRemoveModificationList.class, Side.SERVER);
		registerMessage(PacketFaceSelectionAdvancedGUI.class, Side.SERVER);
		registerMessage(PacketInitiateCloakingRecipe.class, Side.SERVER);
		registerMessage(PacketUpdateExternalCard.class, Side.SERVER);
	}
	
	private static int idCount = 0;
	
    public static void registerMessage(Class claz, Side recievingSide)
    {
    	INSTANCE.registerMessage(claz, claz, idCount++, recievingSide);
    }
    
	public static void sendToServer(IMessage message)
	{
		INSTANCE.sendToServer(message);
	}
	
	public static void sendToPlayer(EntityPlayer player, IMessage message)
	{
		if(!player.world.isRemote)
			INSTANCE.sendTo(message, (EntityPlayerMP) player);
	}
	
	public static void sendToPlayersInWorld(World world, IMessage message)
	{
		if(world == null)
			sendToAll(message);
		else if(!world.isRemote)
			INSTANCE.sendToDimension(message, world.provider.getDimension());
	}
	
	public static void sendToAll(IMessage message)
	{
		INSTANCE.sendToAll(message);
	}	
}
