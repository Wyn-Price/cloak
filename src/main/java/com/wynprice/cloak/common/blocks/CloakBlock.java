package com.wynprice.cloak.common.blocks;

import java.util.List;
import java.util.Random;

import com.wynprice.cloak.client.handlers.ParticleHandler;
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
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) 
	{
		if(blockAccess.getTileEntity(pos) instanceof TileEntityCloakBlock && !(((TileEntityCloakBlock)blockAccess.getTileEntity(pos)).getRenderState().getBlock() == Blocks.AIR || ((TileEntityCloakBlock)blockAccess.getTileEntity(pos)).getModelState().getBlock() == Blocks.AIR))
			return false;
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getModelState(source, pos).getBoundingBox(source, pos);
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
                manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
                		.createParticle(0, worldObj, d0, d1, d2, 0, 0, 0, 
                				Block.getStateId(((TileEntityCloakBlock)worldObj.getTileEntity(target.getBlockPos())).getRenderState())))
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
			IBlockState state = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getActualState(world, pos);
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
	                    manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
	                    		.createParticle(0, world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2,
	                    				d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, Block.getStateId(state))).setBlockPos(pos));
	                }
	            }
	        }
		}
		return false;
	}

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
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
    	return tileEntity == null || tileEntity.getModelState().getBlock() == this ? Blocks.STONE.getDefaultState() : tileEntity.getRenderState().getActualState(new CloakBlockAccess(access), pos);
    }
}
