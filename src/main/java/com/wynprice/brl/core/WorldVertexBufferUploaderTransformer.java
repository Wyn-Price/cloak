package com.wynprice.brl.core;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
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

import com.wynprice.brl.BRBufferBuilder;
import com.wynprice.brl.addons.plastic.BufferedPlastic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;

public class WorldVertexBufferUploaderTransformer implements IClassTransformer
{
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.WorldVertexBufferUploader"))
			return basicClass;
		
		String methodName = BetterRenderCore.isDebofEnabled ? "func_181679_a" : "draw";
		String methodDesc = "(Lnet/minecraft/client/renderer/BufferBuilder;)V";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, methodDesc, null, new String[0]);
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    method.instructions = new InsnList();
	    method.instructions.add(startLabel);
	    method.instructions.add(new LineNumberNode(52, startLabel));
	    	    
	    LocalVariableNode bufferBuilderIn = new LocalVariableNode(BetterRenderCore.isDebofEnabled ? "p_181679_1_" : "bufferBuilderIn", "Lnet/minecraft/client/renderer/BufferBuilder;", null, startLabel, endLabel, 1);
	    
	    method.localVariables.add(bufferBuilderIn);
	    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
	    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/core/WorldVertexBufferUploaderTransformer", "draw", "(Lnet/minecraft/client/renderer/BufferBuilder;)V", false));
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
	
	public static void draw(BufferBuilder bufferBuilderIn)
	{
		if(bufferBuilderIn instanceof BRBufferBuilder)
		{
			int bindedTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
			boolean flag = bindedTexture == Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).getGlTextureId();
			for(ResourceLocation location : ((BRBufferBuilder)bufferBuilderIn).builderMap.keySet())
			{
				BufferBuilder builder = ((BRBufferBuilder)bufferBuilderIn).builderMap.get(location);
				
				if(BufferedPlastic.plastic)
					GlStateManager.bindTexture(BufferedPlastic.getTextureID());
				else
					Minecraft.getMinecraft().renderEngine.bindTexture(location);
				
				if(builder instanceof BRBufferBuilder)
					throw new IllegalArgumentException("BRBufferBuilder was found nested inside another BRBufferBuilder. This should not happen");
				
				draw(builder);
			}
			if(flag)
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			}
		}
		else
		{
			if (bufferBuilderIn.getVertexCount() > 0)
	        {
	            VertexFormat vertexformat = bufferBuilderIn.getVertexFormat();
	            int i = vertexformat.getNextOffset();
	            List<VertexFormatElement> list = vertexformat.getElements();
	            ByteBuffer bytebuffer = bufferBuilderIn.getByteBuffer();
	            for (int j = 0; j < list.size(); ++j)
	            {
	                VertexFormatElement vertexformatelement = list.get(j);
	                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
	                int k = vertexformatelement.getType().getGlConstant();
	                int l = vertexformatelement.getIndex();
	                bytebuffer.position(vertexformat.getOffset(j));
	
	                // moved to VertexFormatElement.preDraw
	                vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
	            }
	
	            GlStateManager.glDrawArrays(bufferBuilderIn.getDrawMode(), 0, bufferBuilderIn.getVertexCount());
	            int i1 = 0;
	
	            for (int j1 = list.size(); i1 < j1; ++i1)
	            {
	                VertexFormatElement vertexformatelement1 = list.get(i1);
	                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
	                int k1 = vertexformatelement1.getIndex();
	
	                // moved to VertexFormatElement.postDraw
	                vertexformatelement1.getUsage().postDraw(vertexformat, i1, i, bytebuffer);
	            }
	        }
	        bufferBuilderIn.reset();
		}
    }
}
