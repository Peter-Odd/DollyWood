package graphics.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Loads non-binary .OBJ-file
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class OBJLoader {
	
	/**
	 * Generates a model from <code> modelFile </code> 
	 * @see Model
	 * @param modelFile file that points to .OBJ-file.
	 * @return model represented by the <code> modelFile </code>.
	 * @throws FileNotFoundException
	 */
	public static Model loadModel(File modelFile) throws FileNotFoundException{
		BufferedReader in = new BufferedReader(new FileReader(modelFile));
		String line = null;
		Model model = new Model();
		try {
			while((line = in.readLine()) != null){
				if(line.startsWith("v ")){
					String[] components = line.split(" ");
					float x = Float.parseFloat(components[1]);
					float y = Float.parseFloat(components[2]);
					float z = Float.parseFloat(components[3]);
					model.addVertex(new Vector3f(x,y,z));
				}
				else if(line.startsWith("vn ")){
					String[] components = line.split(" ");
					float x = Float.parseFloat(components[1]);
					float y = Float.parseFloat(components[2]);
					float z = Float.parseFloat(components[3]);
					model.addNormal(new Vector3f(x,y,z));
				}
				else if(line.startsWith("f ")) {
					String[] components = line.split(" ");
					String[][] faceString = new String[][] {
							components[1].split("/"), 
							components[2].split("/"), 
							components[3].split("/")};
					Vector3f verticies = new Vector3f(Float.parseFloat(faceString[0][0]), Float.parseFloat(faceString[1][0]), Float.parseFloat(faceString[2][0]));
					Vector3f normals = new Vector3f(Float.parseFloat(faceString[0][2]), Float.parseFloat(faceString[1][2]), Float.parseFloat(faceString[2][2]));
					Face face = new Face(verticies, normals);
					model.addFace(face);
				}
				else if(line.startsWith("o ")) {
					//o 4_250_250_250
					String[] colorComponents = line.split(" ")[1].split("_");
					Vector3f color = new Vector3f(Float.parseFloat(colorComponents[1])/255.0f, Float.parseFloat(colorComponents[2])/255.0f, Float.parseFloat(colorComponents[3])/255.0f);
					model.newModelPart(color);
				}
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}

	private static float[] asFloatArray(Vector3f v){
		return new float[]{v.x, v.y, v.z};
	}
	/**
	 * 
	 * @param model
	 * @return
	 */
	public static int[] createVBO(Model model) {
		int vertexBufferHandle = glGenBuffers();
		int normalBufferHandle = glGenBuffers();

		FloatBuffer vertex = BufferUtils.createFloatBuffer(model.getFaces().size() * 9); //3 verticies * 3 points(x,y,z)
		FloatBuffer normal = BufferUtils.createFloatBuffer(model.getFaces().size() * 9);
		for(Face f:model.getFaces()){
			vertex.put(asFloatArray(model.getVerticies().get((int)(f.getVerticies().x-1))));
			vertex.put(asFloatArray(model.getVerticies().get((int)(f.getVerticies().y-1))));
			vertex.put(asFloatArray(model.getVerticies().get((int)(f.getVerticies().z-1))));
			
			normal.put(asFloatArray(model.getNormals().get((int)(f.getNormals().x-1))));
			normal.put(asFloatArray(model.getNormals().get((int)(f.getNormals().y-1))));
			normal.put(asFloatArray(model.getNormals().get((int)(f.getNormals().z-1))));
		}
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferHandle);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return new int[]{vertexBufferHandle, normalBufferHandle};
	}
}
