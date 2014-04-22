package graphics;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	Vector3f verticies; //Index for 3 verticies
	Vector3f normals; //Invex for 3 normals
	public Face(Vector3f verticies, Vector3f normals){
		this.verticies = verticies;
		this.normals = normals;
	}
}
