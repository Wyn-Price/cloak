package com.wynprice.brl.addons.plastic;

import javax.vecmath.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;

public class PlasticVertexLighterFlat extends VertexLighterFlat
{
	public PlasticVertexLighterFlat(BlockColors colors) {
		super(colors);
	}

	private int tint = -1;
    private boolean diffuse = true;
    
    @Override
    protected void processQuad()
    {
    	float[][] position = quadData[posIndex];
        float[][] normal = null;
        float[][] lightmap = quadData[lightmapIndex];
        float[][] color = quadData[colorIndex];

        // If all three normal values are either -1 or 0, normals must be generated
        if(quadData[normalIndex][0][0] != quadData[normalIndex][0][1] ||
            quadData[normalIndex][0][1] != quadData[normalIndex][0][2] ||
           (quadData[normalIndex][0][0] != -1 && quadData[normalIndex][0][0] != 0))
        {
            normal = quadData[normalIndex];
        }
        else
        {
            normal = new float[4][4];
            Vector3f v1 = new Vector3f(position[3]);
            Vector3f t = new Vector3f(position[1]);
            Vector3f v2 = new Vector3f(position[2]);
            v1.sub(t);
            t.set(position[0]);
            v2.sub(t);
            v1.cross(v2, v1);
            v1.normalize();
            for(int v = 0; v < 4; v++)
            {
                normal[v][0] = v1.x;
                normal[v][1] = v1.y;
                normal[v][2] = v1.z;
                normal[v][3] = 0;
            }
        }

        int multiplier = -1;
        if(BufferedPlastic.plastic)
        {
        	multiplier = BufferedPlastic.getImageColor(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockInfo.getState()));
        }
        if(tint != -1)
        {
        	multiplier = blockInfo.getColorMultiplier(tint);
        }
        VertexFormat format = parent.getVertexFormat();
        int count = format.getElementCount();

        for(int v = 0; v < 4; v++)
        {
            position[v][0] += blockInfo.getShx();
            position[v][1] += blockInfo.getShy();
            position[v][2] += blockInfo.getShz();

            float x = position[v][0] - .5f;
            float y = position[v][1] - .5f;
            float z = position[v][2] - .5f;

            //if(blockInfo.getBlock().isFullCube())
            {
                x += normal[v][0] * .5f;
                y += normal[v][1] * .5f;
                z += normal[v][2] * .5f;
            }

            float blockLight = lightmap[v][0], skyLight = lightmap[v][1];
            updateLightmap(normal[v], lightmap[v], x, y, z);
            if(dataLength[lightmapIndex] > 1)
            {
                if(blockLight > lightmap[v][0]) lightmap[v][0] = blockLight;
                if(skyLight > lightmap[v][1]) lightmap[v][1] = skyLight;
            }
            updateColor(normal[v], color[v], x, y, z, 2 , multiplier);

            if(diffuse)
            {
                float d = LightUtil.diffuseLight(normal[v][0], normal[v][1], normal[v][2]);
                for(int i = 0; i < 3; i++)
                {
                    color[v][i] *= d;
                }
            }
            if(EntityRenderer.anaglyphEnable)
            {
                applyAnaglyph(color[v]);
            }

            // no need for remapping cause all we could've done is add 1 element to the end
            for(int e = 0; e < count; e++)
            {
                VertexFormatElement element = format.getElement(e);
                switch(element.getUsage())
                {
                    case POSITION:
                        // position adding moved to VertexBufferConsumer due to x and z not fitting completely into a float
                        /*float[] pos = new float[4];
                        System.arraycopy(position[v], 0, pos, 0, position[v].length);
                        pos[0] += blockInfo.getBlockPos().getX();
                        pos[1] += blockInfo.getBlockPos().getY();
                        pos[2] += blockInfo.getBlockPos().getZ();*/
                        parent.put(e, position[v]);
                        break;
                    case NORMAL: if(normalIndex != -1)
                    {
                        parent.put(e, normal[v]);
                        break;
                    }
                    case COLOR:
                        parent.put(e, color[v]);
                        break;
                    case UV: if(element.getIndex() == 1)
                    {
                        parent.put(e, lightmap[v]);
                        break;
                    }
                    default:
                        parent.put(e, quadData[e][v]);
                }
            }
        }
        tint = -1;
    }
    
    @Override
    public void setQuadTint(int tint)
    {
        this.tint = tint;
    }
    
    @Override
    public void setApplyDiffuseLighting(boolean diffuse) 
    {
    	this.diffuse = diffuse;
    }
}
