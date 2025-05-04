package com.letsbegin.core.engine;

import com.letsbegin.core.game.Launcher;
import com.letsbegin.core.interfaces.EngineInterfaces;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;
import java.util.List;

public class EngineManager {

    public static final long NANOSECOND = 1000000000L;

    //render speed every second
    public static final float FRAMERATE = 144;

    //game update speed every second
    public static final float LOGIC_RATE = 60;

    private static int fps;
    private static int tps;
    private static float frametime = 1.0f / FRAMERATE;
    private static float logictime = 1.0f / LOGIC_RATE;

    private boolean isRunning;
    private GLFWErrorCallback errorCallback;
    private WindowManager window;
    private EngineInterfaces gameLogic;

    private void init() throws Exception {
        //set error callback
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));


        //initialize window
        window = new WindowManager("title", 800, 600, false);
        gameLogic = Launcher.getGame();

        // create display/window
        window.createDisplay();
        gameLogic.init();

    }

    //start engine
    public void start() throws Exception{

        //initializes
        init();
        //run if it's not already running
        if(isRunning) {
            return;
        }
        run();

    }

    public void run() {

        this.isRunning = true;
        //frame counter
        int frames = 0;
        //update counter
        int updates = 0;

        //explain later
        long frameCounter = 0;
        long updateCounter =0;

        //Sets the nanotime
        //runs once before the loop
        //used to measure time difference
        long lastTime = System.nanoTime();

        //explain later
        double unprocessedTime = 0;
        double unprocessedLogicTime = 0;


        while(isRunning) {

            //render if true
            boolean render = false;
            //update if true
            boolean update = false;
            //factor of time difference
            long startTime = System.nanoTime();
            //to get time difference
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            //adds "time debt"
            unprocessedTime += passedTime / (double)NANOSECOND;
            unprocessedLogicTime += passedTime / (double)NANOSECOND;

            //adds "time" for checking later
            updateCounter += passedTime;
            frameCounter += passedTime;

            //game update logic
            while(unprocessedLogicTime >= logictime) {

                //game logic goes here/
                update = true;

                //"resets" "debt"
                unprocessedLogicTime -= logictime;

                //if window should close
                if(window.windowShouldClose()) {

                    stop();
                    return;

                }

                //updates every second
                //show TPS
                if (updateCounter >= NANOSECOND) {

                    setTps(updates);
                    updates = 0;
                    updateCounter = 0;

                }

            }

            //render logic
            while(unprocessedTime >= frametime) {

                //update(frametime);
                render = true;
                unprocessedTime -= frametime;

                //fps counter
                if (frameCounter >= NANOSECOND) {

                    setFps(frames);
                    window.setTitle("title" + "  | FPS" + getFps() + " | TPS " + getTps());
                    frames = 0;
                    frameCounter = 0;

                }

            }

            if(render) {
                render();
                frames++;
            }

            if (update) {
                //game logic goes here//
                gameLogic.renderLogic();
                updates++;
            }
        }

        cleanup();

    }

    private void stop() {

        if(!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void input() {
        gameLogic.input();
    }

    private void render() {
        gameLogic.renderFrame();
        window.update();

    }

    private void cleanup() {
        window.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    //getters and setters

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

    public static int getTps() {
        return  tps;
    }

    public static void setTps(int tps) {
        EngineManager.tps = tps;
    }

}
