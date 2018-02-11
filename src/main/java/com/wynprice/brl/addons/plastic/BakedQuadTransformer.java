package com.wynprice.brl.addons.plastic;

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
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;
import com.wynprice.brl.core.BetterRenderCore;

import net.minecraft.launchwrapper.IClassTransformer;

public class BakedQuadTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.block.model.BakedQuad"))
			return basicClass;
		
		
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    String methodNameHasTint = BetterRenderCore.isDebofEnabled ? "func_178212_b" : "hasTintIndex";
		String methodDescHasTint = "()Z";
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    MethodNode methodHasTint = new MethodNode(Opcodes.ACC_PUBLIC, methodNameHasTint, methodDescHasTint, null, new String[0]);
	    methodHasTint.instructions = new InsnList();
	    methodHasTint.instructions.add(startLabel);
        methodHasTint.instructions.add(new LineNumberNode(49, startLabel));
        methodHasTint.instructions.add(new InsnNode(Opcodes.ICONST_1));
	    methodHasTint.instructions.add(new InsnNode(Opcodes.IRETURN));
	    methodHasTint.instructions.add(endLabel);
	    
	    String methodNameGetTint = BetterRenderCore.isDebofEnabled ? "func_178211_c" : "getTintIndex";
		String methodDescGetTint = "()I";
	    startLabel = new LabelNode();
	    endLabel = new LabelNode();
	    MethodNode methodGetTint = new MethodNode(Opcodes.ACC_PUBLIC, methodNameGetTint, methodDescGetTint, null, new String[0]);
	    methodGetTint.instructions = new InsnList();
	    methodGetTint.instructions.add(startLabel);
	    methodGetTint.instructions.add(new LineNumberNode(54, startLabel));
	    methodGetTint.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    methodGetTint.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/block/model/BakedQuad", BetterRenderCore.isDebofEnabled ? "field_178213_b" : "tintIndex" , "I"));
	    methodGetTint.instructions.add(new InsnNode(Opcodes.ICONST_1));
	    methodGetTint.instructions.add(new InsnNode(Opcodes.IADD));
	    methodGetTint.instructions.add(new InsnNode(Opcodes.IRETURN));
	    methodGetTint.instructions.add(endLabel);
	    
	    ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)//Woah there, those are some nasty lookin' operators you got there. TODO SORT IT OUT
        	if(!(!(!m.desc.equalsIgnoreCase(methodDescHasTint) || !m.name.equalsIgnoreCase(methodNameHasTint)) || 
        			!(!m.desc.equalsIgnoreCase(methodDescGetTint) || !m.name.equalsIgnoreCase(methodNameGetTint))))
        		nodes.add(m);
        
        nodes.add(methodHasTint);
        nodes.add(methodGetTint);

        node.methods.clear();
        node.methods.addAll(nodes);
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
        
		return basicClass;
	}
	
}
