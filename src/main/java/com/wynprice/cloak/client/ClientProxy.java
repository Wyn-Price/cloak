package com.wynprice.cloak.client;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.brl.api.BRLRegistry;
import com.wynprice.cloak.client.handlers.CaptureCardHandler;
import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.handlers.ModelBakeHandler;
import com.wynprice.cloak.client.handlers.ParticleHandler;
import com.wynprice.cloak.client.handlers.TextureStitchHandler;
import com.wynprice.cloak.client.rendering.CloakBlockBRLFactory;
import com.wynprice.cloak.client.rendering.ExternalCaptureCardRenderer;
import com.wynprice.cloak.client.rendering.ItemBlockCloakBlockRenderer;
import com.wynprice.cloak.client.rendering.TileEntityCloakingMachineRenderer;
import com.wynprice.cloak.client.rendering.world.CloakedRenderChunkFactory;
import com.wynprice.cloak.common.CommonProxy;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.registries.CloakItems;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.ItemStackHandler;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
		CloakItems.regRenders();
		CloakBlocks.regRenders();
		
		registerHandlers();
		registerTileEntityDispatchers();
		
		ForgeHooksClient.registerTESRItemStack(CloakItems.EXTERNAL_CARD, 0, ExternalCaptureCardRenderer.FakeTileEntity.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(CloakBlocks.CLOAK_BLOCK), 0, ItemBlockCloakBlockRenderer.FakeTileEntity.class);
		
		BRLRegistry.registerFactory(CloakBlocks.CLOAK_BLOCK, new CloakBlockBRLFactory());
		
	}
	
	@Override
	public void init(FMLInitializationEvent event) 
	{
		super.init(event);
		ExternalImageHandler.init();		

		registerItemColors();
		setupChunkRenderFactory();	
		
	}
	
	private void setupChunkRenderFactory()
	{
		try
		{
			for(Field field : RenderGlobal.class.getDeclaredFields())
				if(field.getType() == IRenderChunkFactory.class)
				{
					field.setAccessible(true);
					field.set(Minecraft.getMinecraft().renderGlobal, new CloakedRenderChunkFactory((IRenderChunkFactory) field.get(Minecraft.getMinecraft().renderGlobal)));
					field.setAccessible(false);
				}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private void registerTileEntityDispatchers()
	{
//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCloakBlock.class, new TileEntityCloakBlockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCloakingMachine.class, new TileEntityCloakingMachineRenderer());
		
		ClientRegistry.bindTileEntitySpecialRenderer(ExternalCaptureCardRenderer.FakeTileEntity.class, new ExternalCaptureCardRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(ItemBlockCloakBlockRenderer.FakeTileEntity.class, new ItemBlockCloakBlockRenderer());

	}
	
	private void registerHandlers()
	{
		Object[] handlers = 
			{
					new ModelBakeHandler(),
					new TextureStitchHandler(),
					new ParticleHandler(),
					new CaptureCardHandler()
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
				int blockColor = -1;
				try
				{
					blockColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(NBTUtil.readBlockState(stack.getOrCreateSubCompound("capture_info")), Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), tintIndex - 1);
				}
				catch (Throwable e) 
				{
					;
				}
				ItemStackHandler handler = new ItemStackHandler(1);
				handler.deserializeNBT(stack.getOrCreateSubCompound("capture_info").getCompoundTag("item"));
				return tintIndex == 0 ? -1 : handler.getStackInSlot(0).isEmpty() ? -1 : blockColor == -1 ? Minecraft.getMinecraft().getItemColors().colorMultiplier(handler.getStackInSlot(0), tintIndex - 1) : blockColor;
			}
		}, CloakItems.BLOCKSTATE_CARD);
		
		ic.registerItemColorHandler(new IItemColor() {
			
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				int liquidColor = -1;
				try
				{
					liquidColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(NBTUtil.readBlockState(stack.getOrCreateSubCompound("capture_info")), Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), tintIndex - 1);
				}
				catch (Throwable e) 
				{
					;
				}
				return tintIndex == 0 ? -1 : liquidColor;
			}
		}, CloakItems.LIQUDSTATE_CARD);
		
		ic.registerItemColorHandler(new IItemColor() {
			
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex == 0 ? -1 : -1;
			}
		}, CloakItems.EXTERNAL_CARD);
		
		ic.registerItemColorHandler(new IItemColor() {
			
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				NBTTagCompound nbt = stack.getOrCreateSubCompound("rendering_info");
		    	HashMap<Integer, ItemStack> currentModMap = TileEntityCloakingMachine.readFromNBTTag(nbt.getCompoundTag("mod_list"));
				HashMap<Integer, Pair<ItemStack, IBlockState>> overrideList = new HashMap<>();
				for(int i : currentModMap.keySet())
					if(currentModMap.get(i) != null && !currentModMap.get(i).isEmpty())
					{
						ItemStackHandler innerHandler = new ItemStackHandler();
						innerHandler.deserializeNBT(currentModMap.get(i).getOrCreateSubCompound("capture_info").getCompoundTag("item"));
						overrideList.put(i, Pair.of(innerHandler.getStackInSlot(0), NBTUtil.readBlockState(currentModMap.get(i).getOrCreateSubCompound("capture_info"))));
					}
								
				
				if(overrideList.containsKey(Math.floorDiv(tintIndex, 1000)))
				{
					int blockcolor = -1;
					try
					{
						blockcolor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(overrideList.get(Math.floorDiv(tintIndex, 1000)).getRight(), Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), tintIndex - 1);
					}
					catch (Throwable e) 
					{
						;
					}
					if(blockcolor != -1)
						return blockcolor;
					return Minecraft.getMinecraft().getItemColors().colorMultiplier(overrideList.get(Math.floorDiv(tintIndex, 1000)).getLeft(), tintIndex % 1000);
				}
				
				ItemStackHandler handler = new ItemStackHandler(3);
				handler.deserializeNBT(nbt.getCompoundTag("ItemHandler"));
				ItemStackHandler innerHandler = new ItemStackHandler();
				innerHandler.deserializeNBT(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getCompoundTag("item"));
				return Minecraft.getMinecraft().getItemColors().colorMultiplier(innerHandler.getStackInSlot(0), tintIndex % 1000);
			}
		}, CloakBlocks.CLOAK_BLOCK);
	}
}
