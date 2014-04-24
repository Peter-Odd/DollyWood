package graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import graphics.utilities.Camera;
import graphics.utilities.Face;
import graphics.utilities.Model;
import graphics.utilities.OBJLoader;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import utilities.Globals;

public class Graphics3D {
	private Camera camera;
	private int modelDisplayList;
	private HashMap<String, Integer> models = new HashMap<String, Integer>();
	public Graphics3D(){
		setupDisplay();
		setupCamera();
		setupStates();
		setupLighting();
		
        glMatrixMode(GL_MODELVIEW);
        long lastTime = 0;
		while(!Display.isCloseRequested()){
			long time = System.currentTimeMillis();
	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	        glPushAttrib(GL_TRANSFORM_BIT);
	        glLoadIdentity();
	        camera.processInput(lastTime*0.05f);
	        camera.applyTranslations();

	        float size = 4.0f;
	        for(int x = 0; x < Globals.width; x++){
	        	for(int y = 0; y < Globals.height; y++){
	        		renderModel("tile", new Vector3f(x*size,y*size,Globals.heightmap[x][y]/1.0f-200.0f), null, null);
	        	}
	        }
	        
	        glPopAttrib();
	        
	        lastTime = System.currentTimeMillis() - time;
	        
			Display.update();
		}
	}

	private void renderModel(String modelName, Vector3f position, Vector3f rotation, Vector3f size) {
		if(models.containsKey(modelName)){
			glTranslatef(position.x, position.y, position.z);
			glCallList(models.get(modelName));
			glTranslatef(-position.x, -position.y, -position.z);
		}
		else
		{
			Integer newModel = setupModelList("res/" + modelName + ".obj");
			models.put(modelName, newModel);
			renderModel(modelName, position, rotation, size);
		}
	}

	private int setupModelList(String modelName) {
        modelDisplayList = glGenLists(1);
        glNewList(modelDisplayList, GL_COMPILE);
        {
			try {
				Model m;
				m = OBJLoader.loadModel(new File(modelName));
	            glColor3f(0.4f, 0.27f, 0.17f);
	            glBegin(GL_TRIANGLES);
	            for (Face face : m.getFaces()) {
	                Vector3f n1 = m.getNormals().get((int)(face.getNormals().x - 1));
	                glNormal3f(n1.x, n1.y, n1.z);
	                Vector3f v1 = m.getVerticies().get((int)(face.getVerticies().x - 1));
	                glVertex3f(v1.x, v1.y, v1.z);
	                
	                Vector3f n2 = m.getNormals().get((int)(face.getNormals().y - 1));
	                glNormal3f(n2.x, n2.y, n2.z);
	                Vector3f v2 = m.getVerticies().get((int)(face.getVerticies().y - 1));
	                glVertex3f(v2.x, v2.y, v2.z);
	                
	                Vector3f n3 = m.getNormals().get((int)(face.getNormals().z - 1));
	                glNormal3f(n3.x, n3.y, n3.z);
	                Vector3f v3 = m.getVerticies().get((int)(face.getVerticies().z - 1));
	                glVertex3f(v3.x, v3.y, v3.z);
	                //System.out.println("(" + v1.x + "," + v1.y + "," + v1.z + ")" + "(" + v2.x + "," + v2.y + "," + v2.z + ")" + "(" + v3.x + "," + v3.y + "," + v3.z + ")");
	            }
	            glEnd();
				//renderModel(m);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
        }
        glEndList();
		return modelDisplayList;
	}

	private void setupLighting() {
        glEnable(GL_DEPTH_TEST);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        glLightModel(GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.5f, 0.5f, 0.5f, 1.0f}).flip());
        glLight(GL_LIGHT0, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
        //glEnable(GL_CULL_FACE);
        //glCullFace(GL_FRONT);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private void setupStates() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH); //should be set to smooth by default but just in case.
	}

	private void setupCamera() {
		camera = new Camera(new Vector3f(0.0f, 0.0f, 10.0f), new Vector3f(0.0f, 0.0f, 0.0f), 70.0f, 0.01f, 1000.0f);
		camera.applyPerspective();
	}

	private void setupDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Globals.screenWidth, Globals.screenHeight));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}