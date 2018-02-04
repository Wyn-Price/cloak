package com.wynprice.cloak.common.registries;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.common.blocks.BasicCloakingMachine;
import com.wynprice.cloak.common.blocks.CloakBlock;
import com.wynprice.cloak.common.blocks.TextureMatrix;
import com.wynprice.cloak.common.items.CloakBlockItemBlock;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CloakBlocks 
{
	
	public final static Block CLOAK_BLOCK = new CloakBlock();
	public final static Block CLOAKING_MACHINE = new BasicCloakingMachine();
	public final static Block TEXTURE_MATRIX = new TextureMatrix();
	
	public static void preInit()
	{
		regBlock(CLOAK_BLOCK);
		regBlock(CLOAKING_MACHINE, 64, BasicCloakingMachine.POWERED);
		regBlock(TEXTURE_MATRIX);
	}
	
	private final static ArrayList<Block> BLOCKS_WITH_ITEMS = new ArrayList<Block>();
	private final static HashMap<Block, Integer> BLOCK_STACK_SIZES = new HashMap<>();
	private final static ArrayList<Block> BLOCKS_WITH_CUSTOM_STATE_MAP = new ArrayList<Block>();
	private final static ArrayList<IProperty<?>[]> PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP = new ArrayList<IProperty<?>[]>();
	
	public static void regRenders() {
		for(int i = 0; i < BLOCKS_WITH_CUSTOM_STATE_MAP.size(); i++)
			createStateMappers(BLOCKS_WITH_CUSTOM_STATE_MAP.get(i), PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.get(i));
		for(Block b : BLOCKS_WITH_ITEMS)
			regRender(b);
	}

	private static void regBlock(Block block) 
	{
		regBlock(block, 64);
	}

	private static void regBlock(Block block, int stackSize) 
	{
		BLOCKS_WITH_ITEMS.add(block);
		BLOCK_STACK_SIZES.put(block, stackSize);	
		block.setCreativeTab(CloakMod.TAB);
		register(block);
	}
	
	private static void regBlockIgnoreAll(Block block)
	{
		regBlockIgnoreAll(block, 64);
	}
	
	private static void regBlockIgnoreAll(Block block, int stackSize)
	{
		regBlock(block, stackSize, block.getDefaultState().getProperties().asMultimap().asMap().keySet().toArray(new IProperty[block.getDefaultState().getProperties().asMultimap().asMap().keySet().size()]));
	}
	
	private static void regSingleBlockIgnoreAll(Block block)
	{
		regSingleBlock(block, block.getDefaultState().getProperties().asMultimap().asMap().keySet().toArray(new IProperty[block.getDefaultState().getProperties().asMultimap().asMap().keySet().size()]));
	}
	
	private static void regBlock(Block block, int stackSize, IProperty<?>... toIgnore)
	{
		BLOCKS_WITH_CUSTOM_STATE_MAP.add(block);
		PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.add(toIgnore);
		regBlock(block, stackSize);
	}
	
	@SideOnly(Side.CLIENT)
	private static void createStateMappers(Block block, IProperty<?>[] toIgnore)
	{
		ModelLoader.setCustomStateMapper(block, (new StateMap.Builder().ignore(toIgnore)).build());
	}
	
	private static void regSingleBlock(Block block)
	{
		register(block);
	}
	
	private static void regSingleBlock(Block block,  IProperty<?>... toIgnore)
	{
		BLOCKS_WITH_CUSTOM_STATE_MAP.add(block);
		PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.add(toIgnore);
		register(block);
	}

	private static void regRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	private static void register(Block block)
	{
		ForgeRegistries.BLOCKS.register(block);
		if(BLOCKS_WITH_ITEMS.contains(block))
		{
			ItemBlock item = new ItemBlock(block);
			if(block == CLOAK_BLOCK) item = new CloakBlockItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setMaxStackSize(BLOCK_STACK_SIZES.get(block));
			ForgeRegistries.ITEMS.register(item);
		}
	}
}
