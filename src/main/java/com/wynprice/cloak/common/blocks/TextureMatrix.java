package com.wynprice.cloak.common.blocks;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.rendering.gui.ExternalImageGui;
import com.wynprice.cloak.client.rendering.gui.UploadMatrixGui;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextureMatrix extends Block
{

	public TextureMatrix() 
	{
		super(Material.IRON);
		setUnlocalizedName("texture_matrix");
		setRegistryName("texture_matrix");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(worldIn.isRemote)
			openGui();
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	private void openGui()
	{
		ExternalImageHandler.init();
		Minecraft.getMinecraft().displayGuiScreen(new UploadMatrixGui());
	}

}
