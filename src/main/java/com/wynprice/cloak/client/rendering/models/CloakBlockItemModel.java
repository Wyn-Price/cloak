package com.wynprice.cloak.client.rendering.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.wynprice.cloak.common.tileentity.TileEntityCloakingMachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class CloakBlockItemModel extends BaseModelProxy
{

	public CloakBlockItemModel(IBakedModel oldModel) {
		super(oldModel);
	}

	public static final Method SLOTMETHOD = getSlotMethod();
	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		boolean isUnderMouse = false;
		if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer)
		{
			if(SLOTMETHOD != null)
			{
				SLOTMETHOD.setAccessible(true);
				int i = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
		        int j = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;
				try 
				{
					Slot slot = ((Slot) SLOTMETHOD.invoke(Minecraft.getMinecraft().currentScreen, i, j));
					isUnderMouse = slot != null && slot.getStack() == stack;
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			if(!isUnderMouse)
				isUnderMouse = Minecraft.getMinecraft().player.inventory.getItemStack() == stack;
		}
		if(stack.hasTagCompound() && !isUnderMouse)
		{
			NBTTagCompound nbt = stack.getOrCreateSubCompound("rendering_info");
			ItemStackHandler handler = new ItemStackHandler(nbt.getCompoundTag("ItemHandler").getInteger("Size"));
			handler.deserializeNBT(nbt.getCompoundTag("ItemHandler"));
			if(handler.getSlots() == nbt.getCompoundTag("ItemHandler").getInteger("Size") && !handler.getStackInSlot(0).isEmpty() && !handler.getStackInSlot(1).isEmpty())
			{
				IBlockState renderState = NBTUtil.readBlockState(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info"));
		    	IBlockState modelState = NBTUtil.readBlockState(handler.getStackInSlot(1).getOrCreateSubCompound("capture_info"));
		    	
		    	CloakedModel model = new CloakedModel(modelState, renderState);
		    	
		    	HashMap<Integer, ItemStack> currentModMap = TileEntityCloakingMachine.readFromNBTTag(nbt.getCompoundTag("mod_list"));
		    	
		    	HashMap<Integer, IBlockState> overrideList = new HashMap<>();
				for(int i : currentModMap.keySet())
					if(currentModMap.get(i) != null && !currentModMap.get(i).isEmpty())
						overrideList.put(i, NBTUtil.readBlockState(currentModMap.get(i).getSubCompound("capture_info")));
	
				
				model.getOverrideList().putAll(overrideList);
				
				ArrayList<BakedQuad> quadList = new ArrayList<>();
				
				for(BakedQuad quad : model.getQuads(modelState, side, rand))
				{
					BakedQuad newQuad = new BakedQuad(quad.getVertexData(), quad.getTintIndex() + (quad.getTintIndex() > -1 ? model.getParentID(quad) * 1000 : 0), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
					quadList.add(newQuad);
				}
				
				return quadList;
			}
		}

		return super.getQuads(state, side, rand);
	}
	
	private ItemStack stack = ItemStack.EMPTY;

	@Override
	public ItemOverrideList getOverrides() 
	{
		return new ItemOverrideList(oldModel.getOverrides().getOverrides())
				{
					@Override
					public ResourceLocation applyOverride(ItemStack stack, World worldIn, EntityLivingBase entityIn) 
					{
						CloakBlockItemModel.this.stack = stack;
						return super.applyOverride(stack, worldIn, entityIn);
					}
				};
	}
	
	private static Method getSlotMethod()
	{
		Method slotMethod = null;
		for(Method method : GuiContainer.class.getDeclaredMethods())
			if(method.getReturnType() == Slot.class && method.getParameterCount() == 2 && method.getParameterTypes()[0] == int.class && method.getParameterTypes()[1] == int.class)
				return method;
		return null;
	}
}
