package com.letsbegin.core.engine;
import com.letsbegin.core.rendering.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, int[] indices) {

        //debug
        System.out.println("Creating VAO with " + positions.length/3 + " vertices and " + indices.length/3 + " triangles");

        // Print vertex positions
        for(int i = 0; i < positions.length; i += 3) {
            System.out.println("Vertex " + (i/3) + ": (" + positions[i] + ", " + positions[i+1] + ", " + positions[i+2] + ")");
        }

        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions);
        unbindVAO();

        System.out.println("VAO created with ID: " + vaoID);
        return new RawModel(vaoID, indices.length);

    }

    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null){
                System.out.println("Image file \"" + filename + "\"not found. Using placeholder texture");
                return createCheckerBoardTexture();

            }


            width = w.get();
            height = h.get();
        }

        int textureID = GL11.glGenTextures();
        textures.add(textureID);
        return textureID;
    }

    public int createCheckerBoardTexture() {

        // Create a simple procedural checkerboard texture
        int width = 16;
        int height = 16;
        int squareSize = 4;

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isPurple = ((x / squareSize) + (y / squareSize)) % 2 == 0;

                if (isPurple) {
                    buffer.put((byte) 128).put((byte) 0).put((byte) 128).put((byte) 255); // Purple (R:128, G:0, B:128, A:255)
                } else {
                    buffer.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 255); // Black (R:0, G:0, B:0, A:255)
                }
            }
        }
        buffer.flip();

        int textureID = GL11.glGenTextures();
        textures.add(textureID);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Generate mipmaps
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return textureID;
    }

    //vao creation
    private int createVAO() {

        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;

    }

    //create VBO
    private void storeDataInAttributeList(int attributeNumber, float[] data){

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        //converts data to float
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    //create IBO
    private void bindIndicesBuffer(int[] indices) {

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;

    }

    private void unbindVAO() {

        GL30.glBindVertexArray(0);

    }

    public void cleanUp() {
        for (int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            GL30.glDeleteBuffers(vbo);
    }
}
