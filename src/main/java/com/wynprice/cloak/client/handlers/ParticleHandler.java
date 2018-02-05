package com.wynprice.cloak.client.handlers;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.cloak.client.rendering.particle.ParticleExternalDigging;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.items.ItemStackHandler;

public class ParticleHandler 
{
	public static final HashMap<BlockPos, Pair<IBlockState, String>> BLOCKBRAKERENDERMAP = new HashMap<>();

	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		int i = MathHelper.floor(event.getEntity().posX);
        int j = MathHelper.floor(event.getEntity().posY - 0.20000000298023224D);
        int k = MathHelper.floor(event.getEntity().posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        if (event.getEntity().isSprinting() && !event.getEntity().isInWater() &&
        		event.getEntity().world.getTileEntity(blockpos) instanceof TileEntityCloakBlock && 
        		((TileEntityCloakBlock)event.getEntity().world.getTileEntity(blockpos)).getRenderState() != null)
        {
        	ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(((TileEntityCloakBlock)event.getEntity().world.getTileEntity(blockpos)).writeRenderData(new NBTTagCompound()).getCompoundTag("ItemHandler"));
            if(ExternalImageHandler.SYNCED_RESOURCE_MAP.containsKey(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")))
            	Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleExternalDigging(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")),
            		event.getEntity().world, event.getEntity().posX + 
        			((double)event.getEntityLiving().getRNG().nextFloat() - 0.5D) * 
        			(double)event.getEntity().width, event.getEntity().getEntityBoundingBox().minY + 0.1D,
        			event.getEntity().posZ + ((double)event.getEntityLiving().getRNG().nextFloat() - 0.5D) *
        			(double)event.getEntity().width, -event.getEntity().motionX * 4.0D, 1.5D, -event.getEntity().motionZ * 4.0D).setBlockPos(blockpos));
            else
            	;
        	event.getEntity().world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, event.getEntity().posX + 
        			((double)event.getEntityLiving().getRNG().nextFloat() - 0.5D) * 
        			(double)event.getEntity().width, event.getEntity().getEntityBoundingBox().minY + 0.1D,
        			event.getEntity().posZ + ((double)event.getEntityLiving().getRNG().nextFloat() - 0.5D) *
        			(double)event.getEntity().width, -event.getEntity().motionX * 4.0D, 1.5D, -event.getEntity().motionZ * 4.0D,
        			Block.getStateId(((TileEntityCloakBlock)event.getEntity().world.getTileEntity(blockpos)).getRenderState()));
        }
	}
	
	@SubscribeEvent
	public void onWorldTick(ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().world != null)
			for(TileEntity te : Minecraft.getMinecraft().world.loadedTileEntityList)
				if(te instanceof TileEntityCloakBlock)
				{
					ItemStackHandler handler = new ItemStackHandler();
	                handler.deserializeNBT(((TileEntityCloakBlock)te).writeRenderData(new NBTTagCompound()).getCompoundTag("ItemHandler"));
	                BLOCKBRAKERENDERMAP.put(te.getPos(), Pair.of(((TileEntityCloakBlock) te).getRenderState(), handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")));
				}
	}
}