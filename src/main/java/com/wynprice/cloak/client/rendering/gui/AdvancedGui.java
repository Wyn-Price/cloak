package com.wynprice.cloak.client.rendering.gui;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.common.containers.ContainerBasicCloakingMachine;
import com.wynprice.cloak.common.network.CloakNetwork;
import com.wynprice.cloak.common.network.packets.PacketFaceSelectionAdvancedGUI;
import com.wynprice.cloak.common.network.packets.PacketRemoveModificationList;
import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class AdvancedGui extends BasicGui
{

	private EntityPlayer player;
	
	public AdvancedGui(EntityPlayer player, TileEntityCloakingMachine tileEntity) {
		super(player, tileEntity);
	}
	
	private boolean previous36;
	private boolean previous37;
	private boolean hasOpened;
	
	@Override
	public void updateScreen() 
	{
		if(previous36 != this.inventorySlots.getSlot(36).getHasStack() || previous37 != this.inventorySlots.getSlot(37).getHasStack())
		{
			previous36 = this.inventorySlots.getSlot(36).getHasStack();
			previous37 = this.inventorySlots.getSlot(37).getHasStack();
			
			if(hasOpened)
			{
				PacketRemoveModificationList.updateContainer((ContainerBasicCloakingMachine) this.inventorySlots, Minecraft.getMinecraft().player);
				CloakNetwork.sendToServer(new PacketRemoveModificationList());
			}
		}
		hasOpened = true;
	}
	
	@Override
	protected CloakedModel createModel(IBlockState modelState, IBlockState basicRenderState) 
	{
		Pair<HashMap<Integer, IBlockState>, HashMap<Integer, ResourceLocation>> pair = ((ContainerBasicCloakingMachine)this.inventorySlots).getBlockStateMap();
		CloakedModel model = new CloakedModel(modelState, basicRenderState, pair.getLeft());
		model.setExternalOverrideList(pair.getRight());
		return model;
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
