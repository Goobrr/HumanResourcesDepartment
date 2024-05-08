package hrd.ui;

import arc.Core;
import arc.func.Cons;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Log;
import hrd.operators.Operator;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;

public class OperatorCard extends Table {

    public OperatorCard(Operator operator, Cons<Operator> callback) {
        top().left();

        background(Tex.button);

        // Text overlay
        Table overlay = new Table(t -> {
            t.top().left();
            t.margin(5f);

            // Name & Title
            t.table(n -> {
                n.image(Icon.modeSurvival).size(30f).left();

                n.table(m -> {
                    m.top().left();
                    m.label(() -> operator.name).top().left();
                    m.row();
                    m.label(() -> operator.title).color(Pal.gray).top().left();
                }).padLeft(5f).left().growX();
            }).top().left().growX();

            // Stars
            t.row();
            t.table(s -> {
                TextureRegion star = Core.atlas.find("human-resources-department-star" + (operator.stars >= 5 ? "-blue" : ""));
                for(int i = 0; i < operator.stars; i++){
                    s.image(star).padBottom(1f);
                    s.row();
                }
            }).top().left().padTop(10f);
        });

        // Character portrait
        Table portrait = new Table(t -> {
            TextureRegion p = Core.atlas.find("human-resources-department-" + operator.name + "-portrait-bust", "human-resources-department-default-portrait");

            t.background(new TextureRegionDrawable(p));
        });

        stack(portrait, overlay).size(300f, 420f);

        // Click and hover behavior, similar to buttons.
        touchable(() -> Touchable.enabled);
        setTransform(true);

        align(Align.center);
        setZIndex(50);

        addListener(new ClickListener(){
            boolean entered = false;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Element fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if(entered) return;
                entered = true;

                background(Tex.buttonDown);

                setZIndex(50);
                actions(Actions.parallel(
                        Actions.scaleTo(1.05f, 1.05f, 0.3f, Interp.pow3Out),
                        Actions.moveBy(-15f / 2f, -21f / 2f, 0.3f, Interp.pow3Out)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Element toActor) {
                super.exit(event, x, y, pointer, toActor);
                if(!entered) return;
                entered = false;

                background(Tex.button);

                setZIndex(51);
                actions(Actions.parallel(
                        Actions.scaleTo(1.0f, 1.0f, 0.3f, Interp.pow3Out),
                        Actions.moveBy(15f / 2f, 21f / 2f, 0.3f, Interp.pow3Out)
                ));
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.get(operator);
            }
        });
    }
}
