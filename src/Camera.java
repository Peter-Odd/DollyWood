/**
 * @exclude
 */

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;


public class Camera 
{
	private float zNear = -100.0f;
	private float zFar = 100.0f;
	private float x,y,z;
	public float xRot,yRot,zRot;
	private float fov;
	private float aspect;
	public Camera(float x, float y, float z, float xRot, float yRot, float zRot, float fov, float aspect)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.xRot = xRot;
		this.yRot = yRot;
		this.zRot = zRot;
		this.fov = fov;
		this.aspect = aspect;
	}
	
	public void translateCamera()
	{
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_MODELVIEW);
        //glRotatef(xRot, 1, 0, 0);
        //glRotatef(yRot, 0, 1, 0);
        //glRotatef(zRot, 0, 0, 1);
        glTranslatef(-x, -y, -z);
        glPopAttrib();
	}
	
    public void setOrthographic() {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-aspect, aspect, -1, 1, 0, zFar);
        glPopAttrib();
    }
    public void setPerspective() {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fov, aspect, zNear, zFar);
        glPopAttrib();
    }
    
    public void processKeyboard() 
    {

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        float speed = 1.0f;
        
        if (keyRight && !keyLeft)
            x += speed;
        if (keyLeft && !keyRight)
            x -= speed;
        if (keyUp && !keyDown)
            y += speed;
        if (keyDown && !keyUp)
            y -= speed;
        if (flyUp && !flyDown) 
            z += speed;
        if (flyDown && !flyUp) 
            z -= speed;
    }
}
