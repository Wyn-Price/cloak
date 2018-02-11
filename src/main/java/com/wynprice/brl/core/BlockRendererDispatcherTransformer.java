package com.wynprice.brl.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;
import com.wynprice.brl.BRBufferBuilder;
import com.wynprice.brl.addons.plastic.BufferedPlastic;
import com.wynprice.brl.api.BRLRegistry;
import com.wynprice.brl.api.BRLRenderInfo;
import com.wynprice.brl.api.IBRLRenderFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
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
        method.localVariables.addAll(Lists.newArrayList(new LocalVariableNode[]{state, pos, blockAccess, bufferBuilderIn})); //Not required, but reccomended

	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", BetterRenderCore.isDebofEnabled ? "field_175025_e" : "fluidRenderer" , "Lnet/minecraft/client/renderer/BlockFluidRenderer;"));
	    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/core/BlockRendererDispatcherTransformer", "renderBlock", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/BlockFluidRenderer;)Z", false));
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
		
	public static boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, BlockFluidRenderer fluidRender)
	{
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
                    	IBRLRenderFactory factory =  BRLRegistry.getFactory(state.getBlock());
                        List<BRLRenderInfo> renderInfos = factory.getModels(blockAccess, pos, state);
                        boolean retBool = true;
                        for(BRLRenderInfo info : renderInfos)
                        {
                        	if(bufferBuilderIn instanceof BRBufferBuilder)
                        		((BRBufferBuilder)bufferBuilderIn).split(info.getLocation());
                        	                        	
                        	if(!Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(blockAccess, info.getModel(), info.getState(), pos, bufferBuilderIn, true))
                        		retBool = false;
                        }
                        return retBool;
                    case ENTITYBLOCK_ANIMATED:
                        return false;
                    case LIQUID:
                        return fluidRender.renderFluid(blockAccess, state, pos, bufferBuilderIn);
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
