package graphics.utilities;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utilities.Globals;

/**
 * Imaginary camera to navigate 3D world.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 *
 */
public class Camera {
	private Vector3f position;
	private Vector3f rotation;
	private float fov;
	private float aspectRatio;
	private Vector2f clippingPlane;
	

	private Camera(Vector3f position, Vector3f rotation, float fov, float aspectRatio, Vector2f clippingPlane){
		this.position = position;
		this.rotation = rotation;
		this.fov = fov;
		this.aspectRatio = aspectRatio;
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
		this(rotation, rotation, fov, Globals.screenWidth / Globals.screenHeight, new Vector2f(zNear,zFar));
	}
	
	/**
	 * Changes simulated camera position.
	 */
	public void applyTranslations(){
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glTranslatef(position.x, position.y, position.z);
        glPopAttrib();
	}
	
	/**
	 * Set perspective view to the world, if not run orthographic view will be used.
	 * <br><br><img src="http://media-cache-ak0.pinimg.com/originals/61/e6/ae/61e6aee28960b816fe55fbb0384dc859.jpg" style="width:30%">
	 * <br> <b>Top right object is in perspective view.</b>
	 */
	public void applyPerspective(){
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(70.0f, 1.0f, 0.0f, 100.0f);
//		GLU.gluPerspective(fov, aspectRatio, clippingPlane.x, clippingPlane.y);
		glPopAttrib();
	}
}
