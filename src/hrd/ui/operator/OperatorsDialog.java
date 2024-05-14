package hrd.ui.operator;

import arc.Core;
import arc.flabel.FLabel;
import arc.graphics.*;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import hrd.operators.*;
import hrd.operators.meta.OperatorAbility;
import hrd.ui.HRStyles;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class OperatorsDialog extends BaseDialog{
    ScrollPane roster;
    Table rosterTopRow, rosterBottomRow;
    Table operatorInfo, operatorRoster, operatorInfoTable, operatorNameTable, offshoot, operatorStats, sidebarDefault;
    int operatorCount;
    Operator selectedOperator;
    public OperatorsDialog(){
        super("Operators");
        addCloseButton();
        titleTable.clear();

        cont.setTransform(true);
        cont.margin(100f);
        cont.table(main -> {
            main.table(t -> {
                // operator name table offshoot
                t.top().left();
                offshoot = t.table(Tex.whiteui).top().right().height(140f).growX().get();
                offshoot.setTransform(true);
                offshoot.actions(Actions.parallel(
                        Actions.moveBy(30f, 0),
                        Actions.scaleTo(0, 1)
                ));
            }).top().left().width(30f).padTop(30f);

            main.table(Styles.black5, main2 -> {
                Table sideBackground = new Table(Styles.grayPanel, d -> {
                    // soon
                });

                Table sideStats = new Table(d -> {
                    d.top().left();

                    operatorNameTable = d.table(Table::left).top().left().padTop(30f).height(140).get();
                    operatorNameTable.setTransform(true);
                    operatorNameTable.background(Tex.whiteui);
                    operatorNameTable.actions(Actions.scaleTo(0, 1));

                    d.row();
                    d.table(w -> operatorStats = w).grow();
                    operatorStats.setTransform(true);
                });

                Table sideDefault = new Table(d -> {
                    sidebarDefault = d;

                    buildDefaultSidebar();
                });

                main2.stack(sideBackground, sideDefault, sideStats).width(700f).growY().left().get().setZIndex(1);

                operatorRoster = new Table(c -> {
                    c.top().left();
                    roster = c.pane(Styles.horizontalPane, t -> {
                        t.left();

                        rosterTopRow = t.table(r -> r.top().left()).height(450f).growX().top().left().padBottom(10f).get();
                        rosterTopRow.setClip(false);
                        t.row();
                        rosterBottomRow = t.table(r -> r.top().left()).height(450f).growX().top().left().get();
                        rosterBottomRow.setClip(false);
                        t.pack();

                    }).growX().height(950f).get();
                    roster.setZIndex(0);
                    roster.setScrollingDisabled(false, true);
                    roster.setFadeScrollBars(true);
                });
                operatorRoster.setTransform(true);

                operatorInfo = new Table(c -> {
                    operatorInfoTable = c;
                });
                operatorInfo.touchable(() -> Touchable.disabled);
                operatorInfo.setTransform(true);
                operatorInfo.actions(Actions.alpha(0));

                // TODO replace with fill()s
                main2.stack(operatorInfo, operatorRoster).left().grow();
            }).grow();
        }).grow();

        for(Operator operator : Operators.all.values().toArray()){
            addOperator(operator);
        }

        // Dummies for testing

        addOperator(new Operator("Theodore"));
        addOperator(new Operator("Wyoming"));
        addOperator(new Operator("Downwind"));
    }

    public void buildOperatorInfo(Operator operator, float transitionDuration){
        Table main = operatorInfoTable;
        main.clearChildren();
        main.top().left();

        main.button("Back", () -> {
            showOperator(null, 0.5f);
            showOperatorStats(null);
        });

        main.table(t -> {
            t.table(w -> {
                w.setTransform(true);
                w.image(Core.atlas.find("human-resources-department-manager-portrait-full")).size(1280f / 2, 1700f / 2).get();

                w.actions(Actions.parallel(
                        Actions.repeat(-1, Actions.sequence(
                            Actions.scaleTo(0, 1, 2f, Interp.sineIn),
                            Actions.scaleTo(-1, 1, 2f, Interp.sineOut),
                            Actions.scaleTo(0, 1, 2f, Interp.sineIn),
                            Actions.scaleTo(1, 1, 2f, Interp.sineOut)
                        )),
                        Actions.repeat(-1, Actions.sequence(
                                Actions.moveBy(1280 / 2f, 0f, 4f, Interp.sine),
                                Actions.moveBy(-1280 / 2f, 0f, 4f, Interp.sine)
                        ))
                ));
            }).expand();
        }).grow();

        main.table(Styles.grayPanel, m -> {
            m.fill(t -> {
                t.pane(p -> {
                    p.top().left();
                    p.margin(30f);
                    for(OperatorAbility ability : operator.abilities){
                        if(ability.unlockCondition.get(operator)) {
                            p.table(ability::build).growX().height(250f).padBottom(15f);
                            p.row();
                        }else{
                            p.table(Styles.black5, c -> {
                                c.table(w -> {
                                    w.image(Icon.lock).size(30f);
                                    w.row();
                                    w.label(() -> Core.bundle.get("hrd.locked")).style(HRStyles.pixel).expand().get().setColor(Pal.darkerGray);
                                }).center();
                            }).growX().height(250f).padBottom(15f).tooltip(Core.bundle.get("hrd.ability." + ability.name + ".condition"));
                            p.row();
                        }
                    }
                }).top().left().grow();;
            });

            m.fill(t -> {

            });
        }).top().right().growY().width(700f);
    }

    boolean nameTableShown = false;
    Operator lastOperator;

    public void showOperatorStats(Operator operator){
        // Name Table
        if(operator == null){
            if(nameTableShown) {
                operatorNameTable.actions(Actions.scaleTo(0, 1, 0.5f, Interp.pow3Out));
                offshoot.actions(Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.parallel(
                                Actions.moveBy(30f, 0, 0.1f, Interp.pow3Out),
                                Actions.scaleTo(0, 1, 0.1f, Interp.pow3Out)
                        )
                ));
                operatorStats.clearChildren();
                buildDefaultSidebar();
                nameTableShown = false;
            }
            return;
        };
        operatorNameTable.clearChildren();

        operatorNameTable.margin(15f, 30f, 15f, 30f);
        operatorNameTable.center().left();

        Table text = new Table(t -> {
            FLabel label = t.add(new FLabel(Core.bundle.get("hrd.operator." + operator.name + ".name").toUpperCase())).color(Pal.darkerGray).top().left().height(50).padTop(30f).get();
            label.setStyle(HRStyles.title);

            if(lastOperator != null && lastOperator.id == operator.id){
                label.skipToTheEnd();
            }else{
                label.pause();
                if (!nameTableShown) {
                    operatorNameTable.actions(Actions.delay(0.4f, Actions.run(label::restart)));
                } else {
                    label.restart();
                }
            }

            t.row();
            t.table(r -> {
                r.label(() ->
                        Core.bundle.get("hrd.operator." + operator.name + ".title").toUpperCase()
                ).top().left().color(Pal.darkerGray).get().setStyle(HRStyles.pixel);;
                r.label(() ->
                        "// " + Core.bundle.get("hrd.operator." + operator.name + ".serial").toUpperCase()
                ).top().left().padLeft(15f).color(Pal.lightishGray).get().setStyle(HRStyles.pixel);
            }).top().left();
        }).left();

        operatorNameTable.add(text).padLeft(45f).left();

        if(!nameTableShown) {
            offshoot.actions(Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(-30f, 0, 0.1f, Interp.pow3Out),
                            Actions.scaleTo(1, 1, 0.1f, Interp.pow3Out)
                    )
            ));
            operatorNameTable.actions(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.scaleTo(1, 1, 0.5f, Interp.pow3Out)
            ));
            nameTableShown = true;
        }

        // Operator Stats
        buildStats(operator);
        sidebarDefault.clearChildren();

        lastOperator = operator;

    }

    public void buildDefaultSidebar(){
        sidebarDefault.top().left();
        sidebarDefault.margin(35f);

        sidebarDefault.label(() -> Core.bundle.get("hrd.operators").toUpperCase()).style(HRStyles.title).height(80f).top().left().growX().get().setAlignment(Align.left);
    }

    public void buildStats(Operator operator){
        operatorStats.clearChildren();
        operatorStats.top().left();
        operatorStats.margin(35f);
        operatorStats.table(t -> {
            t.table(Styles.black5, d -> {
                Label l = d.label(() -> operator.level + "").grow().center().get();
                l.setStyle(HRStyles.pixel);
                l.setAlignment(Align.center);
            }).size(50f);
            t.table(Tex.whiteui, d -> {
                Table bar = new Table(Tex.whiteui);
                bar.setColor(Pal.accent);
                bar.setTransform(true);
                bar.actions(Actions.scaleTo(operator.getProgress(), 1));

                Label label = new Label(() -> "  " + operator.getExperience() + "/" + operator.getMaxExperience());
                label.setStyle(HRStyles.pixel);
                label.setColor(Pal.gray);

                d.stack(bar, label).grow().left();
            }).size(450f, 50f).padLeft(15f).color(Color.black);
        }).top().left().growX().padBottom(30f);
        operatorStats.row();
        operatorStats.table(t -> {
            t.top().left();
            t.table(Tex.whiteui).size(30f).left().padRight(15f);
            t.label(() -> Core.bundle.get("hrd.profile")).left().get().setStyle(HRStyles.pixel);
        }).top().left().growX().height(40f).padBottom(25f);
        operatorStats.row();
        operatorStats.pane(Styles.defaultPane, t -> {
            Label label = t.label(() -> Core.bundle.get("hrd.operator." + operator.name + ".profile")).top().left().growX().expandY().get();
            label.setAlignment(Align.topLeft);
            label.setWrap(true);
        }).top().left().growX().maxHeight(400f).padBottom(30f);
        operatorStats.row();
        operatorStats.table(t -> {
            t.top().left();
            t.table(Tex.whiteui).size(30f).left().padRight(15f);
            t.label(() -> Core.bundle.get("hrd.status")).left().height(30f).get().setStyle(HRStyles.pixel);
        }).top().left().growX().height(40f).padBottom(25f);
        operatorStats.row();
        operatorStats.table(t -> {
            t.table(Tex.whiteui, d -> {
                Label l = d.label(() -> {
                    if(operator.assigned) return "STATIONED";
                    return "IDLE";
                }).grow().left().padLeft(15f).get();
                l.setStyle(HRStyles.pixel);
                l.setAlignment(Align.left);
                l.setColor(operator.assigned ? Pal.darkerGray : Color.white);

                if(operator.sectorid != -1){
                    l = d.label(() -> {
                        return operator.planet == null ? "" : operator.planet.sectors.get(operator.sectorid).name();
                    }).grow().right().padRight(15f).get();
                    l.setStyle(HRStyles.pixel);
                    l.setAlignment(Align.right);
                    l.setColor(operator.assigned ? Pal.darkerGray : Color.white);
                }

                d.setColor(Pal.techBlue);
                if(operator.assigned) d.setColor(Pal.accent);
            }).height(50f).growX();
            t.row();
            t.table(Styles.black, d -> {
                d.margin(15f);
                d.top().left();

                Label label = d.label(() -> Core.bundle.format(operator.assigned ? "hrd.operator-assigned" : "hrd.operator-idle", Core.bundle.get("hrd.operator." + operator.name + ".name"), operator.sectorid, operator.planet == null ? "" : operator.planet.localizedName)).grow().get();
                label.setAlignment(Align.topLeft);
                label.setWrap(true);

                d.table(f -> {
                    f.button(operator.assigned ? Core.bundle.get("hrd.reassign") : Core.bundle.get("hrd.assign"), HRStyles.flat, () -> {
                        Vars.ui.planet.showSelect(operator.sectorid == -1 ? SectorPresets.groundZero.sector : operator.planet.sectors.get(operator.sectorid), s -> {
                            operator.assign(s);
                            buildStats(operator);
                        });
                    }).color(Pal.gray).top().left().growX().height(50f);

                    if(operator.assigned){
                        f.row();
                        f.button(Core.bundle.get("hrd.unassign"), HRStyles.flat, () -> {
                            operator.assign(null);
                            buildStats(operator);
                        }).color(Pal.gray).top().left().growX().height(50f).padTop(15f);
                    };
                }).top().right().width(150);

            }).expandY().growX().top().left();
        }).top().left().growX().padBottom(30f);
    }

    public void showOperator(Operator operator, float duration){
        cont.actions(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(0.98f, 0.98f, duration, Interp.pow3),
                        Actions.moveBy(cont.getWidth() * 0.02f * 0.5f, cont.getHeight() * 0.02f * 0.5f, duration, Interp.pow3)
                ),
                Actions.parallel(
                        Actions.scaleTo(1f, 1f, duration, Interp.pow3),
                        Actions.moveBy(-cont.getWidth() * 0.02f * 0.5f, -cont.getHeight() * 0.02f * 0.5f, duration, Interp.pow3)
                )
        ));
        if(operator == null){
            selectedOperator = null;

            operatorInfo.touchable(() -> Touchable.disabled);
            operatorInfo.actions(Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(100, 0, duration, Interp.pow3In),
                            Actions.fadeOut(duration, Interp.pow3In)
                    ),
                    Actions.moveBy(-100, 0)
            ));

            operatorRoster.actions(Actions.sequence(
                    Actions.delay(duration),
                    Actions.moveBy(100, 0),
                    Actions.parallel(
                            Actions.moveBy(-100, 0, duration, Interp.pow3Out),
                            Actions.fadeIn(duration, Interp.pow3Out)
                    ),
                    Actions.run(() -> operatorRoster.touchable(() -> Touchable.enabled))
            ));
        }else{
            buildOperatorInfo(operator, duration);
            selectedOperator = operator;

            operatorRoster.touchable(() -> Touchable.disabled);
            operatorRoster.actions(Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(100, 0, duration, Interp.pow3In),
                            Actions.fadeOut(duration, Interp.pow3In)
                    ),
                    Actions.moveBy(-100, 0)
            ));

            operatorInfo.actions(Actions.sequence(
                    Actions.delay(duration),
                    Actions.moveBy(100, 0),
                    Actions.parallel(
                            Actions.moveBy(-100, 0, duration, Interp.pow3Out),
                            Actions.fadeIn(duration, Interp.pow3Out)
                    ),
                    Actions.run(() -> {
                        operatorInfo.touchable(() -> Touchable.enabled);
                    })
            ));
        }
    }

    public void addOperator(Operator operator){
        Table row = operatorCount % 2 == 0 ? rosterTopRow : rosterBottomRow;
        Table card = new OperatorCard(operator, o -> showOperator(o, 0.5f), o -> showOperatorStats(o));
        row.add(card).pad(10f);

        operatorCount++;
    }
}
