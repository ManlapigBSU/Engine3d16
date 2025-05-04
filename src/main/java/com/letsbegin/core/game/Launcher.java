package com.letsbegin.core.game;

import com.letsbegin.core.engine.EngineManager;
import com.letsbegin.core.engine.WindowManager;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

public class Launcher {

    private static WindowManager window;
    private static TestGame game;
    private static EngineManager engine;


    public static void main(String[] args) throws Exception {
        String version = GLFW.glfwGetVersionString();
        System.out.println("GLFW Version" + version);
        System.out.println(Version.getVersion());
        window = new WindowManager("test", 0, 0, false);
        engine = new EngineManager();
        game = new TestGame();
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}