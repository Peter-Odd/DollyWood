package graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Random;

import graphics.utilities.AnimationEvent;
import graphics.utilities.AnimationState;
import graphics.utilities.AnimationEventController;
import graphics.utilities.Camera;
import graphics.utilities.Face;
import graphics.utilities.Model;
import graphics.utilities.ModelPart;
import graphics.utilities.OBJLoader;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import simulation.Animal;
import simulation.Grass;
import simulation.Race;
import simulation.Water;
import utilities.Globals;
import utilities.HexagonUtils;

/**
 * <img src="http://www.geekend.fr/wp-content/uploads/2012/02/Lwjgl_logo.jpg" style="width:30%"><br />
 * Creates a window in which the world will be displayed in breathtaking 3D<br />
 * Uses LWJGL to create all graphics.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Graphics3D {
	private Camera camera;
	private int modelDisplayList;
	private HashMap<String, Integer> models = new HashMap<String, Integer>();
	private float size = 3.5f;
	
	private AnimationEventController animationEventController = new AnimationEventController(40); //24 FPS
	/**
	 * This is all that is needed.<br />
	 * Everything is dependant on Globals, so make sure to setup Globals before creating Graphics3D object or it will not work.<br />
	 * Press F to toggle fullscreen <br />
	 * Press escape to exit the application.<br />
	 * Note: Only use escape to exit!
	 */
	public Graphics3D(){
		setupDisplay();
		setupCamera();
		setupStates();
		setupLighting();
		setupStarterAnimation();
		

        camera.pitch = -90.0f;
        glMatrixMode(GL_MODELVIEW);
        long lastTime = 0;
		while(!Display.isCloseRequested()){
			processInput();
			long time = System.currentTimeMillis();
	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	        glPushAttrib(GL_TRANSFORM_BIT);
	        glPushMatrix();
	        glLoadIdentity();
	        camera.processInput(lastTime*0.05f);
	        camera.applyTranslations();

	        //animationEventController.step();
	        render();
	        
	        glPopAttrib();
	        
	        lastTime = System.currentTimeMillis() - time;
	        glPopMatrix();
			Display.update();
		}
	}

	private void processInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			try {
				Display.setFullscreen(!Display.isFullscreen());
				Thread.sleep(100);
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			Display.destroy();
			System.exit(0);
		}
	}

	private void setupStarterAnimation() {
		try {
			Random random = new Random();			

			float xRange = 4.0f;
			float yRange = 2.0f;
			for(int i = 0; i < 100; i++){
				Vector3f position = new Vector3f();
				String animation = "res/ButterflyAnimationRed.ani";
				if(random.nextInt(2) == 0)
					animation = "res/ButterflyAnimationGreen.ani";
				animationEventController.loadEvent(animation, "Butterfly"+i, position, new Vector3f(90.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
				position = new Vector3f(Globals.width/2*size+(random.nextFloat()*2.0f*xRange-xRange), Globals.height/2*size+5.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f+(random.nextFloat()*2.0f*yRange-yRange));
				animationEventController.addAnimationState(new AnimationState(position, new Vector3f(90.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 0.03f), "Butterfly"+i);

				animationEventController.addAnimationState(new AnimationState(new Vector3f(position.x, position.y-2.0f, position.z), new Vector3f(0.0f, 0.0f, 180.0f), new Vector3f(0.0f, 0.0f, 0.0f), 0.03f), "Butterfly"+i);
				position = new Vector3f(position.x+(random.nextFloat()*2.0f*xRange-xRange), position.y-5.0f, position.z+(random.nextFloat()*2.0f*yRange-yRange));
				animationEventController.addAnimationState(new AnimationState(new Vector3f(position.x, position.y-2.0f, position.z), new Vector3f(0.0f, 0.0f, 180.0f), new Vector3f(0.0f, 0.0f, 0.0f), 0.03f), "Butterfly"+i);

				animationEventController.setRandomAnimationSpeed("Butterfly"+i, 0.02f, 0.06f);
				animationEventController.setRandomModelSpeed("Butterfly"+i, 0.2f, 0.4f);
			}
			
			//animationEventController.loadEvent("res/ButterflyAnimation.ani", "ButterflyTest", new Vector3f(Globals.width/2*size, Globals.height/2*size+5.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
			//Vector3f position = new Vector3f(Globals.width/2*size+(random.nextFloat()*6.0f-3.0f), Globals.height/2*size-2.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f+(random.nextFloat()*6.0f-3.0f));
			//animationEventController.addAnimationState(new AnimationState(position, rotation, new Vector3f(1.0f, 1.0f, 1.0f), 0.03f), "ButterflyTest");
			
			animationEventController.loadEvent("res/startupAnimation.ani", "StartupAnimation", new Vector3f(Globals.width/2*size, Globals.height/2*size+20.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Thread animationControllerThread = new Thread(animationEventController);
		animationControllerThread.start();
	}

	private void render() {
        glTranslatef(-Globals.width/2*size, -Globals.height/2*size, -(Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f)); //Moves the world to keep the worldCenter at center point
        
        float worldSunIntensity = Math.abs(Globals.dayNightCycle.getTime()/12.0f-1.0f);
        updateLight(GL_LIGHT0, new Vector3f(Globals.width/2*size, Globals.height/2*size, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-150.0f), new Vector3f(worldSunIntensity, worldSunIntensity, worldSunIntensity));

		for(AnimationEvent animEvent : animationEventController.getEvents()){
			AnimationState currentState = animEvent.getStateSum();
			if(currentState != null){
				//System.out.println(currentState.model);
				renderModel(currentState.model, currentState.position, currentState.rotation, currentState.scale);
			}
		}

		//Render Water system
		float[][] cloudWaterLevel = Globals.water.getCloudWaterLevel();
        for(int x = 0; x < Globals.width; x++){
        	for(int y = 0; y < Globals.height; y++){
        		if(cloudWaterLevel[x][y] != 1.0f){
        			float cloudSize = cloudWaterLevel[x][y]*3.0f;
        			if(cloudWaterLevel[x][y] > 0.0f)
        				renderModel("Sphere", new Vector3f(x*size,y*size+((x%2)*(size/2)),-75.0f+cloudSize*2), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(cloudWaterLevel[x][y]*cloudSize, cloudWaterLevel[x][y]*cloudSize, cloudWaterLevel[x][y]*cloudSize));
        		}
            	if(Globals.water.getGroundWaterLevel(x, y) > 0.1f)
        			renderModel("Water", new Vector3f(x*size,y*size+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, Globals.water.getGroundWaterLevel(x, y)));
        	}
        }
		
        for(int x = 0; x < Globals.width; x++){
        	for(int y = 0; y < Globals.height; y++){
        		//Render ground tiles
        		renderModel("tile", new Vector3f(x*size,y*size+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
        		
        		//Render races
        		for(Race r:Globals.races){
        			Animal animal = r.getSpeciesAt(x, y);
        			//Render animal
        			if(animal != null)
        				renderModel(r.getSpecies(), new Vector3f(x*size,y*size+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, animal.getRotation()), new Vector3f(1.0f, 1.0f, 1.0f));
        			//Special case for plants
        			if(r.getSpecies().equals("Grass") && ((Grass)r).getGrassAt(x,y) > 0.1f){
                		renderModel("grass", new Vector3f(x*size,y*size+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, ((Grass)r).getGrassAt(x,y)));		
        			}
        		}
        	}
        }
        //Random tree
		renderModel("tree", new Vector3f(6*size,6*size+((6%2)*(size/2)),Globals.heightmap[6][6]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));
		
		//Render SkyDome
		float sphereScale = 60.0f;
		renderModel("sphereInvNorm", new Vector3f(Globals.width/2*size, Globals.height/2*size, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(sphereScale, sphereScale, sphereScale));

	}

	private void updateLight(int light, Vector3f position, Vector3f color) {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		//glTranslatef(-position.x+20, -position.y+20, -position.z+50);
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        glLight(light, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{position.x, position.y, position.z, 1.0f}).flip());
        glLight(light, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{color.x, color.y, color.z, 1.0f}).flip());
		glPopMatrix();
	}

	private void renderModel(String modelName, Vector3f position, Vector3f rotation, Vector3f size) {
		if(models.containsKey(modelName)){
			glTranslatef(position.x, position.y, position.z);
			glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
			glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
			glScalef(size.x, size.y, size.z);
			glCallList(models.get(modelName));
			glScalef(1.0f/size.x, 1.0f/size.y, 1.0f/size.z);
			glRotatef(-rotation.z, 0.0f, 0.0f, 1.0f);
			glRotatef(-rotation.y, 0.0f, 1.0f, 0.0f);
			glRotatef(-rotation.x, 1.0f, 0.0f, 0.0f);
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
				for(ModelPart modelPart : m.getModelParts()){
					Vector3f color = modelPart.getColor();
					glColor3f(color.x, color.y, color.z);
					glBegin(GL_TRIANGLES);
		            for (Face face : modelPart.getFaces()) {
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
		            }
		            glEnd();
				}
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
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        glLightModel(GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
        glLight(GL_LIGHT0, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{Globals.width/2*4.0f, Globals.height/2*4.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f, 1.0f}).flip());
        glLight(GL_LIGHT0, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{1.0f, 1.0f, 1.0f, 1.0f}).flip());
        //glLight(GL_LIGHT0, GL_SPOT_CUTOFF, (FloatBuffer)temp.asFloatBuffer().put(new float[]{100.0f, 100.0f, 100.0f, 1.0f}).flip());
        glLight(GL_LIGHT0, GL_SPOT_DIRECTION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
        //glLight(GL_LIGHT0, GL_QUADRATIC_ATTENUATION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.000001f, 0.000001f, 0.000001f, 1.0f}).flip());
        glLight(GL_LIGHT0, GL_SPOT_EXPONENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
        //glEnable(GL_CULL_FACE);
        //glCullFace(GL_FRONT);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private void setupStates() {
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH); //should be set to smooth by default but just in case.
	}

	private void setupCamera() {
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 70.0f, 0.01f, 1000.0f);
		camera.applyPerspective();
	}

	private void setupDisplay() {
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for(int i = 0; i < modes.length; i++){
				if(modes[i].getWidth() == Globals.screenWidth && modes[i].getHeight() == Globals.screenHeight && modes[i].isFullscreenCapable())
					Display.setDisplayMode(modes[i]);
			}
			//Display.setDisplayMode(new DisplayMode(Globals.screenWidth, Globals.screenHeight));
			Display.setTitle("DOLLYWOOD");
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
