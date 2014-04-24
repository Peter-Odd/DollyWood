package graphics.utilities;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import utilities.Globals;

/**
 * Imaginary camera to navigate 3D world.
 * @author OSM Group 5 - DollyWood project
 * @version 1.1
 *
 */
public class Camera {
	private Vector3f position;
	private float fov;
	private float aspectRatio;
	private Vector2f clippingPlane;
	private float pitch;
	private float yaw;
	private float roll;
	

	private Camera(Vector3f position, Vector3f rotation, float fov, float aspectRatio, Vector2f clippingPlane){
		this.position = position;
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.clippingPlane = clippingPlane;
		this.pitch = rotation.x;
		this.yaw = rotation.y;
		this.roll = rotation.z;
		
	}
	
	/**
	 * @param position Camera position
	 * @param rotation Camera rotation
	 * @param fov Field of View angle from center point to upper edge point. Horizontal fov specified by Globals screen size; {@link Globals#screenHeight} and {@link Globals#screenWidth}. 
	 * @param zNear Objects closer to the camera than zNear not visible, zNear >= 0.
	 * @param zFar Objects further away from camera than zFar not visible, zNear >= 0.
	 * <br><br><img src="http://www.incgamers.com/wp-content/uploads/2013/05/6a0120a85dcdae970b0120a86d9495970b.png" style="width:30%">
	 */
	public Camera(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar){
		this(rotation, rotation, fov, ((float)Globals.screenWidth / (float)Globals.screenHeight), new Vector2f(zNear,zFar));
	}
	
	/**
	 * Takes input from keyboard and mouse and changes the cameras position and rotation acordingly.<br />
	 * W = forward<br />
	 * S = backward<br />
	 * A = left<br />
	 * D = right<br />
	 * All the above movements are in x/y coordinates and depend on where the camera i looking<br />
	 * Left_Shift = down<br />
	 * Space = up<br />
	 * note that up/down only changes z position, and is unaffected by camera rotation<br />
	 * lastly the rotation of the camera is set by mouse DX/DY movement
	 * @param speedModifier sets how much the cameras position should change
	 */
	public void processInput(float speedModifier){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			relativeMovement(speedModifier, 0.0f, 0.0f);
        }
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			relativeMovement(-speedModifier, 0.0f, 0.0f);
        }
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			relativeMovement(speedModifier, 90.0f, 0.0f);
        }
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			relativeMovement(-speedModifier, 90.0f, 0.0f);
        }
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.z += speedModifier;
        }
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.z -= speedModifier;
        }

		roll += Mouse.getDX();
		pitch -= Mouse.getDY();

        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } 
        else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }
	}    
	
	private void relativeMovement(float speed, float angleXOffset, float angleYOffset){
        position.y -= speed * (float) cos(toRadians(roll-angleXOffset));
        position.x -= speed * (float) sin(toRadians(roll-angleXOffset));
        //position.z += speed * (float) sin(toRadians(roll - angleYOffset));
	}
	
	/**
	 * Changes simulated camera position and rotation.
	 */
	public void applyTranslations(){
        glPushAttrib(GL_TRANSFORM_BIT);
        glRotatef(pitch, 1, 0, 0);
        glRotatef(yaw, 0, 1, 0);
        glRotatef(roll, 0, 0, 1);
        glTranslatef(position.x, position.y, position.z);
        glPopAttrib();
	}
	
	/**
	 * Set perspective view to the world, if not run orthographic view will be used.
	 * <br><br><img src="http://media-cache-ak0.pinimg.com/originals/61/e6/ae/61e6aee28960b816fe55fbb0384dc859.jpg" style="width:30%">
	 * <br> <b>Top right object is in perspective view.</b>
	 */
	public void applyPerspective(){
		//glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//gluPerspective(170.0f, 1.0f, 0.1f, 1.0f);
		gluPerspective(fov, aspectRatio, clippingPlane.x, clippingPlane.y);
		//glPopAttrib();
	}

	/**
	 * @return The current position vector of the camera
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Sets the position of the camera. Note that the camera won't move unless you call applyTranslations() afterwards
	 * @param position The new position of the camera
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
}
