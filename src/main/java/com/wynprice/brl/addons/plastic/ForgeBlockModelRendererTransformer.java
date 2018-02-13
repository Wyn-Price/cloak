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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;

public class ForgeBlockModelRendererTransformer  implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer"))
			return basicClass;
		
		String methodName = "<init>";
		String methodDesc = "(Lnet/minecraft/client/renderer/color/BlockColors;)V";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, methodDesc, null, new String[0]);
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();

	    //Thats a lotta damage
	    method.instructions = new InsnList();
	    method.instructions.add(startLabel);
        method.instructions.add(new LineNumberNode(46, startLabel));
	    
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/renderer/BlockModelRenderer", "<init>", "(Lnet/minecraft/client/renderer/color/BlockColors;)V", false));

        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/ThreadLocal"));
        method.instructions.add(new InsnNode(Opcodes.DUP));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/ThreadLocal", "<init>", "()V", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "wrFlat", "Ljava/lang/ThreadLocal;"));
        
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/ThreadLocal"));
        method.instructions.add(new InsnNode(Opcodes.DUP));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/ThreadLocal", "<init>", "()V", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "wrSmooth", "Ljava/lang/ThreadLocal;"));
        
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/ThreadLocal"));
        method.instructions.add(new InsnNode(Opcodes.DUP));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/ThreadLocal", "<init>", "()V", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "lastRendererFlat", "Ljava/lang/ThreadLocal;"));
        
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/ThreadLocal"));
        method.instructions.add(new InsnNode(Opcodes.DUP));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/ThreadLocal", "<init>", "()V", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "lastRendererSmooth", "Ljava/lang/ThreadLocal;"));
        
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/addons/plastic/ForgeBlockModelRendererTransformer", "createLighterFlat", "(Lnet/minecraft/client/renderer/color/BlockColors;)Ljava/lang/ThreadLocal;", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "lighterFlat", "Ljava/lang/ThreadLocal;"));
        
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/addons/plastic/ForgeBlockModelRendererTransformer", "createLighterSmooth", "(Lnet/minecraft/client/renderer/color/BlockColors;)Ljava/lang/ThreadLocal;", false));
        method.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraftforge/client/model/pipeline/ForgeBlockModelRenderer", "lighterSmooth", "Ljava/lang/ThreadLocal;"));
        
	    method.instructions.add(new InsnNode(Opcodes.RETURN));
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
	
	public static ThreadLocal<VertexLighterFlat> createLighterFlat(BlockColors colors)
	{
		return ThreadLocal.withInitial(() -> new PlasticVertexLighterFlat(colors));
	}
	
	public static ThreadLocal<VertexLighterSmoothAo> createLighterSmooth(BlockColors colors)
	{
		return ThreadLocal.withInitial(() -> new PlasticVertexLighterFlaSmoothAo(colors));
	}
}