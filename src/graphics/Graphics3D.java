package graphics;

import java.io.File;
import java.io.FileNotFoundException;

import graphics.utilities.Camera;
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
	public Graphics3D(){
		setupDisplay();
		setupCamera();
		setupStates();
		
		while(!Display.isCloseRequested()){
			Display.update();
			try {
				Model m;
				m = loadModel("res/sheep.obj");
				renderModel(m);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private void renderModel(Model m) {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_MODELVIEW);
        glTranslatef(0.0f, 0.0f, 0.0f);
        float scale = 1.0f;
        glScalef(scale, scale, scale);
        glPopAttrib();
		camera.applyTranslations();
		glDrawArrays(GL_TRIANGLES, 0, m.getFaces().size() * 3);
	}

	private Model loadModel(String fileName) throws FileNotFoundException {
		Model model = OBJLoader.loadModel(new File(fileName));
		int[] vbo = OBJLoader.createVBO(model);
        glEnableClientState(GL_VERTEX_ARRAY);
		return model;
	}

	private void setupStates() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH); //should be set to smooth by default but just in case.
	}

	private void setupCamera() {
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 70.0f, 0.0f, 100.0f);
		camera.applyPerspective();
	}

	private void setupDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Globals.width, Globals.height));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
