package com.wynprice.brl.core;

import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.wynprice.brl.BRBufferBuilder;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.BlockRenderLayer;

public class RegionRenderCacheBuilderTransformer implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.RegionRenderCacheBuilder"))
			return basicClass;
		
		String methodName = "<init>";
		String methodDesc = "()V";
		
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, methodDesc, null, new String[0]);
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    method.instructions = new InsnList();
	    method.instructions.add(startLabel);
        method.instructions.add(new LineNumberNode(14, startLabel));
	    
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/util/BlockRenderLayer", "values", "()[Lnet/minecraft/util/BlockRenderLayer;", false));
        method.instructions.add(new InsnNode(Opcodes.ARRAYLENGTH));
        method.instructions.add(new TypeInsnNode(Opcodes.ANEWARRAY, "net/minecraft/client/renderer/BufferBuilder"));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/RegionRenderCacheBuilder", BetterRenderCore.isDebofEnabled ? "field_179040_a" : "worldRenderers" , "[Lnet/minecraft/client/renderer/BufferBuilder;"));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/RegionRenderCacheBuilder", BetterRenderCore.isDebofEnabled ? "field_179040_a" : "worldRenderers" , "[Lnet/minecraft/client/renderer/BufferBuilder;"));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/core/RegionRenderCacheBuilderTransformer", "initRegionRenderCache", "([Lnet/minecraft/client/renderer/BufferBuilder;)V", false));
        method.instructions.add(new InsnNode(Opcodes.RETURN));
        
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
	
	public static void initRegionRenderCache(BufferBuilder[] worldRenderers)
	{
		 worldRenderers[BlockRenderLayer.SOLID.ordinal()] = new BRBufferBuilder(2097152);
	     worldRenderers[BlockRenderLayer.CUTOUT.ordinal()] = new BRBufferBuilder(131072);
	     worldRenderers[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BRBufferBuilder(131072);
	     worldRenderers[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BRBufferBuilder(262144);
	}
}
