package com.wynprice.cloak.common.blocks;

import java.util.List;
import java.util.Random;

import com.wynprice.cloak.client.handlers.ExternalImageHandler;
import com.wynprice.cloak.client.handlers.ParticleHandler;
import com.wynprice.cloak.client.rendering.particle.ParticleExternalDigging;
import com.wynprice.cloak.common.items.CloakBlockItemBlock;
import com.wynprice.cloak.common.registries.CloakBlocks;
import com.wynprice.cloak.common.tileentity.TileEntityCloakBlock;
import com.wynprice.cloak.common.world.CloakBlockAccess;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class CloakBlock extends Block implements ITileEntityProvider
{
	public CloakBlock() {
		super(Material.ROCK);
		setRegistryName("cloak_block");
		setUnlocalizedName("cloak_block");
		setHardness(1f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityCloakBlock();
	}
	
	@Override
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) 
	{
		;
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) 
	{
		if(player instanceof EntityPlayerMP && !((EntityPlayerMP)player).interactionManager.isCreative())
			dropBlock(world, pos);
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) 
	{
		dropBlock(worldIn, pos);
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}
	
	private void dropBlock(World worldIn, BlockPos pos)
	{
		ItemStack baseStack = new ItemStack(this);
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityCloakBlock) //Change around NBT tag so it'll stack with others of its kind
		{
			NBTTagCompound data = ((TileEntityCloakBlock)te).writeRenderData(new NBTTagCompound());
			
			ItemStackHandler handler = new ItemStackHandler();
			ItemStackHandler handler2 = new ItemStackHandler(5);
			ItemStackHandler handler3 = new ItemStackHandler();
			
			handler.deserializeNBT(data.getCompoundTag("ItemHandler"));
			handler2.setStackInSlot(0, handler.getStackInSlot(0));
			handler2.setStackInSlot(1, handler.getStackInSlot(1));
			handler2.setStackInSlot(2, handler.getStackInSlot(2));
			data.setTag("ItemHandler", handler2.serializeNBT());
			
			handler3.deserializeNBT(handler.getStackInSlot(1).getTagCompound().getCompoundTag("capture_info").getCompoundTag("item"));
			handler2.getStackInSlot(1).getTagCompound().setTag("capture_info", handler2.getStackInSlot(1).getTagCompound().getTag("itemblock_info"));
			handler2.getStackInSlot(1).getTagCompound().removeTag("itemblock_info");
			handler2.getStackInSlot(1).getTagCompound().getCompoundTag("capture_info").setTag("item", handler3.serializeNBT());
			baseStack.setTagCompound(new NBTTagCompound());
			baseStack.getTagCompound().setTag("rendering_info", data.copy());
			
		}
		
		spawnAsEntity(worldIn, pos, baseStack);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) 
	{
		if(blockAccess.getTileEntity(pos) instanceof TileEntityCloakBlock && !(((TileEntityCloakBlock)blockAccess.getTileEntity(pos)).getModelState().getBlock() == Blocks.AIR))
			return false;
		return getModelState(blockAccess, pos).shouldSideBeRendered(new CloakBlockAccess(blockAccess), pos, side);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getModelState(source, pos).getBoundingBox(new CloakBlockAccess(source), pos);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return getModelState(worldIn, pos).getBlockFaceShape(worldIn, pos, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getModelState(world, pos).isSideSolid(world, pos, side);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) 
	{
		getModelState(worldIn, pos).addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, true);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return getRenderState(world, pos).getBlock().getSoundType(getRenderState(world, pos), world, pos, entity);
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isFullBlock(IBlockState state) 
	{
		return false;
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
		
    @SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) 
	{
    		if (target.getBlockPos() != null && worldObj.getTileEntity(target.getBlockPos()) instanceof TileEntityCloakBlock && 
    				((TileEntityCloakBlock)worldObj.getTileEntity(target.getBlockPos())).getRenderState() != null)
            {
                int i = target.getBlockPos().getX();
                int j = target.getBlockPos().getY();
                int k = target.getBlockPos().getZ();
                float f = 0.1F;
                AxisAlignedBB axisalignedbb = ((TileEntityCloakBlock)worldObj.getTileEntity(target.getBlockPos())).getRenderState().getBoundingBox(worldObj, target.getBlockPos());
                double d0 = (double)i + new Random().nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
                double d1 = (double)j + new Random().nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
                double d2 = (double)k + new Random().nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
                if (target.sideHit == EnumFacing.DOWN) d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
                if (target.sideHit == EnumFacing.UP) d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
                if (target.sideHit == EnumFacing.NORTH) d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
                if (target.sideHit == EnumFacing.SOUTH) d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
                if (target.sideHit == EnumFacing.WEST) d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
                if (target.sideHit == EnumFacing.EAST) d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
                IBlockState renderState = ((TileEntityCloakBlock)worldObj.getTileEntity(target.getBlockPos())).getRenderState();
                ItemStackHandler handler = new ItemStackHandler();
                handler.deserializeNBT(((TileEntityCloakBlock)worldObj.getTileEntity(target.getBlockPos())).writeRenderData(new NBTTagCompound()).getCompoundTag("ItemHandler"));
                if(ExternalImageHandler.SYNCED_RESOURCE_MAP.containsKey(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")))
                  manager.addEffect(new ParticleExternalDigging(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info").getString("external_image")), worldObj, d0, d1, d2, 0, 0, 0)
                		  .setBlockPos(target.getBlockPos()).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
                else
	                manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
	                		.createParticle(0, worldObj, d0, d1, d2, 0, 0, 0, Block.getStateId(renderState.getBlock() == Blocks.AIR ? CloakBlocks.CLOAKING_MACHINE.getDefaultState() : renderState)))
	                		.setBlockPos(target.getBlockPos()).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
               
                return true;
            }
    		return false;
	}
		
	@SideOnly(Side.CLIENT)
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) 
	{
		if(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos) != null)
		{
			IBlockState state = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getLeft().getActualState(world, pos);
	        int i = 4;

	        for (int j = 0; j < 4; ++j)
	        {
	            for (int k = 0; k < 4; ++k)
	            {
	                for (int l = 0; l < 4; ++l)
	                {
	                    double d0 = ((double)j + 0.5D) / 4.0D;
	                    double d1 = ((double)k + 0.5D) / 4.0D;
	                    double d2 = ((double)l + 0.5D) / 4.0D;
	                    if(ExternalImageHandler.SYNCED_RESOURCE_MAP.containsKey(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getRight()))
	                    	manager.addEffect(new ParticleExternalDigging(ExternalImageHandler.SYNCED_RESOURCE_MAP.get(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getRight()),  world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D).setBlockPos(pos));
	                    else
		                    manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
		                    		.createParticle(0, world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2,
		                    				d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, Block.getStateId(state.getBlock() == Blocks.AIR ? CloakBlocks.CLOAKING_MACHINE.getDefaultState() : state)))
		                    		.setBlockPos(pos));
	                }
	            }
	        }
		}
		return false;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) //Changing this would cause more issues that it would fix
	{
		return 0; 
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) 
	{
		return getRenderStateWithList(world, pos).getLightValue(world, pos);
	}

    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state)
    {
        return 1.0F;
    }
    
    private IBlockState getModelState(IBlockAccess access, BlockPos pos)
    {
    	TileEntityCloakBlock tileEntity = (TileEntityCloakBlock)access.getTileEntity(pos);
    	return tileEntity == null || tileEntity.getModelState().getBlock() == this ? Blocks.STONE.getDefaultState() : tileEntity.getModelState().getActualState(new CloakBlockAccess(access), pos);
    }
    
    private IBlockState getRenderState(IBlockAccess access, BlockPos pos)
    {
    	TileEntityCloakBlock tileEntity = (TileEntityCloakBlock)access.getTileEntity(pos);
    	return tileEntity == null || tileEntity.getModelState().getBlock() == this || tileEntity.getRenderState().getBlock() == Blocks.AIR ? getModelState(access, pos) : tileEntity.getRenderState().getActualState(new CloakBlockAccess(access), pos);
    }
    
    private IBlockState getRenderStateWithList(IBlockAccess access, BlockPos pos)
    {
    	IBlockState overState = Blocks.STONE.getDefaultState();
    	if(CloakBlockItemBlock.PRESETTING_LIST.containsKey(pos))
    	{
    		ItemStackHandler handler = new ItemStackHandler();
        	handler.deserializeNBT(CloakBlockItemBlock.PRESETTING_LIST.get(pos).getCompoundTag("ItemHandler"));
        	if(handler.getSlots() > 0)
        		overState = NBTUtil.readBlockState(handler.getStackInSlot(0).getOrCreateSubCompound("capture_info"));

    	}
    	TileEntityCloakBlock tileEntity = (TileEntityCloakBlock)access.getTileEntity(pos);
    	IBlockState ret =  tileEntity == null || tileEntity.getModelState().getBlock() == this || overState.getBlock() != Blocks.STONE? overState : tileEntity.getRenderState().getActualState(new CloakBlockAccess(access), pos);

    	return ret;
    }
}
