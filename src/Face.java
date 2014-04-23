/**
 * @exclude
 */


import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;


public class Face
{
	public Vector3f vertex = new Vector3f(); //lists the three vertices needed to create face
	public Vector3f normal = new Vector3f();
	public Color color;
	public Face(Vector3f vertex, Vector3f normal)
	{
		this.vertex = vertex;
		this.normal = normal;
	}
	
	public Face(Vector3f vertex, Vector3f normal, Color color)
	{
		this.vertex = vertex;
		this.normal = normal;
		this.color = color;
	}
}