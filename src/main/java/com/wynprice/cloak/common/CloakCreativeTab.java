package com.wynprice.cloak.common;

import com.wynprice.cloak.CloakMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CloakCreativeTab extends CreativeTabs
{

	public CloakCreativeTab() {
		super(CloakMod.MODID);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Blocks.BEDROCK);
	}

}
