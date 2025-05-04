package com.letsbegin.core.rendering;

import com.letsbegin.core.engine.WindowManager;
import com.letsbegin.core.game.Launcher;
import com.letsbegin.core.rendering.models.RawModel;
import com.letsbegin.core.rendering.shaders.ShaderManager;
import com.letsbegin.core.rendering.shaders.StaticShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {

    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {

        shader = new StaticShader(); // Create an instance of BasicShader

        String vertexSource = ShaderManager.loadResource("/shaders/vertex.vs");
        String fragmentSource = ShaderManager.loadResource("/shaders/fragment.fs");

        System.out.println("Vertex shader source:" + vertexSource);
        System.out.println("Fragment shader source:" + fragmentSource);

        shader.createVertexShader(vertexSource);
        shader.createFragmentShader(fragmentSource);
        shader.link();

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //debug
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        System.out.println("Shader program linked successfully");

    }

    public void render(RawModel model) {

        //debug
        int error = GL11.glGetError();
        if(error != GL11.GL_NO_ERROR) {
            System.out.println("OpenGL error before rendering: " + error);
        }
        shader.bind();

        GL30.glBindVertexArray(model.getVaoID());
        // Enable attribute 0 (position)
        GL20.glEnableVertexAttribArray(0);
        // Draw elements
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        // Disable attribute 0
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        shader.unbind();
        //debug
        int error1 = GL11.glGetError();
        if(error1 != GL11.GL_NO_ERROR) {
            System.out.println("OpenGL error during rendering: " + error1);
        }

    }

    public void cleanup() {
        shader.cleanUp();
    }

    public void clear() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 1.0f);

    }

    public ShaderManager getShader() {
        return shader;
    }
}
