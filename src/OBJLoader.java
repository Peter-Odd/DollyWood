/**
 * @exclude
 */


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader 
{
	public static Model loadModel(File fileToLoad)
	{
		Model model = new Model();
		try 
		{
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(fileToLoad));
			String line;
			Color faceColor = Color.PINK;
			while((line = reader.readLine()) != null)
			{
				if(line.startsWith("v ")) //Space is needed to ensure vn and vt does not get caught in here
				{
					String[] component = line.split(" ");
					float x = Float.valueOf(component[1]);
					float y = Float.valueOf(component[2]);
					float z = Float.valueOf(component[3]);
					model.verticies.add(new Vector3f(x,y,z));
				}
				else if(line.startsWith("vn "))
				{
					String[] component = line.split(" ");
					float x = Float.valueOf(component[1]);
					float y = Float.valueOf(component[2]);
					float z = Float.valueOf(component[3]);
					model.normals.add(new Vector3f(x,y,z));
				}
				else if(line.startsWith("f "))
				{
					String[] faceIndex = line.split(" ");
					String[][] vertexIndex = new String[][]{faceIndex[1].split("/"), faceIndex[2].split("/"), faceIndex[3].split("/")};
					Vector3f vertexindices = new Vector3f(Integer.parseInt(vertexIndex[0][0]), Integer.parseInt(vertexIndex[1][0]), Integer.parseInt(vertexIndex[2][0]));
					Vector3f normalindices = new Vector3f(Integer.parseInt(vertexIndex[0][2]), Integer.parseInt(vertexIndex[1][2]), Integer.parseInt(vertexIndex[2][2]));
					
					model.faces.add(new Face(vertexindices, normalindices, faceColor));
				}
				else if(line.startsWith("o "))
				{
					String color = line.split(" ")[1];
					String colorComponents[] = color.split("_");//index_R_G_B
					faceColor = new Color(Integer.parseInt(colorComponents[1]),Integer.parseInt(colorComponents[2]),Integer.parseInt(colorComponents[3]));
				}
			}
			
			reader.close();
		} 
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}

    private static FloatBuffer reserveData(int size) 
    {
        return BufferUtils.createFloatBuffer(size);
    }
    
    private static float[] asFloatArray(Vector3f v) 
    {
        return new float[]{v.x, v.y, v.z};
    }
    
	public static int[] createVertexBuffer(Model model)
	{
        int vboVertexHandle = glGenBuffers();
        int vboNormalHandle = glGenBuffers();
        // TODO: Implement materials with VBOs
        FloatBuffer vertices = reserveData(model.getFaces().size() * 9); // No idea of why it is *9
        for (Face face : model.getFaces()) {
            vertices.put(asFloatArray(model.getVerticies().get((int)(face.vertex.x - 1))));
            vertices.put(asFloatArray(model.getVerticies().get((int)(face.vertex.y - 1))));
            vertices.put(asFloatArray(model.getVerticies().get((int)(face.vertex.z - 1))));
        }
        vertices.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return new int[]{vboVertexHandle, vboNormalHandle};
	}
}
