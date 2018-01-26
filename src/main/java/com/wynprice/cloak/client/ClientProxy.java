package com.wynprice.cloak.client;

import com.wynprice.cloak.client.handlers.ModelBakeHandler;
import com.wynprice.cloak.common.CloakItems;
import com.wynprice.cloak.common.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.ItemStackHandler;
import scala.collection.parallel.ParIterableLike.Min;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
		CloakItems.regRenders();
		registerHandlers();
	}
	
	@Override
	public void init(FMLInitializationEvent event) 
	{
		super.init(event);
		registerItemColors();
	}
	
	private void registerHandlers()
	{
		Object[] handlers = 
			{
					new ModelBakeHandler()
			};
		
		for(Object o : handlers)
    		MinecraftForge.EVENT_BUS.register(o);
	}
	
	private void registerItemColors()
	{
		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		ic.registerItemColorHandler(new IItemColor() {
			
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				ItemStackHandler handler = new ItemStackHandler(1);
				handler.deserializeNBT(stack.getOrCreateSubCompound("capture_info").getCompoundTag("item"));
				return tintIndex == 0 ? -1 : handler.getStackInSlot(0).isEmpty() ? -1 : Minecraft.getMinecraft().getItemColors().colorMultiplier(handler.getStackInSlot(0), tintIndex - 1);
			}
		}, CloakItems.BLOCKSTATE_CARD);
	}
}
