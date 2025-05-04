package com.letsbegin.core.game;

import com.letsbegin.core.engine.Loader;
import com.letsbegin.core.engine.WindowManager;
import com.letsbegin.core.game.controls.MouseInput;
import com.letsbegin.core.interfaces.EngineInterfaces;
import com.letsbegin.core.rendering.RenderManager;
import com.letsbegin.core.rendering.models.RawModel;
import com.letsbegin.core.rendering.shaders.StaticShader;

public class TestGame implements EngineInterfaces {

    private final WindowManager window;
    private final RenderManager renderer;
    private final Loader loader;
    private StaticShader shader;

    private RawModel model;

    public TestGame() throws Exception {

        window = Launcher.getWindow();
        renderer = new RenderManager();
        loader = new Loader();


    }


    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        model = loader.loadToVAO(vertices, indices);

    }

    @Override
    public void input() {

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

    }

    @Override
    public void renderLogic() {

    }

    @Override
    public void renderFrame() {

        renderer.clear();
        renderer.render(model);

    }

    @Override
    public void cleanUp() {
        renderer.cleanup();
        loader.cleanUp();

    }
}
