/**
 * @exclude
 */


import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class Main
{
    public static void main(String args[])
    {
	new Main();
    }

    
    Camera camera;
    public Main()
    {
		int screenWidth = 600;
		int screenHeight = 600;
		//Display setup
		try
		{
		    Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
		    Display.create();
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		//Black Magic
		glMatrixMode(GL_PROJECTION);
		glOrtho(0,screenWidth,screenHeight, 0, 100,-100);//Last 2 Z close/near clipping
		glMatrixMode(GL_MODELVIEW);
		
	
		setUpStates();
		loadModel("res/tile.obj");
		
		camera = new Camera(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 70.0f, 1.0f);
		camera.setPerspective();
		//camera.setOrthographic();
		while(!Display.isCloseRequested())
		{
			camera.processKeyboard();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //Only first part is relevant for 2D

			long time = System.currentTimeMillis();
			Model toRender = loadModel("res/tile.obj");
			for(int x = 0; x < 20; x++)
			{
				for(int y = 0; y < 20; y++)
				{
					float lineOffset = (y%2)*63.0f;
					render2DModel(toRender, 20.0f, (x*125.0f)+lineOffset, y*40.0f);
				}
			}
			Display.update();
			System.out.println(System.currentTimeMillis() - time);
		}
    }

    private void setUpStates() 
    {
    	float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };  // Ambient Light Values
        float lightDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };      // Diffuse Light Values
        float lightPosition[] = { 0.0f, 0.0f, 2.0f, 1.0f }; // Light Position

        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
        GL11.glEnable(GL11.GL_LIGHT1); 
    	
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_CULL_FACE);
        glShadeModel(GL_SMOOTH);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
        glColor3f(1.0f, 1.0f, 1.0f);
        glMaterialf(GL_FRONT, GL_SHININESS, 10f);
        if (GLContext.getCapabilities().GL_ARB_depth_clamp) {
            glEnable(ARBDepthClamp.GL_DEPTH_CLAMP);
        }
    }
    
    private Model loadModel(String file) 
    {
        int[] vbos;
        int vboVertexHandle;
        Model model = OBJLoader.loadModel(new File(file));
		vbos = OBJLoader.createVertexBuffer(model);
		vboVertexHandle = vbos[0];
        glEnableClientState(GL_VERTEX_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        return model;
	}

	private void render2DModel(Model renderModel, float sizeMultiplier, float xOffset, float yOffset)
    {
		glLoadIdentity();//Add Perspective
		//Move camera
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_MODELVIEW);
        glTranslatef(xOffset-150, yOffset-150, -500);
        float scale = sizeMultiplier;
        glScalef(scale, scale, scale);
        glPopAttrib();
		camera.translateCamera();
		glDrawArrays(GL_TRIANGLES, 0, renderModel.faces.size() * 3); // *3 because of Vector3f?
    }
}
