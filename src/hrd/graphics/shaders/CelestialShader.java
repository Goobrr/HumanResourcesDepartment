package hrd.graphics.shaders;

import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import mindustry.*;

public class CelestialShader extends Shader{
    public Vec3 light1 = new Vec3(), light2 = new Vec3();
    public Color color1 = new Color(), color2 = new Color();
    public Vec3 camPos = new Vec3();


    public CelestialShader(){
        super(Vars.tree.get("shaders/hrd/celestial.vert"), Vars.tree.get("shaders/hrd/celestial.frag"));
    }

    @Override
    public void apply() {
        setUniformf("u_camPos", camPos);

        setUniformf("pointLights[0].position", light1);
        setUniformf("pointLights[1].position", light2);

        setUniformf("pointLights[0].color", color1.r, color1.g, color1.b);
        setUniformf("pointLights[1].color", color2.r, color2.g, color2.b);
    };
}