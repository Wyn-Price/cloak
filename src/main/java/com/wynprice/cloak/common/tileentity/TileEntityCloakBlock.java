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
	private IBlockState state = Blocks.AIR.getDefaultState();
	
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
		Block block = Block.REGISTRY.getObject(new ResourceLocation(nbt.getString("block")));
		state = block.getStateFromMeta(nbt.getInteger("meta"));
		super.readFromNBT(compound);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("block", state.getBlock().getRegistryName().toString());
		compound.setInteger("meta", state.getBlock().getMetaFromState(state));
		return super.writeToNBT(nbt);
	}
	
	public IBlockState getState() {
		return state;
	}
	
	public void setState(IBlockState state) {
		this.state = state;
	}
}
