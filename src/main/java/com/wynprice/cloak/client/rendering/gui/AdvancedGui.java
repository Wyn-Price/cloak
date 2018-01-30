package com.wynprice.cloak.client.rendering.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.cloak.client.rendering.CloakedModel;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketFaceSelectionAdvancedGUI;
import com.wynprice.cloak.common.network.packets.PacketOpenBasicCloakingMachine;
import com.wynprice.cloak.common.network.packets.PacketSendRenderInfoAdvancedGUI;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class AdvancedGui extends BasicGui
{

	private EntityPlayer player;
	
	public AdvancedGui(EntityPlayer player, TileEntityCloakingMachine tileEntity) {
		super(player, tileEntity);
	}
	
	private int previousSize = -1;
	
	@Override
	public void updateScreen() 
	{
		boolean update = false;
		if(this.inventorySlots.getSlot(37).getHasStack())
		{
			NBTTagCompound modelStackNBT = this.inventorySlots.getSlot(37).getStack().getSubCompound("capture_info");
			IBlockState modelState = Block.REGISTRY.getObject(new ResourceLocation(modelStackNBT.getString("block"))).getStateFromMeta(modelStackNBT.getInteger("meta"));
			ArrayList<EnumFacing> facingList = Lists.newArrayList((EnumFacing)null);
			if(modelState.getBlock() != Blocks.AIR)
			{
				facingList.addAll(Lists.newArrayList(EnumFacing.VALUES));
				int size = 0;
				for(EnumFacing facing : facingList)
					for(BakedQuad quad : Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(modelState).getQuads(modelState, facing, 0L))
						size++;
				
				if(size != previousSize)
				{
					if(previousSize != -1)
						update = true;
					previousSize = size;
				}
			}
		}
		else if(previousSize != 0)
		{
			previousSize = 0;
			update = true;
		}
		
		if(update)
		{
			PacketSendRenderInfoAdvancedGUI.updateContainer((ContainerBasicCloakingMachine) this.inventorySlots, Minecraft.getMinecraft().player);
			CloakNetwork.sendToServer(new PacketSendRenderInfoAdvancedGUI());
		}
	}
	
	@Override
	protected CloakedModel createModel(IBlockState modelState, IBlockState basicRenderState) 
	{
		return new CloakedModel(modelState, basicRenderState, ((ContainerBasicCloakingMachine)this.inventorySlots).getBlockStateMap());
	}
	
	@Override
	protected HashMap<Integer, ItemStack> getInventoryColors(CloakedModel model, ItemStack defualt) 
	{
		HashMap<Integer, ItemStack> map = super.getInventoryColors(model, defualt);
		ContainerBasicCloakingMachine container = ((ContainerBasicCloakingMachine)this.inventorySlots);
		for(int i : container.modification_list.keySet())
			if(container.modification_list.get(i) != null && !container.modification_list.get(i).isEmpty())
			{
				ItemStackHandler handler = new ItemStackHandler(1);
				handler.deserializeNBT(container.modification_list.get(i).getSubCompound("capture_info").getCompoundTag("item"));
				map.put(i, handler.getStackInSlot(0));
			}
		return map;
	}
	
	@Override
	protected void onFaceSelected(int slotID, int OldSlotID) 
	{
		PacketFaceSelectionAdvancedGUI.setContainerFace((ContainerBasicCloakingMachine) this.inventorySlots, slotID, OldSlotID);
		CloakNetwork.sendToServer(new PacketFaceSelectionAdvancedGUI(slotID, OldSlotID));
	}

}
