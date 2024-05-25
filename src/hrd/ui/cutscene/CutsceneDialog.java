package hrd.ui.cutscene;

import arc.*;
import arc.flabel.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Timer.*;
import hrd.audio.HRSounds;
import hrd.operators.*;
import hrd.ui.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class CutsceneDialog extends BaseDialog{
    public ObjectMap<String, CutsceneActor> actors = new ObjectMap<>();
    public Table cutsceneStage, background, stage, overlay, skip;
    public Label nameLabel;
    public FLabel textLabel;

    public CutsceneSequence sequence;

    public boolean auto = true;
    public float autoWaitTime = 5f;
    public Task autoTask;

    float slideTime;

    public float cameraX = 0;
    public CutsceneDialog(){
        super("Cutscene");
        titleTable.clear();
        setStyle(new DialogStyle());
        setBackground(Styles.black);

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

            t.add(new Table(){
                @Override
                public void draw(){
                    if(sequence != null && sequence.current != null){
                        sequence.current.drawBackground(slideTime);
                    }
                }
            }).grow();
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
            t.table(Styles.black, w -> {
                w.margin(20f);
                w.top().left();

                // Dialogue text
                w.table(c -> {
                    c.top().left();
                    c.margin(20f, 300f, 20f, 300f);
                    c.name = "Text";

                    c.table(d -> {
                        d.top().right();
                        d.name = "Name";
                        nameLabel = d.label(() -> "Name").top().right().grow().get();
                        nameLabel.setColor(Pal.darkishGray);
                        nameLabel.setAlignment(Align.topRight);
                    }).growY().width(500f).top().left().padRight(20f);

                    textLabel = c.add(new FLabel("Text")).top().left().grow().get();

                    textLabel.setAlignment(Align.topLeft);
                }).grow();
                w.row();

                // Controls
                w.table(c -> {
                    c.name = "Controls";
                    c.bottom().right();

                    c.table(x -> {
                        skip = x;
                        x.touchable(() -> Touchable.enabled);
                        x.center();
                        x.setBackground(Tex.whiteui);
                        x.setColor(Pal.darkestGray);

                        x.margin(10f);
                        x.image(Icon.distribution).size(30f).padRight(10f).touchable(Touchable.disabled).color(Color.white);
                        x.label(() -> Core.bundle.get("hrd.cutscene.auto")).style(HRStyles.pixel).touchable(Touchable.disabled).color(Color.white).growX();

                        x.hovered(() -> x.setColor(auto ? Pal.gray : Pal.darkestGray));
                        x.exited(() -> x.setColor(auto ? Pal.darkishGray : Pal.darkestGray));
                        x.clicked(() -> {
                            auto = !auto;
                            setColor(auto ? Pal.darkishGray : Pal.darkestGray);
                        });
                    }).size(300f, 50f).bottom().right();

                    c.table().growX();

                    c.table(x -> {
                        skip = x;
                        x.touchable(() -> Touchable.enabled);
                        x.center();
                        x.setBackground(Tex.whiteui);
                        x.setColor(Pal.darkishGray);

                        x.margin(10f);
                        x.label(() -> Core.bundle.get("hrd.cutscene.next")).style(HRStyles.pixel).touchable(Touchable.disabled).color(Color.white).growX();
                        x.image(Icon.right).size(30f).padLeft(10f).touchable(Touchable.disabled).color(Color.white);

                        x.hovered(() -> x.setColor(Pal.gray));
                        x.exited(() -> x.setColor(Pal.darkishGray));
                        x.clicked(this::next);
                    }).size(300f, 50f).bottom().left();
                }).growX().height(50f).bottom().left();
            }).bottom().growX().height(300f);
        });

        Events.run(Trigger.update, () -> {
            if(!isShown()) return;

            slideTime += Time.delta;

            if(sequence != null && sequence.current != null && sequence.current.slideDuration != -1){
                if(slideTime >= sequence.current.slideDuration * 60){
                    next();
                }
            }
        });
    }

    // SLIDE HANDLING
    public Dialog show(CutsceneSequence sequence){
        this.sequence = sequence;

        setSlide(this.sequence.start());

        return show();
    }

    public void setSlide(CutsceneSlide slide){
        Log.info(slide.id);

        skip.visible(() -> false);
        skip.touchable(() -> Touchable.disabled);
        slide.enter(this);

        slideTime = 0;
    }

    public void next(){
        if(autoTask != null){
            autoTask.cancel();
            autoTask = null;
        }
        if(sequence == null){
            hide();
            return;
        }

        sequence.current.exit(this);

        CutsceneSlide next = sequence.next();
        if(next == null){
            hide();
            return;
        }

        setSlide(next);
    }

    // CUTSCENE UTILITIES

    public void lockInput(boolean b){
        HRUI.inputLock = b;
    }

    public void setOverlayBackground(Drawable bg){
        overlay.setBackground(bg);
    }

    public void showBackground(boolean shown){
        setBackground(shown ? Styles.black : Styles.none);
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

    public void setSpeaker(String name){
        nameLabel.setText(() -> name);
    }

    public void setDialogueSpeech(String text){
        setDialogueSpeech(text, CutsceneDialog::slideDone);
    }

    public void setDialogueSpeech(String text, Cons<CutsceneDialog> finishedSpeaking){
        textLabel.restart(text);
        textLabel.setTypingListener(new FListener(){
            @Override
            public void end(){
                finishedSpeaking.get(CutsceneDialog.this);
            }

            @Override
            public void onChar(char ch) {
                FListener.super.onChar(ch);

                Core.app.post(() -> HRSounds.type.play());
            }
        });
    }

    public void slideDone(){
        if(sequence.current.slideDuration == -1){
            skip.visible(() -> true);
            skip.touchable(() -> Touchable.enabled);

            if(auto && sequence.current.canAuto){
                autoTask = Timer.schedule(this::next, autoWaitTime);
            }
        }
    }
}
