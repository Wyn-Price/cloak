package com.wynprice.brl;

import java.nio.ByteBuffer;

import net.minecraft.client.renderer.BufferBuilder;


/**
 * Used as a extended BufferBuilder to keep track of if and where the texture changes 
 * @author Wyn Price
 *
 */
public class BLBufferBuilder extends BufferBuilder
{	
    public BLBufferBuilder(int bufferSizeIn)
    {
		super(bufferSizeIn);
    }
    
    @Override
    public ByteBuffer getByteBuffer() 
    {
    	return super.getByteBuffer();
    }
}
