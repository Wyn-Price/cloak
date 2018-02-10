package com.wynprice.brl;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import net.minecraft.client.renderer.BufferBuilder;


/**
 * Used as a extended BufferBuilder to keep track of if and where the texture changes 
 * @author Wyn Price
 *
 */
public class BLBufferBuilder extends BufferBuilder
{	
	
	public ArrayList<byte[]> extraRenders = new  ArrayList<>();
	
    public BLBufferBuilder(int bufferSizeIn)
    {
		super(bufferSizeIn);
    }
    
    
}
