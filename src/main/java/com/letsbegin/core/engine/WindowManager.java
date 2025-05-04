package com.letsbegin.core.engine;

import com.letsbegin.core.interfaces.EngineInterfaces;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

//Handles the creation of the window and stuff
public class WindowManager {

    private final String title;

    private int width, height;
    private long window;

    private boolean resize, vSync;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void createDisplay() {

        GLFWErrorCallback.createPrint(System.err).set();

        //checks if glfw initialized
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();

        //find out what this does
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);

        //makes window resizeable
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

        //gets major and minor version
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);

        //enables core profile??
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        //enables forward compat
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        // if width and height 0, automaximize
        boolean maximized = false;
        if(width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        //creates the window
        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        //checks if the window works
        if(window == MemoryUtil.NULL) {

            throw new RuntimeException("GLFW failed to create a window");

        }

        //makes "window" the active window
        GLFW.glfwMakeContextCurrent(window);

        //Load OpenGl functions
        GL.createCapabilities();

        //Calls when window is being resized
        GLFW.glfwSetFramebufferSizeCallback(window,(window, width, height) -> {

            //sets width and height to current w&h
            this.width = width;
            this.height = height;
            this.setResize(true);

        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });


        //maximizes window
        if(maximized) {

            GLFW.glfwMaximizeWindow(window);

        } else {

            //gets the primary monitor
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            //sets the position of "window"
            /*gets the posX and y pos by getting the size of the monitor
              subtracting window size, then dividing by 2*/
            GLFW.glfwSetWindowPos(window,(vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);

        }

        if(isvSync())
            GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);

    }

    public void resizer () {
        GL11.glViewport(0, 0, getWidth(), getHeight());
        setResize(false);
    }

    public void update() {

        resizer();
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();

    }

    public void cleanup() {

        GLFW.glfwDestroyWindow(window);
    }

    //getters and setters

    public boolean windowShouldClose() {

        return  GLFW.glfwWindowShouldClose(window);

    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isvSync() {

        return vSync;

    }

    public void setResize(boolean resize) {

        this.resize = resize;

    }

    public boolean isResize() {
        return this.resize;
    }

    public void setTitle(String title) {

        GLFW.glfwSetWindowTitle(window, title);

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }


}