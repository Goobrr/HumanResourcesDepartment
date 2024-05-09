package hrd.operators;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

public class OperatorClass {
    public String name;
    public TextureRegion region, regionSmall;

    public static OperatorClass operator, support, defender, vanguard;

    public OperatorClass(String name){
        this.name = name;
        this.region = Core.atlas.find(name);
        this.regionSmall = Core.atlas.find(name + "-small");
    }
    public static void load(){
        // fallback operator class
        operator = new OperatorClass("operator");

        support = new OperatorClass("support");
        defender = new OperatorClass("defender");
        vanguard = new OperatorClass("vanguard");
    };
}
