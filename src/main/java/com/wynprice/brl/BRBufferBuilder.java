package com.wynprice.brl;

import java.nio.ByteBuffer;
import java.util.HashMap;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;


/**
 * Used as a extended BufferBuilder to keep track of if and where the texture changes 
 * @author Wyn Price
 *
 */
public class BRBufferBuilder extends BufferBuilder
{
	
	private BufferBuilder currentBuilder;

	private final int bufferSize;
	
	public final HashMap<ResourceLocation, BufferBuilder> builderMap = new HashMap<>();
	
	public BRBufferBuilder(int bufferSizeIn) {
		super(0); //Be 0, as this class isnt technically used as a bufferbuilder
		this.bufferSize = bufferSizeIn;
		this.currentBuilder = new BufferBuilder(this.bufferSize);
		builderMap.put(TextureMap.LOCATION_BLOCKS_TEXTURE, this.currentBuilder);

	}	
	
	@Override
	public void addVertexData(int[] vertexData) {
		currentBuilder.addVertexData(vertexData);
	}
	
	private int begunGlMode;
	private VertexFormat begunFormat;
	
	boolean isDrawing;

	@Override
	public void begin(int glMode, VertexFormat format) 
	{
		this.isDrawing = true;
		this.begunGlMode = glMode;
		this.begunFormat = format;
		for(BufferBuilder builder : builderMap.values())
			builder.begin(glMode, format);
	}

	@Override
	public void finishDrawing() 
	{
		this.isDrawing = false;
		for(BufferBuilder builder : builderMap.values())
			builder.finishDrawing();
	}
	
	@Override
	public BufferBuilder color(float red, float green, float blue, float alpha) {
		return currentBuilder.color(red, green, blue, alpha);
	}

	@Override
	public BufferBuilder color(int red, int green, int blue, int alpha) {
		return currentBuilder.color(red, green, blue, alpha);
	}

	@Override
	public void endVertex() {
		currentBuilder.endVertex();
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return currentBuilder.getByteBuffer();
	}

	@Override
	public int getColorIndex(int vertexIndex) {
		return currentBuilder.getColorIndex(vertexIndex);
	}

	@Override
	public int getDrawMode() {
		return currentBuilder.getDrawMode();
	}

	@Override
	public int getVertexCount() {
		return currentBuilder.getVertexCount();
	}

	@Override
	public VertexFormat getVertexFormat() {
		return currentBuilder.getVertexFormat();
	}

	@Override
	public State getVertexState() {
		return currentBuilder.getVertexState();
	}

	@Override
	public boolean isColorDisabled() {
		return currentBuilder.isColorDisabled();
	}

	@Override
	public BufferBuilder lightmap(int p_187314_1_, int p_187314_2_) {
		return currentBuilder.lightmap(p_187314_1_, p_187314_2_);
	}

	@Override
	public void noColor() {
		currentBuilder.noColor();
	}

	@Override
	public BufferBuilder normal(float x, float y, float z) {
		return currentBuilder.normal(x, y, z);
	}

	@Override
	public BufferBuilder pos(double x, double y, double z) {
		return currentBuilder.pos(x, y, z);
	}

	@Override
	public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
		currentBuilder.putBrightness4(p_178962_1_, p_178962_2_, p_178962_3_, p_178962_4_);
	}

	@Override
	public void putColor4(int argb) {
		 currentBuilder.putColor4(argb);
	}

	@Override
	public void putColorMultiplier(float red, float green, float blue, int vertexIndex) {
		currentBuilder.putColorMultiplier(red, green, blue, vertexIndex);
	}

	@Override
	public void putColorRGB_F(float red, float green, float blue, int vertexIndex) {
		currentBuilder.putColorRGB_F(red, green, blue, vertexIndex);
	}

	@Override
	public void putColorRGB_F4(float red, float green, float blue) {
		currentBuilder.putColorRGB_F4(red, green, blue);
	}

	@Override
	public void putColorRGBA(int index, int red, int green, int blue) {
		currentBuilder.putColorRGBA(index, red, green, blue);
	}

	@Override
	public void putColorRGBA(int index, int red, int green, int blue, int alpha) {
		currentBuilder.putColorRGBA(index, red, green, blue, alpha);
	}

	@Override
	public void putNormal(float x, float y, float z) {
		currentBuilder.putNormal(x, y, z);
	}

	@Override
	public void putPosition(double x, double y, double z) {
		currentBuilder.putPosition(x, y, z);
	}

	@Override
	public void reset() {
		currentBuilder.reset();
	}

	private Vec3d currentTranslation;
	
	@Override
	public void setTranslation(double x, double y, double z) {
		currentBuilder.setTranslation(x, y, z);
		this.currentTranslation = new Vec3d(x, y, z);
	}

	@Override
	public void setVertexState(State state) {
		currentBuilder.setVertexState(state);
	}

	@Override
	public void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_) {
		currentBuilder.sortVertexData(p_181674_1_, p_181674_2_, p_181674_3_);
	}
	
	@Override
	public BufferBuilder tex(double u, double v) {
		return currentBuilder.tex(u, v);
	}
	
	public boolean isDrawing() { //Needed for Optifine
		return isDrawing;
	}
	
	public void split(ResourceLocation location)
	{
		if(!builderMap.containsKey(location))
		{
			BufferBuilder builder = new BufferBuilder(bufferSize);
			if(isDrawing)
			builder.begin(begunGlMode, begunFormat);
			builderMap.put(location, builder);
		}
		this.currentBuilder = builderMap.get(location);
		this.currentBuilder.setTranslation(currentTranslation.x, currentTranslation.y, currentTranslation.z);
	}
    
}
