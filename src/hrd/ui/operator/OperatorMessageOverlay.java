package hrd.ui.operator;

import arc.Core;
import arc.Events;
import arc.flabel.FLabel;
import arc.flabel.FListener;
import arc.graphics.g2d.*;
import arc.math.Interp;
import arc.scene.*;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.*;
import arc.util.*;
import hrd.*;
import hrd.audio.HRSounds;
import hrd.operators.*;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.ui.*;


// TODO Sometimes it breaks UI noises as a whole, find out what the fuck's causing it and stop it
public class OperatorMessageOverlay{
    public static Table lastMessage;
    public static Operator lastOperator;
    public static Table messagePile, codec;
    public static void build(Group parent){
        parent.fill(t -> {
            t.clearChildren();

            t.margin(35f);
            t.bottom().left();

            t.table(w -> {
                w.bottom().left();
                messagePile = w;
            }).bottom().left().width(700f).padBottom(15f);
            t.row();
            t.table(Styles.black5, w -> {
                codec = w;
                w.margin(5f);
                w.label(() -> lastOperator == null ? "" : " " + Core.bundle.format("hrd.codec.connected", Core.bundle.get("hrd.operator." + lastOperator.name + ".name").toUpperCase())).left().grow();
                w.image(Icon.chat).size(25f).right();

                w.setTransform(true);
                w.actions(Actions.fadeOut(0f));
            }).bottom().left().width(700f).height(40f);

            Events.on(EventType.ResetEvent.class, e -> {
                messagePile.clearChildren();
                codec.actions(Actions.fadeOut(0f));
                lastOperator = null;
                lastMessage = null;

                codecShown = false;
            });
        });

        Events.on(HREvents.DialogMessage.class, e -> {
            if(!Vars.state.isGame()) return;
            Core.app.post(() -> HRSounds.message.play(1f));
            addMessage(e.sender, e.portrait, e.text, e.callback, e.lifetime);
        });
    }

    static boolean codecShown = false;

    public static void addMessage(Operator operator, TextureRegion portrait, String text, Runnable callback, float lifetime){
        messagePile.row();

        Table message = messagePile.table(t -> {
            t.bottom().left();
            t.table(Core.atlas.drawable("human-resources-department-portrait-background"),w -> {
                w.image(() -> portrait == null ? Core.atlas.find("human-resources-department-deafault-portrait") : portrait).size(30 * 3 + 15, 42 * 3 + 15);
            }).bottom().left().size(30 * 3 + 15, 42 * 3 + 15).name("Portrait");

            t.table(Styles.black5, w -> {
                w.top().left();
                w.margin(15f);
                FLabel l = w.add(new FLabel(text)).grow().get();
                l.setAlignment(Align.topLeft);
                l.setWrap(true);
                l.pause();

                l.setTypingListener(new FListener(){
                    @Override
                    public void onChar(char ch) {
                        FListener.super.onChar(ch);
                        Core.app.post(() -> HRSounds.type.play(1f));
                    }
                });

                w.setTransform(true);
                w.actions(Actions.sequence(
                        Actions.scaleTo(0, 1),
                        Actions.scaleTo(1, 1, 0.5f, Interp.pow3Out),
                        Actions.run(l::resume)
                ));
            }).padLeft(15f).bottom().left().growX().minHeight(42 * 3 + 15).expandY();
        }).bottom().left().width(700f).expandY().padTop(15f).get();

        if(!codecShown) {
            codec.actions(Actions.fadeIn(0.5f, Interp.pow3Out));
            Core.app.post(() -> HRSounds.codecOpen.play(1f));
            codecShown = true;
        }

        message.name = Core.graphics.getFrameId() + "";

        if(callback != null) callback.run();

        // remove previous message's portrait if operator is the same
        if(lastOperator != null && lastOperator.id == operator.id){
            (lastMessage.find("Portrait")).visible(() -> false);
        }

        // delete after lifetime
        message.setTransform(true);
        message.actions(Actions.sequence(
                Actions.delay(lifetime),
                Actions.parallel(
                        Actions.fadeOut(0.5f, Interp.pow3Out),
                        Actions.moveBy(-50, 0, 0.5f, Interp.pow3Out)
                ),
                Actions.run(() -> {
                    // if this message is last on the pile
                    if(lastMessage != null && lastMessage.name == message.name){
                        lastOperator = null;
                        lastMessage = null;
                        codec.actions(Actions.fadeOut(0.5f, Interp.pow3Out));
                        codecShown = false;
                    }
                    message.remove();
                })
        ));

        lastOperator = operator;
        lastMessage = message;
    }
}
