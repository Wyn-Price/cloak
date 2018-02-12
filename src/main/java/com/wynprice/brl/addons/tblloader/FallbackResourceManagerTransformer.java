package com.wynprice.brl.addons.tblloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;	

public class FallbackResourceManagerTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.resources.FallbackResourceManager"))
			return basicClass;
		
		String getResMethodName = BetterRenderCore.isDebofEnabled ? "func_110536_a" : "getResource";
		String getResMethodDesc = "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/resources/IResource;";
		
		
		String getStreamMethodName = BetterRenderCore.isDebofEnabled ? "func_177245_a" : "getInputStream";
		String getStreamMethodDesc = "(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/resources/IResourcePack;)Ljava/io/InputStream;";
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    
	    LabelNode startLabel = new LabelNode();
	    LabelNode endLabel = new LabelNode();
	    MethodNode getResMethod = new MethodNode(Opcodes.ACC_PUBLIC, getResMethodName, getResMethodDesc, null, new String[0]);
	    getResMethod.instructions = new InsnList();
	    getResMethod.instructions.add(startLabel);
        getResMethod.instructions.add(new LineNumberNode(41, startLabel));
        getResMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        getResMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getResMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/FallbackResourceManager", BetterRenderCore.isDebofEnabled ? "field_110540_a" : "resourcePacks", "Ljava/util/List;"));
        getResMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getResMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/FallbackResourceManager", BetterRenderCore.isDebofEnabled ? "field_110539_b" : "frmMetadataSerializer", "Lnet/minecraft/client/resources/data/MetadataSerializer;"));
        getResMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/brl/addons/tblloader/FallbackResourceManagerTransformer", "getResource", "(Lnet/minecraft/util/ResourceLocation;Ljava/util/List;Lnet/minecraft/client/resources/data/MetadataSerializer;)Lnet/minecraft/client/resources/IResource;", false));
	    getResMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
	    getResMethod.instructions.add(endLabel);
	    
	    startLabel = new LabelNode();
	    endLabel = new LabelNode();
	    MethodNode getStreamMethod = new MethodNode(Opcodes.ACC_PUBLIC, getStreamMethodName, getStreamMethodDesc, null, new String[0]);
	    getStreamMethod.instructions = new InsnList();
	    getStreamMethod.instructions.add(startLabel);
        getStreamMethod.instructions.add(new LineNumberNode(75, startLabel));
        getStreamMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        getStreamMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        getStreamMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/client/resources/IResourcePack", BetterRenderCore.isDebofEnabled ? "func_110590_a" : "getInputStream", "(Lnet/minecraft/util/ResourceLocation;)Ljava/io/InputStream;", true));
	    getStreamMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
	    getStreamMethod.instructions.add(endLabel);
	    
	    ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)
        	if(!((m.desc.equalsIgnoreCase(getResMethodDesc) && m.name.equalsIgnoreCase(getResMethodName)) ||
        		 (m.desc.equalsIgnoreCase(getStreamMethodDesc) && m.name.equalsIgnoreCase(getStreamMethodName)) ))
        		nodes.add(m);
        
        nodes.add(getResMethod);
        nodes.add(getStreamMethod);
        
        node.methods.clear();
        node.methods.addAll(nodes);
        
        
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
        
		return basicClass;
	}
	
    private static final Logger LOGGER = LogManager.getLogger();

    public static IResource getResource(ResourceLocation location, List<IResourcePack> resourcePacks, MetadataSerializer frmMetadataSerializer) throws IOException
    {
        checkResourcePath(location);
        IResourcePack iresourcepack = null;
        ResourceLocation resourcelocation = getLocationMcmeta(location);

        for (int i = resourcePacks.size() - 1; i >= 0; --i)
        {
            IResourcePack iresourcepack1 =  resourcePacks.get(i);
            
            if (iresourcepack == null && (iresourcepack1.resourceExists(resourcelocation)))
            {
                iresourcepack = iresourcepack1;
            }
            
            if(TBLImageLocation.hasStream(location))
            	return new SimpleResource(iresourcepack1.getPackName(), location, TBLImageLocation.getStream(location), null, frmMetadataSerializer);
            
            else if (iresourcepack1.resourceExists(location))
            {
                InputStream inputstream = null;

                if (iresourcepack != null)
                {
                    inputstream =  getInputStream(resourcelocation, iresourcepack);
                }

                return new SimpleResource(iresourcepack1.getPackName(), location, getInputStream(location, iresourcepack1), inputstream, frmMetadataSerializer);
            }
        }

        throw new FileNotFoundException(location.toString());
    }

    protected static InputStream getInputStream(ResourceLocation location, IResourcePack resourcePack) throws IOException
    {
        return resourcePack.getInputStream(location);
    }
    
    protected InputStream getInputStream2(ResourceLocation location, IResourcePack resourcePack) throws IOException
    {
        return resourcePack.getInputStream(location);
    }

    private static void checkResourcePath(ResourceLocation p_188552_1_) throws IOException
    {
        if (p_188552_1_.getResourcePath().contains(".."))
        {
            throw new IOException("Invalid relative path to resource: " + p_188552_1_);
        }
    }
      
    static ResourceLocation getLocationMcmeta(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".mcmeta");
    }
}
