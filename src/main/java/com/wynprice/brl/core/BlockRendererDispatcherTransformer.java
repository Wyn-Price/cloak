package com.wynprice.brl.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;
import com.wynprice.brl.BLBufferBuilder;
import com.wynprice.cloak.CloakMod;
import com.wynprice.cloak.client.rendering.models.CloakedModel;
import com.wynprice.cloak.client.rendering.models.SingleQuadModel;
import com.wynprice.cloak.common.registries.CloakBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcherTransformer implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.BlockRendererDispatcher"))
			return basicClass;
		
		String methodName = BetterRenderCore.isDebofEnabled ? "func_175018_a" : "renderBlock";
		String methodDesc = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, methodDesc, null, new String[0]);
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    method.instructions = new InsnList();
	    method.instructions.add(startLabel);
        method.instructions.add(new LineNumberNode(52, startLabel));
	    	    
	    LocalVariableNode state = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_1_" : "state", "Lnet/minecraft/block/state/IBlockState;", null, startLabel, endLabel, 0);
	    LocalVariableNode pos = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_2_" : "pos", "Lnet/minecraft/util/math/BlockPos;", null, startLabel, endLabel, 1);
	    LocalVariableNode blockAccess = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_3_" : "blockAccess", "Lnet/minecraft/world/IBlockAccess;", null, startLabel, endLabel, 2);
	    LocalVariableNode bufferBuilderIn = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_4_" : "bufferBuilderIn", "Lnet/minecraft/client/renderer/BufferBuilder;", null, startLabel, endLabel, 3);

	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));

	    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/core/BlockRendererDispatcherTransformer", "renderBlock", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z", false));
	    method.instructions.add(new InsnNode(Opcodes.IRETURN));
	    method.instructions.add(endLabel);
	    
	    ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)
        	if(!m.desc.equalsIgnoreCase(methodDesc) || !m.name.equalsIgnoreCase(methodName))
        		nodes.add(m);
        
        nodes.add(method);
        
        node.methods.clear();
        node.methods.addAll(nodes);
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
        
		return basicClass;
	}
	
	private static BlockFluidRenderer fluid_render;//TODO add resource manager reloading
	
	public static boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn)
    {
		if(fluid_render == null)
			fluid_render = new BlockFluidRenderer(Minecraft.getMinecraft().getBlockColors());
        try
        {
            EnumBlockRenderType enumblockrendertype = state.getRenderType();

            if (enumblockrendertype == EnumBlockRenderType.INVISIBLE)
            {
                return false;
            }
            else
            {
                if (blockAccess.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
                {
                    try
                    {
                        state = state.getActualState(blockAccess, pos);
                    }
                    catch (Exception var8)
                    {
                        ;
                    }
                }
                switch (enumblockrendertype)
                {
                    case MODEL:
                        ArrayList<IBakedModel> models = Lists.newArrayList(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state));
                        state = state.getBlock().getExtendedState(state, blockAccess, pos);
                        if(state.getBlock() == CloakBlocks.CLOAK_BLOCK)
                        {
                        	CloakedModel model = new CloakedModel(Blocks.ACACIA_FENCE.getDefaultState(), Blocks.BEACON.getDefaultState());
                        	List<EnumFacing> facingList = new ArrayList<>();
                        	ArrayList<IBakedModel> modelList = new ArrayList<>();
                        	facingList.add(null);
                            for (EnumFacing enumfacing : EnumFacing.values()) facingList.add(enumfacing);
                        	for(EnumFacing face : facingList)
                        		for(BakedQuad quad : model.getQuads(Blocks.BEACON.getDefaultState(), face, 0L))
                        			modelList.add(new SingleQuadModel(model, quad, face));
                        	models = modelList;
                        }
                        boolean retBool = true;
                        for(IBakedModel model : models)
                        	if(!Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(blockAccess, model, state, pos, bufferBuilderIn, true))
                        		retBool = false;
                        return retBool;
                    case ENTITYBLOCK_ANIMATED:
                        return false;
                    case LIQUID:
                        return fluid_render.renderFluid(blockAccess, state, pos, bufferBuilderIn);
                    default:
                        return false;
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
    }

}
