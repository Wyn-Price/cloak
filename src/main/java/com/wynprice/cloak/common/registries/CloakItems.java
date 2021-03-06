package com.wynprice.cloak.common.registries;

import java.util.ArrayList;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.items.ExternalCaptureCard;
import com.wynprice.cloak.common.items.ItemCaptureBlock;
import com.wynprice.cloak.common.items.ItemCaptureLiquid;
import com.wynprice.cloak.common.items.TextureMatrix;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CloakItems 
{
	
	public static final Item BLOCKSTATE_CARD = new ItemCaptureBlock();
	public static final Item LIQUDSTATE_CARD = new ItemCaptureLiquid();
	public static final Item EXTERNAL_CARD = new ExternalCaptureCard();
	
	public static final Item TEXTURE_MATRIX = new TextureMatrix();
	
	public static void preInit()
	{
		regItem(BLOCKSTATE_CARD);
		regItem(LIQUDSTATE_CARD);
		regItem(EXTERNAL_CARD);
		
		regItem(TEXTURE_MATRIX, 1);
	}
	
	public static void regRenders()
	{
		for(Item item : ALL_ITEMS)
			regRender(item);
	}
	
	public final static ArrayList<Item> ALL_ITEMS= new ArrayList<Item>();

	
	private static String[] emptyList(int size)
	{
		String[] s = new String[size];
		for(int i = 0; i < size; i++)
			s[i] = "";
		return s;
	}
	
	private static Item getItem(Item item)
	{
		return item;
	}
	
	private static void regItem(Item item)
	{
		regItem(item, 64);
	}
	
	private static void regItem(Item item, int stackSize)
	{
		ALL_ITEMS.add(item);
		item.setMaxStackSize(stackSize);
		ForgeRegistries.ITEMS.register(item);
	}
	
	private static void regRender(Item item)
	{
		if(item.getHasSubtypes())
		{
			int metaAmount = 1;
			ResourceLocation[] fileNames = {item.getRegistryName()};
			for(int i = 0; i < metaAmount; i++)
				regRender(item, i, fileNames[i]);
		}
		else
			regRender(item, 0, item.getRegistryName());
	}
	
	private static void regRender(Item item, int meta, ResourceLocation fileName)
	{
		item.setCreativeTab(CloakMod.TAB);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(fileName, "inventory"));
	}
}
