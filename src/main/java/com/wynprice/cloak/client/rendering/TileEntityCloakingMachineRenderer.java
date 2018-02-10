package com.wynprice.cloak.client.rendering;

import com.wynprice.brl.tcn.TJR;
import com.wynprice.brl.tcn.TJRModel;
import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.CloakingMachineModel;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCloakingMachineRenderer extends BaseCloakingModelRenderer<TileEntityCloakingMachine>
{	
	public TileEntityCloakingMachineRenderer() 
	{
		super(new CloakedRenderingFactory() {
		
			@Override
			public CloakedModel createModel(World world, BlockPos pos, IBlockState modelState, IBlockState renderState) 
			{
				return new CloakingMachineModel(modelState, renderState);
			}
		});
	}	
	
}
