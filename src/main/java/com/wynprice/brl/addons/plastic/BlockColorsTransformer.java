package com.wynprice.brl.addons.plastic;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

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
import com.wynprice.brl.core.BetterRenderCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.registries.IRegistryDelegate;

public class BlockColorsTransformer implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.color.BlockColors"))
			return basicClass;
		
		String methodName = BetterRenderCore.isDebofEnabled ? "func_186720_a" : "colorMultiplier";
		String methodDesc = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;I)I";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, methodDesc, null, new String[0]);
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();

	    //Thats a lotta damage
	    method.instructions = new InsnList();
	    method.instructions.add(startLabel);
        method.instructions.add(new LineNumberNode(183, startLabel));
        
        LocalVariableNode state = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_186724_1_" : "state", "Lnet/minecraft/block/state/IBlockState;", null, startLabel, endLabel, 0);
	    LocalVariableNode blockAccess = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_186724_2_" : "blockAccess", "Lnet/minecraft/world/IBlockAccess;", null, startLabel, endLabel, 1);
	    LocalVariableNode pos = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_186724_3_" : "pos", "Lnet/minecraft/util/math/BlockPos;", null, startLabel, endLabel, 2);
	    LocalVariableNode renderPass = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_186724_4_" : "renderPass", "I", null, startLabel, endLabel, 3);
        method.localVariables.addAll(Lists.newArrayList(new LocalVariableNode[]{state, pos, blockAccess, renderPass}));
	    
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/color/BlockColors", "blockColorMap" , "Ljava/util/Map;"));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
        method.instructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/addons/plastic/BlockColorsTransformer", "colorMultiplier", "(Ljava/util/Map;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;I)I", false));
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
	
	@Deprecated //TODO redo this bit. Its wayy to buggy
	public static int colorMultiplier(Map<IRegistryDelegate<Block>, IBlockColor> blockColorMap, IBlockState state, @Nullable IBlockAccess blockAccess, @Nullable BlockPos pos, int renderPass)
	{
		if(Thread.currentThread().getStackTrace()[4].getClassName().equals(VertexLighterFlat.class.getName()))
			renderPass--;
		if(renderPass == -1 && !BufferedPlastic.plastic)
			return -1;
		ArrayList<Block> noColorBlocks = Lists.newArrayList
				(
					Blocks.WATER,
					Blocks.FLOWING_WATER,
					Blocks.RED_FLOWER,
					Blocks.YELLOW_FLOWER
				);
		IBlockColor iblockcolor = blockColorMap.get(state.getBlock().delegate);
        return 
        		iblockcolor == null || (BufferedPlastic.plastic && (iblockcolor.colorMultiplier(state, blockAccess, pos, renderPass) == 0xFFFFFF
        							|| iblockcolor.colorMultiplier(state, blockAccess, pos, renderPass) == -1
        							|| noColorBlocks.contains(state.getBlock()))) ? 
        									
        				BufferedPlastic.plastic ? 
	        						BufferedPlastic.getImageColor(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state)) :
	        						-1 
        						: 
        				iblockcolor.colorMultiplier(state, blockAccess, pos, renderPass);
	}

}
