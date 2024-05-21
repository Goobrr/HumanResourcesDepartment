package hrd.graphics.shaders;

import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import mindustry.*;

public class FlatShader extends Shader{
    public Color color = new Color();
    public Vec3 camPos = new Vec3();

    public FlatShader(){
        super(Vars.tree.get("shaders/hrd/flat.vert"), Vars.tree.get("shaders/hrd/flat.frag"));
    }

    @Override
    public void apply() {
        setUniformf("u_camPos", camPos);
        setUniformf("u_color", color.r, color.g, color.b);
    };
}