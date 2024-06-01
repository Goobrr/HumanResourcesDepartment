package hrd.ui.script;

import arc.Core;
import arc.graphics.Color;
import arc.math.Interp;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.StringMap;
import arc.util.Align;
import hrd.ui.HRStyles;
import hrd.ui.HRUI;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class ScriptObjectiveOverlay {
    public static ObjectMap<String, Table> addedObjectives = new ObjectMap<>();

    // build on UI.hudGroup
    public static void build(Group parent){
        Table m = parent.find("overlaymarker");
        m.row();
        m.table(cont -> {
            cont.top().left();
            cont.margin(15f);
            cont.table(Styles.black6, t -> {
                t.margin(15f);
                t.top().left();

                t.label(() -> Core.bundle.get("hrd.objective")).style(HRStyles.pixel).size(40f).top().left().get().setAlignment(Align.left);
                t.row();

                // Might need to make the GameScript class work closer with this overlay
                // TODO don't rely on checking every frame
                t.table(l -> {
                    l.update(() -> {
                        l.top().left();
                        if(HRUI.activeGameScript != null){
                            HRUI.activeGameScript.objectives.each((k, v) -> {
                                if(!addedObjectives.containsKey(k) && !v.finished) {
                                    l.table(Styles.grayPanel, c -> {
                                        addedObjectives.put(k, c);
                                        c.setTransform(true);
                                        c.name = k;

                                        c.top().left();
                                        c.margin(15f);

                                        c.label(v::text).left();
                                        c.table().growX();
                                        c.label(v::progressText).right();
                                    }).height(50f).growX().padBottom(5f);
                                    l.row();

                                    cont.actions(Actions.fadeIn(0.2f));
                                }
                            });

                            addedObjectives.each((k, v) -> {
                                if(!HRUI.activeGameScript.objectives.containsKey(k)){
                                    v.actions(Actions.delay(0.5f), Actions.parallel(
                                            Actions.fadeOut(0.5f),
                                            Actions.moveBy(-200f, 0f, 0.5f, Interp.pow3In)
                                    ), Actions.run(() -> {
                                        v.remove();
                                        addedObjectives.remove(k);

                                        l.invalidate();
                                        if(addedObjectives.isEmpty()){
                                            cont.actions(Actions.fadeOut(0.2f));
                                        }
                                    }));
                                }
                            });


                        }
                    });

                }).growX().expandY().top().left();
            }).width(500f).expandY();

            cont.setTransform(true);
            cont.actions(Actions.fadeOut(0f));
        }).top().left().visible(() -> Vars.ui.hudfrag.shown && HRUI.activeGameScript != null).colspan(2);
    }
}
