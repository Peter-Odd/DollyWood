package graphics.utilities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents 3D object with normals and triangulated faces.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Model {
	private ArrayList<Vector3f> verticies = new ArrayList<>();
	private ArrayList<Vector3f> normals = new ArrayList<>();
	private ArrayList<Face> faces = new ArrayList<>();

	/**
	 * Verticies getter.
	 * @return list of verticies
	 */
	public ArrayList<Vector3f> getVerticies() {
		return verticies;
	}

	/**
	 * Normal getter.
	 * @return list of normals.
	 */
	public ArrayList<Vector3f> getNormals() {
		return normals;
	}

	/**
	 * Face getter.
	 * @return list of faces.
	 * @see Face
	 */
	public ArrayList<Face> getFaces() {
		return faces;
	}

}
