package graphics.utilities;

import org.lwjgl.util.vector.Vector3f;

/**
 * 3D objects are made up by faces. Only triangular faces are represented. 
 * @see Model
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Face {
	Vector3f verticies; //Index for 3 verticies
	Vector3f normals; //Index for 3 normals
	
	/**
	 * 
	 * @param verticies Index in {@link Model#getVerticies()} for three verticies.
	 * @param normals Index in {@link Model#getNormals()} for three normals.
	 */
	public Face(Vector3f verticies, Vector3f normals){
		this.verticies = verticies;
		this.normals = normals;
	}
}
