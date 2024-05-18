package hrd.ui.cutscene;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import hrd.operators.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class CutsceneDialog extends BaseDialog{
    public ObjectMap<String, CutsceneActor> actors = new ObjectMap<>();
    public Table cutsceneStage, background, stage, overlay;

    public float cameraX = 0;
    public CutsceneDialog(){
        super("Cutscene");
        titleTable.clear();
        setBackground(Styles.black8);

        cutsceneStage = new Table(){
            @Override
            public void draw(){
                Draw.trans(this.computeTransform());
                Draw.flush();

                for(CutsceneActor actor : actors.values().toSeq()){
                    actor.draw(0.5f * Core.graphics.getWidth(), Scl.scl() * 300);
                }

                Draw.flush();
                Draw.trans(CutsceneDialog.this.computeTransform());
            }
        };
        cutsceneStage.setTransform(true);

        cont.fill(t -> {
            background = t;
            t.name = "Background";
        });

        cont.fill(t -> {
            stage = t;
            t.name = "Stage";
            t.table(w -> w.add(cutsceneStage)).grow().get().setLayoutEnabled(false);
        });

        cont.fill(t -> {
            overlay = t;
            t.name = "Overlay";
            t.bottom().left();
            t.table(Tex.button, w -> {

            }).bottom().growX().height(300f);
        });
    }

    public void setCutsceneBackground(Drawable bg){
        background.setBackground(bg);
    }

    public void setOverlayBackground(Drawable bg){
        overlay.setBackground(bg);
    }

    public void showBackground(boolean shown){
        background.visible(() -> shown);
        setBackground(shown ? Styles.black8 : Styles.none);
    }

    public void showStage(boolean shown){
        stage.visible(() -> shown);
    }

    public void showOverlay(boolean shown){
        overlay.visible(() -> shown);
    }

    public void panCameraTo(float x){
        cameraX = x;
        cutsceneStage.actions(Actions.moveTo(x, 0, 0.5f, Interp.pow3Out));
    }

    public void removeActor(Operator operator){
        CutsceneActor actor = actors.get(operator.name);
        if(actor != null){
            actor.proxy.remove();
            actors.remove(operator.name);
        }
    }

    public void clearActors(){
        for(CutsceneActor actor : actors.values().toSeq()){
            actor.proxy.remove();
        }
        actors.clear();
    }

    public void addActor(Operator... operators){
        for(Operator operator : operators){
            Table proxy = cutsceneStage.table().get();
            proxy.name = operator.name;
            proxy.setColor(Color.white);
            CutsceneActor actor = new CutsceneActor(operator, proxy);
            actors.put(operator.name, actor);
        }
    }
}
