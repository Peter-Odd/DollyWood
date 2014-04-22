package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {
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
					model.getVerticies().add(new Vector3f(x,y,z));
				}
				else if(line.startsWith("vn ")){
					String[] components = line.split(" ");
					float x = Float.parseFloat(components[1]);
					float y = Float.parseFloat(components[2]);
					float z = Float.parseFloat(components[3]);
					model.getNormals().add(new Vector3f(x,y,z));
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
					model.getFaces().add(face);
				}
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
}
