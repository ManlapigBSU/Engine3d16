package com.letsbegin.core.interfaces;
import com.letsbegin.core.game.controls.MouseInput;

public interface EngineInterfaces {

    void init() throws Exception;

    void input();

    void update(float interval, MouseInput mouseInput);

    void renderLogic();

    void renderFrame();

    void cleanUp();
}
