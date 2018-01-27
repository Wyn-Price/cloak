package com.wynprice.cloak.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityCloakBlock extends TileEntity
{
	private IBlockState modelState = Blocks.AIR.getDefaultState();
	private IBlockState renderState = Blocks.AIR.getDefaultState();

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound nbt = compound.getCompoundTag("block_info");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(nbt.getString("model_block")));
		modelState = block.getStateFromMeta(nbt.getInteger("model_meta"));
		
		block = Block.REGISTRY.getObject(new ResourceLocation(nbt.getString("render_block")));
		renderState = block.getStateFromMeta(nbt.getInteger("render_meta"));
		super.readFromNBT(compound);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("model_block", modelState.getBlock().getRegistryName().toString());
		compound.setInteger("model_meta", modelState.getBlock().getMetaFromState(modelState));
		
		compound.setString("render_block", renderState.getBlock().getRegistryName().toString());
		compound.setInteger("render_meta", renderState.getBlock().getMetaFromState(renderState));
		return super.writeToNBT(nbt);
	}
	
	public void setRenderState(IBlockState renderState) {
		this.renderState = renderState;
	}
	
	public void setModelState(IBlockState modelState) {
		this.modelState = modelState;
	}
	
	public IBlockState getModelState() {
		return modelState;
	}
	
	public IBlockState getRenderState() {
		return renderState;
	}
}
