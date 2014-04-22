package graphics;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Model {
	private ArrayList<Vector3f> verticies = new ArrayList<>();
	private ArrayList<Vector3f> normals = new ArrayList<>();
	private ArrayList<Face> faces = new ArrayList<>();
	
	public Model(){
		
	}

	public ArrayList<Vector3f> getVerticies() {
		return verticies;
	}

	public void setVerticies(ArrayList<Vector3f> verticies) {
		this.verticies = verticies;
	}

	public ArrayList<Vector3f> getNormals() {
		return normals;
	}

	public void setNormals(ArrayList<Vector3f> normals) {
		this.normals = normals;
	}

	public ArrayList<Face> getFaces() {
		return faces;
	}

	public void setFaces(ArrayList<Face> faces) {
		this.faces = faces;
	}
}
