package graphics.utilities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents 3D object with normals and triangulated faces.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class Model {
	private ArrayList<ModelPart> modelParts = new ArrayList<ModelPart>();
	
	public void addVertex(Vector3f vertex){
		if(modelParts.isEmpty())
			newModelPart();
		modelParts.get(modelParts.size()-1).getVerticies().add(vertex);
	}

	public void addNormal(Vector3f normal){
		if(modelParts.isEmpty())
			newModelPart();
		modelParts.get(modelParts.size()-1).getNormals().add(normal);
	}
	
	public void addFace(Face face){
		if(modelParts.isEmpty())
			newModelPart();
		modelParts.get(modelParts.size()-1).getFaces().add(face);
	}
	
	private void newModelPart() {
		modelParts.add(new ModelPart(new Vector3f(1.5f, 0.5f, 0.5f)));
	}
	
	public void newModelPart(Vector3f color) {
		modelParts.add(new ModelPart(color));
	}
	
	public ArrayList<ModelPart> getModelParts(){
		return modelParts;
	}
	
	/**
	 * Verticies getter.
	 * @return list of verticies
	 */
	public ArrayList<Vector3f> getVerticies() {
		ArrayList<Vector3f> verts = new ArrayList<Vector3f>();
		for(ModelPart p : modelParts)
			verts.addAll(p.getVerticies());
		return verts;
	}

	/**
	 * Normal getter.
	 * @return list of normals.
	 */
	public ArrayList<Vector3f> getNormals() {
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		for(ModelPart p : modelParts)
			normals.addAll(p.getNormals());
		return normals;
	}

	/**
	 * Face getter.
	 * @return list of faces.
	 * @see Face
	 */
	public ArrayList<Face> getFaces() {
		ArrayList<Face> faces = new ArrayList<Face>();
		for(ModelPart p : modelParts)
			faces.addAll(p.getFaces());
		return faces;
	}

}
