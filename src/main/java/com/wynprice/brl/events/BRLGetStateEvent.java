package com.wynprice.brl.events;

import com.wynprice.brl.BrlMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;

/**
 * BRLGetStateEvent is fired just before a block is rendered, allowing you to change the ChunkCache, but not the rendered block
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 * @author Wyn Price
 *
 */
public class BRLGetStateEvent extends BRLBaseEvent<BRLGetStateEvent> 
{
	private IBlockState state;
	private IBlockAccess access;
	private final BlockPos pos;
	
	/**
	 * Used to determine if more than one mod has set to a custom state
	 */
	private boolean hasSet;
	
	public BRLGetStateEvent(IBlockAccess access, IBlockState state, BlockPos pos) 
	{
		this.state = state;
		this.access = access;
		this.pos = pos;
	}
	
	/**
	 * Set the state of the block to be rendered
	 */
	public void setState(IBlockState state) 
	{
		if(hasSet)
			BrlMod.LOGGER.warn("BRLGetStateEvent recived two diffrent custom states: {} and {}. Using {} ", this.state, state, state);
		hasSet = true;
		this.state = state;
	} 
	
	/**
	 * Sets the {@link IBlockAccess} of the current render.
	 * <br>
	 * <b>Please proxy the whatever you set here. Keep in mind that other mods may rely on this.</b>
	 * <br>
	 */
	public void setAccess(IBlockAccess access) 
	{
		this.access = access;
	}
	
	/**
	 * Gets the world thats used to render the block.
	 * <br>
	 * It is reccomended that you use {@link Minecraft#world} if you want to override this, as the IBlockAccess may not return the block thats actually in the world
	 * @return
	 */
	public IBlockAccess getAccess() 
	{
		return access;
	}
		
	/**
	 * Get the state of the block thats going to be rendered
	 */
	public IBlockState getState() 
	{
		return state;
	}
	
	
	/**
	 * Get the position where the block will be rendered
	 */
	public BlockPos getPos() 
	{
		return pos;
	}
}
