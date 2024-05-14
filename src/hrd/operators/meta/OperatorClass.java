package hrd.operators.meta;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

// operator archetype classification, used for filtering, otherwise purely visual.
public class OperatorClass {
    public String name;
    public TextureRegion region, regionSmall;

    public static OperatorClass operator, support, defender, vanguard;

    public OperatorClass(String name){
        this.name = name;
        this.region = Core.atlas.find("human-resources-department-" + name);
        this.regionSmall = Core.atlas.find("human-resources-department-" + name + "-small");
    }
    public static void load(){
        // fallback operator class
        operator = new OperatorClass("operator");

        support = new OperatorClass("support");
        defender = new OperatorClass("defender");
        vanguard = new OperatorClass("vanguard");
    };
}
