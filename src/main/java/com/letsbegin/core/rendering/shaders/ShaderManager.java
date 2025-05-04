package com.letsbegin.core.rendering.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public abstract class ShaderManager {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderManager() throws Exception {
        programID = GL20.glCreateProgram();
        if(programID == 0)
            throw new Exception("Could not create shader");
        System.out.println("Created shader program with ID: " + programID);
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = loadShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = loadShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public void link() throws Exception {

        bindAttributes();

        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader code: " + " Info " + GL20.glGetProgramInfoLog(programID, 1024));


        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0)
            throw new Exception("Unable to validate shader code: " + GL20.glGetProgramInfoLog(programID, 1024));

        if(vertexShaderID != 0)
            GL20.glDetachShader(programID, vertexShaderID);

        if(fragmentShaderID != 0)
            GL20.glDetachShader(programID, fragmentShaderID);

    }

    public abstract void bindAttributes();

    protected  void bindAttribute(int attribute, String variableName) {

        GL20.glBindAttribLocation(programID, attribute, variableName);

    }

    private int loadShader(String file, int type) throws Exception{


        int shaderID = GL20.glCreateShader(type);
        if (shaderID == 0)
            throw new Exception("Error creating shader. Type:" + type);

        GL20.glShaderSource(shaderID, file);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new Exception("Error compiling shader code. Type: " + type + "Info " + GL20.glGetShaderInfoLog(shaderID,1024));

        GL20.glAttachShader(programID, shaderID);

        return shaderID;

    }

    public static String loadResource(String filename) throws Exception {
        String result;
        try(InputStream in = ShaderManager.class.getResourceAsStream(filename);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result  = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        unbind();
        if (programID !=0)
            GL20.glDeleteProgram(programID);
    }

    public int getProgramID() {
        return programID;
    }

}
