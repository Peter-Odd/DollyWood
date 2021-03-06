package graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

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
import simulation.Cloud;
import simulation.Grass;
import simulation.Race;
import simulation.Sheep;
import simulation.Wolf;
import utilities.Globals;
import utilities.SoundController;

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
	 * Press tab to view settings and statistics<br />
	 * Press 1 to spawn a sheep at camera position<br />
	 * Press 2 to spawn a wolf at camera position<br />
	 * Press escape to exit the application.<br />
	 * Note: Only use escape to exit!
	 */
	public Graphics3D(){
		splashScreen(5000);
		setupDisplay();
		setupCamera();
		setupStates();
		setupLighting();
		setupStarterAnimation();

		SoundController.size = size;
		SoundController.camera = camera;

		camera.pitch = -90.0f;
		glMatrixMode(GL_MODELVIEW);
		long lastTime = 0;
		updateLight(GL_LIGHT1, camera.getPosition(), new Vector3f(0.3f, 0.35f, 0.45f));
		while(!Display.isCloseRequested()){
			processInput();
			long time = System.currentTimeMillis();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glPushAttrib(GL_TRANSFORM_BIT);
			glPushMatrix();
			glLoadIdentity();
			camera.applyPerspective();
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

	/**
	 * Displays a window for delay ms while the world loads.
	 * @param delay
	 */
	private void splashScreen(int delay) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g){
				try {
					BufferedImage image = ImageIO.read(new File("res/ICON.PNG"));
					g.drawImage(image, 0, 0, 512, 512, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		panel.setPreferredSize(new Dimension(512, 512));
		frame.add(panel, BorderLayout.CENTER);
		JProgressBar progressBar = new JProgressBar(0, delay);
		frame.add(progressBar, BorderLayout.SOUTH);

		frame.setUndecorated(true);
		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width/2-(frame.getWidth()/2), screenSize.height/2-(frame.getHeight()/2));
		
		frame.setVisible(true);
		for(int i = 0; i < delay; i++){
			progressBar.setValue(i);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		frame.setVisible(false);
	}

	/**
	 * Handles keyboard input related to the main graphic part.
	 * so, key 'f' to toggle fullscreen, and escape to exit the program
	 * tab for settings and statistics
	 * '1' and '2' for spawning sheep and wolves
	 */
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
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && !Globals.visibleSettingsFrame()){
			Globals.createSettingsFrame(false, false, true);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_1)){ //Sheep
			for(Race r : Globals.races){
				if(r.getSpecies().equals("Sheep")){
					int[] cameraPos = camera.getArrayPosition(size);
					cameraPos[0] %= Globals.width;
					cameraPos[1] %= Globals.height;
					if(r.getSpeciesAt(cameraPos[0], cameraPos[1]) == null){
						Sheep sheep = new Sheep(cameraPos[0], cameraPos[1], r);
						Thread t = new Thread(sheep);
						r.setSpeciesAt(cameraPos[0], cameraPos[1], sheep);
						t.start();
					}
				}
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)){ //Wolf
			for(Race r : Globals.races){
				if(r.getSpecies().equals("Wolf")){
					int[] cameraPos = camera.getArrayPosition(size);
					cameraPos[0] %= Globals.width;
					cameraPos[1] %= Globals.height;
					if(r.getSpeciesAt(cameraPos[0], cameraPos[1]) == null){
						Wolf wolf = new Wolf(cameraPos[0], cameraPos[1], r);
						Thread t = new Thread(wolf);
						r.setSpeciesAt(cameraPos[0], cameraPos[1], wolf);
						t.start();
					}
				}
			}
		}
	}

	/**
	 * adds the startup butterfly animations to the AnimationEventController
	 */
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
			animationEventController.loadEvent("res/startupAnimation.ani", "StartupAnimation", new Vector3f(Globals.width/2*size, Globals.height/2*size+20.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Thread animationControllerThread = new Thread(animationEventController);
		animationControllerThread.start();
	}

	/**
	 * Main render function.
	 * This renders animations, setups lights and calls other sub functions to render out everything to the screen.
	 */
	private void render() {
		Vector3f center = new Vector3f(-Globals.width/2*size, -Globals.height/2*size, -(Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f));
		glTranslatef(center.x, center.y, center.z); //Moves the world to keep the worldCenter at center point

		float worldSunIntensity = Math.abs(Globals.dayNightCycle.getTime()/12.0f-1.0f);
		Vector3f sunPosition = new Vector3f(Globals.width/2*size, Globals.height/2*size, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-150.0f);
		Vector3f.add(sunPosition, (Vector3f)camera.getPosition().negate(), sunPosition);
		updateLight(GL_LIGHT0, sunPosition, new Vector3f(0.15f+worldSunIntensity, worldSunIntensity, worldSunIntensity-0.2f));

		for(AnimationEvent animEvent : animationEventController.getEvents()){
			AnimationState currentState = animEvent.getStateSum();
			if(currentState != null){
				renderModel(currentState.model, currentState.position, currentState.rotation, currentState.scale);
			}
		}


		renderWorldFromCameraPosition((int) Globals.getSetting("Render distance", "Graphics"));

		//Render SkyDome
		float skyDomeScale = Globals.getSetting("Render distance", "Graphics")*8.0f;
		glTranslatef(-center.x, -center.y, -center.z);
		Vector3f skyDomePosition = (Vector3f) camera.getPosition().negate();
		renderModel("SphereInvNorm", new Vector3f(skyDomePosition), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(skyDomeScale, skyDomeScale, skyDomeScale));

	}

	/**
	 * Renders everything within visionRadius of the camera.
	 * Note that this is only in xy coordinate space.
	 * @param visionRadius the radius from the camera to render
	 */
	private void renderWorldFromCameraPosition(int visionRadius){
		int[] cameraPos = camera.getArrayPosition(size);
		int xOffset = (int) (cameraPos[0]/Globals.width*(size*Globals.width));
		int yOffset = (int) (cameraPos[1]/Globals.height*(size*Globals.height));

		//Render cloud system
		for(Cloud c : Globals.water.getClouds()){
			if(c.getSize() > 0.01f){
				renderModel("Sphere", new Vector3f(c.getxPos()*size+xOffset, c.getyPos()*size+yOffset, -75.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(c.getSize(), c.getSize(), c.getSize()));
				if(c.downfall()){
					glPointSize(3.0f);
					glColor3f(0.0f, 1.2f, 2.0f);
					Random random = new Random();
					glBegin(GL_POINTS);
					for(int i = 0; i < 150; i++){
						Vector3f vertex = new Vector3f(c.getxPos()*size+xOffset+(random.nextFloat()*3.0f-1.0f)*c.getSize(), c.getyPos()*size+yOffset+(random.nextFloat()*3.0f-1.0f)*c.getSize(), -75.0f-(random.nextFloat()*30.0f));
						glVertex3f(vertex.x, vertex.y, vertex.z);
					}
					glEnd();
				}
			}
		}

		for(int xX = -visionRadius; xX <= visionRadius; xX++){
			for(int yY = -visionRadius; yY <= visionRadius; yY++){
				int x = cameraPos[0] + xX;
				int y = cameraPos[1] + yY;
				xOffset = (int) (x/Globals.width*(size*Globals.width));
				yOffset = (int) (y/Globals.height*(size*Globals.height));
				if(x < 0){
					x = Globals.width - x-2;
				}
				if(y < 0){
					y = Globals.height - y-2;
				}
				x %= Globals.width;
				y %= Globals.height;

				//Render ground tiles
				renderModel("tile", new Vector3f(x*size+xOffset,y*size+yOffset+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));

				//Render Water
				if(Globals.water.getGroundWaterLevel(x, y) > 0.7f)
					renderModel("Water", new Vector3f(x*size+xOffset,y*size+yOffset+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, Globals.water.getGroundWaterLevel(x, y)));

				//Render races
				for(Race r:Globals.races){
					Animal animal = r.getSpeciesAt(x, y);
					//Render animal
					if(animal != null)

						renderModel(r.getSpecies(), new Vector3f(x*size+xOffset,y*size+yOffset+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, animal.getRotation()), new Vector3f(animal.getSize(), animal.getSize(), animal.getSize()));

					//Special case for plants
					if(r.getSpecies().equals("Grass") && ((Grass)r).getGrassAt(x,y) > 0.1f){
						renderModel("grass", new Vector3f(x*size+xOffset,y*size+yOffset+((x%2)*(size/2)),Globals.heightmap[x][y]/1.0f-200.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, ((Grass)r).getGrassAt(x,y)));		
					}
				}
			}
		}
	}

	/**
	 * Updates the position and color of a light
	 * @param light the light to update, this should be GL_LIGHTx, where x=0-9
	 * @param position a vector that points to the position of the light after movement
	 * @param color a vector that holds color information of the light
	 */
	private void updateLight(int light, Vector3f position, Vector3f color) {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		glLight(light, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{position.x, position.y, position.z, 1.0f}).flip());
		glLight(light, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{color.x, color.y, color.z, 1.0f}).flip());
		glPopMatrix();
	}

	private HashMap<Integer, FloatBuffer> lightScale = new HashMap<>();
	/**
	 * Scales the light intensity
	 * @param lights all lights to be scaled each of these integers should be GL_LIGHTx where x = 0-9
	 * @param scale the amount to scale
	 */
	private void scaleLights(int[] lights, float scale) {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		for(int l = 0; l < lights.length; l++){
			if(!lightScale.containsKey(l)){
				ByteBuffer temp = ByteBuffer.allocateDirect(16);
				temp.order(ByteOrder.nativeOrder());
				FloatBuffer fBuffer = temp.asFloatBuffer();
				lightScale.put(l, fBuffer);
				glGetLight(lights[l], GL_DIFFUSE, fBuffer);
			}
			FloatBuffer fBuffer = lightScale.get(l);
			for(int i = 0; i < fBuffer.capacity(); i++){
				float f = fBuffer.get(i);
				f *= scale;
				fBuffer.put(i, f);
			}
			glLight(lights[l], GL_DIFFUSE, fBuffer);
		}
		glPopMatrix();
	}
	
	/**
	 * Renders a model that has the key modelName.
	 * If such a model does not exist, it will try to load one.
	 * @param modelName The name of the model. there should be an .obj file in res/ that has the name "res/modelName.obj".
	 * @param position The position of where to render the model
	 * @param rotation The rotation of the model.
	 * @param size The scale of the model. Do note that having any part of this vector set to 0 will "implode" the world.
	 */
	private void renderModel(String modelName, Vector3f position, Vector3f rotation, Vector3f size) {
		if(models.containsKey(modelName)){
			glTranslatef(position.x, position.y, position.z);
			glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
			glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
			float lightScale = Math.min(size.x, Math.min(size.y, size.z));
			if(lightScale != 1.0f)
				scaleLights(new int[]{GL_LIGHT0, GL_LIGHT1}, lightScale);
			glScalef(size.x, size.y, size.z);
			glCallList(models.get(modelName));
			glScalef(1.0f/size.x, 1.0f/size.y, 1.0f/size.z);
			if(lightScale != 1.0f)
				scaleLights(new int[]{GL_LIGHT0, GL_LIGHT1}, 1.0f/lightScale);
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

	/**
	 * Loads a .obj file into a Model object and then converts the Model to a list that is renderable using openGL.
	 * @param modelName The name of the model. there should be an .obj file in res/ that has the name "res/modelName.obj".
	 * @return the integer handle to the openGL compiled list
	 * @see OBJLoader
	 */
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

	/**
	 * Initial setup of all lighting.
	 * This will setup ambient light.
	 * start a few light sources and setup their color, position, cutoff and attenuation.
	 * it will also enable the light sources
	 */
	private void setupLighting() {
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		glLightModel(GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{Globals.width/2*4.0f, Globals.height/2*4.0f, Globals.heightmap[Globals.width/2][Globals.height/2]/1.0f-180.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{1.0f, 1.0f, 1.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_SPOT_DIRECTION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_SPOT_EXPONENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0001f, 0.0001f, 0.0001f, 1.0f}).flip());
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	/**
	 * Sets up global openGL states.
	 * Mostly stuff like enable GL_DEPTH_TEST
	 */
	private void setupStates() {
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH); //should be set to smooth by default but just in case.
	}

	/**
	 * Sets up a camera with perspective.
	 */
	private void setupCamera() {
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 0.01f, 1000.0f);
		camera.applyPerspective();
	}

	/**
	 * Opens the window where everything will be contained.
	 */
	private void setupDisplay() {
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for(int i = 0; i < modes.length; i++){
				if(modes[i].getWidth() == Globals.screenWidth && modes[i].getHeight() == Globals.screenHeight && modes[i].isFullscreenCapable())
					Display.setDisplayMode(modes[i]);
			}

			Display.setTitle("DOLLYWOOD");

			ByteBuffer[] iconList = new ByteBuffer[2];
			iconList[0] = loadIcon("res/ICON16.png");
			iconList[1] = loadIcon("res/ICON32.png");
			Display.setIcon(iconList);

			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * loads a taskbar/window icon to be used for the display.
	 * @param filename The location of the file to load
	 * @return ByteBuffer of the image
	 * @throws IOException
	 */
	private ByteBuffer loadIcon(String filename) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename)); // load image
		// convert image to byte array
		byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
		int counter = 0;
		for (int x = 0; x < image.getHeight(); x++)
			for (int y = 0; y < image.getWidth(); y++)
			{
				int colorSpace = image.getRGB(y, x);
				buffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
				buffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
				buffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
				buffer[counter + 3] = (byte) (colorSpace >> 24);
				counter += 4;
			}
		return ByteBuffer.wrap(buffer);
	}
}
