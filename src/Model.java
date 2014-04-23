/**
 * @exclude
 */


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;


public class Model 
{
	public List<Vector3f> verticies = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<Face> faces = new ArrayList<Face>();
	
	public Model()
	{
		
	}
	
	public List<Face> getFaces()
	{
		return faces;
	}
	
	public List<Vector3f> getVerticies()
	{
		return verticies;
	}
}