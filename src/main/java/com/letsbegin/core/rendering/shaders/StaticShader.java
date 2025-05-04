package com.letsbegin.core.rendering.shaders;

public class StaticShader extends ShaderManager {

    public StaticShader() throws Exception {
        super();
    }

    @Override
    public void bindAttributes() {
        System.out.println("Binding attribute 'position' to location 0");
        super.bindAttribute(0, "position");
    }

    // Add this after shader link
    public void getAllUniformLocations() {
        System.out.println("Getting uniform locations");
    }
}
