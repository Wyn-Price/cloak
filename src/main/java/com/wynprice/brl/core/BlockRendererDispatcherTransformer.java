package com.wynprice.brl.core;

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
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;
import com.wynprice.brl.BRBufferBuilder;
import com.wynprice.brl.api.BRLRegistry;
import com.wynprice.brl.api.BRLRenderInfo;
import com.wynprice.brl.api.IBRLRenderFactory;
import com.wynprice.brl.events.BRLGetStateEvent;

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
import scala.tools.cmd.gen.AnyValReps.AnyValNum.Op;

public class BlockRendererDispatcherTransformer implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.BlockRendererDispatcher"))
			return basicClass;
		
		String renderBlockMethodName = BetterRenderCore.isDebofEnabled ? "func_175018_a" : "renderBlock";
		String renderBlockMethodDesc = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z";
		
		String initMethodName = "<init>";
		String initMethodDesc = "(Lnet/minecraft/client/renderer/BlockModelShapes;Lnet/minecraft/client/renderer/color/BlockColors;)V";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode renderBlockMethod = new MethodNode(Opcodes.ACC_PUBLIC, renderBlockMethodName, renderBlockMethodDesc, null, new String[0]);
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    renderBlockMethod.instructions = new InsnList();
	    renderBlockMethod.instructions.add(startLabel);
        renderBlockMethod.instructions.add(new LineNumberNode(52, startLabel));
	    LocalVariableNode state = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_1_" : "state", "Lnet/minecraft/block/state/IBlockState;", null, startLabel, endLabel, 0);
	    LocalVariableNode pos = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_2_" : "pos", "Lnet/minecraft/util/math/BlockPos;", null, startLabel, endLabel, 1);
	    LocalVariableNode blockAccess = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_3_" : "blockAccess", "Lnet/minecraft/world/IBlockAccess;", null, startLabel, endLabel, 2);
	    LocalVariableNode bufferBuilderIn = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_175018_4_" : "bufferBuilderIn", "Lnet/minecraft/client/renderer/BufferBuilder;", null, startLabel, endLabel, 3);
        renderBlockMethod.localVariables.addAll(Lists.newArrayList(new LocalVariableNode[]{state, pos, blockAccess, bufferBuilderIn})); //Not required, but reccomended
	    renderBlockMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
	    renderBlockMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    renderBlockMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
	    renderBlockMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
	    renderBlockMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        renderBlockMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", BetterRenderCore.isDebofEnabled ? "field_175025_e" : "fluidRenderer" , "Lnet/minecraft/client/renderer/BlockFluidRenderer;"));
	    renderBlockMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/core/BlockRendererDispatcherTransformer", "renderBlock", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/BlockFluidRenderer;)Z", false));
	    renderBlockMethod.instructions.add(new InsnNode(Opcodes.IRETURN));
	    renderBlockMethod.instructions.add(endLabel);
	    
	    
	    MethodNode initMethod = new MethodNode(Opcodes.ACC_PUBLIC, initMethodName, initMethodDesc, null, new String[0]);
	    startLabel = new LabelNode();
	    endLabel = new LabelNode();
	    initMethod.instructions = new InsnList();
	    initMethod.instructions.add(startLabel);
        initMethod.instructions.add(new LineNumberNode(30, startLabel));
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    initMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
	    
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    initMethod.instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/client/renderer/ChestRenderer"));
	    initMethod.instructions.add(new InsnNode(Opcodes.DUP));
	    initMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/renderer/ChestRenderer", "<init>", "()V", false));
	    initMethod.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", "chestRenderer", "Lnet/minecraft/client/renderer/ChestRenderer;"));
	    
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
	    initMethod.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", "blockModelShapes", "Lnet/minecraft/client/renderer/BlockModelShapes;"));
	    
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    initMethod.instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer"));
	    initMethod.instructions.add(new InsnNode(Opcodes.DUP));
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    initMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "<init>", "(Lnet/minecraft/client/renderer/color/BlockColors;)V", false));
	    initMethod.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", "blockModelRenderer", "Lnet/minecraft/client/renderer/BlockModelRenderer;"));
	    
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    initMethod.instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/client/renderer/BlockFluidRenderer"));
	    initMethod.instructions.add(new InsnNode(Opcodes.DUP));
	    initMethod.instructions.add(new TypeInsnNode(Opcodes.NEW, "com/wynprice/brl/addons/plastic/PlasticLiquidColors"));
	    initMethod.instructions.add(new InsnNode(Opcodes.DUP));
	    initMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    initMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/wynprice/brl/addons/plastic/PlasticLiquidColors", "<init>", "(Lnet/minecraft/client/renderer/color/BlockColors;)V", false));
	    initMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/renderer/BlockFluidRenderer", "<init>", "(Lnet/minecraft/client/renderer/color/BlockColors;)V", false));
	    initMethod.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/BlockRendererDispatcher", "fluidRenderer", "Lnet/minecraft/client/renderer/BlockFluidRenderer;"));
	    
	    initMethod.instructions.add(new InsnNode(Opcodes.RETURN));
	    initMethod.instructions.add(endLabel);
	    
	    ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)
        	if(!((m.desc.equalsIgnoreCase(renderBlockMethodDesc) && m.name.equalsIgnoreCase(renderBlockMethodName)) ||
        			m.desc.equalsIgnoreCase(initMethodDesc) && m.name.equalsIgnoreCase(initMethodName)))
        		nodes.add(m);
        
        nodes.add(renderBlockMethod);
        nodes.add(initMethod);
        
        node.methods.clear();
        node.methods.addAll(nodes);
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
        
		return basicClass;
	}
	
	public static boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, BlockFluidRenderer fluidRender)
	{
		
		BRLGetStateEvent event = new BRLGetStateEvent(blockAccess, state, pos).post();
		
		state = event.getState();
		blockAccess = event.getAccess();
		
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

                        	{
                        		if(!Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(blockAccess, info.getModel(), info.getState(), pos, bufferBuilderIn, true))
                        			retBool = false;

                        	}
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
