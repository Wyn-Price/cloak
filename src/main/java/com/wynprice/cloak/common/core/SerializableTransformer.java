package com.wynprice.cloak.common.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

public class SerializableTransformer implements IClassTransformer
{
	
	public SerializableTransformer() {
        FMLLog.info("[BlockStateTransformer] Initialized.");
    }
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) //FUCk it, make everything Serializable
	{
//		ClassNode node = new ClassNode();
//	    ClassReader reader = new ClassReader(basicClass);
//	    reader.accept(node, 0);
//	    
//	    if(node.interfaces.contains("java/io/Serializable"))
//	    	node.interfaces.add("java/io/Serializable");
//	    
//		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        node.accept(writer);
//        basicClass = writer.toByteArray();
		return basicClass;
	}
	
}
