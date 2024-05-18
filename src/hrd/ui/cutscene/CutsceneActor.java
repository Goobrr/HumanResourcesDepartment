package hrd.ui.cutscene;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import hrd.operators.*;
import mindustry.graphics.*;

public class CutsceneActor{
    public TextureRegion region;
    public Operator operator;

    public float width = 128f * 5f;
    public float height = 170f * 5f;

    public Table proxy;
    public boolean highlighted = true;

    public CutsceneActor(Operator operator, Table proxy){
        this.proxy = proxy;
        this.operator = operator;
        this.region = Core.atlas.find("human-resources-department-" + operator.name + "-portrait-full", "human-resources-department-manager-portrait-full");

        proxy.setTransform(true);
    };

    public void draw(float x, float y){
        Draw.z(highlighted ? 10 : 8);
        Draw.color(proxy.color);
        Draw.rect(region, x, y + Scl.scl(height) / 2, Scl.scl(width), Scl.scl(height));

//        Lines.stroke(1f, Pal.accent);
//        Lines.rect(x - Scl.scl(width) / 2, y, Scl.scl(width), Scl.scl(height));
    }

    public void highlight(boolean highlighted){
        this.highlighted = highlighted;
        proxy.actions(Actions.color(highlighted ? Color.white : Pal.darkerGray, 0.2f, Interp.pow3Out));
    }
}
