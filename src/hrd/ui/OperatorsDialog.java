package hrd.ui;

import arc.*;
import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.Log;
import hrd.operators.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class OperatorsDialog extends BaseDialog{
    ScrollPane roster;
    Table rosterTopRow, rosterBottomRow;
    Table operatorInfo, operatorRoster, operatorInfoTable;
    int operatorCount;
    Operator selectedOperator;
    public OperatorsDialog(){
        super("Operators");
        addCloseButton();
        titleTable.clear();

        cont.setTransform(true);
        cont.margin(100f);
        cont.table(Styles.black5, main -> {
            main.table(Styles.grayPanel, d -> {

            }).width(700f).growY().left().get().setZIndex(1);

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

            main.stack(operatorInfo, operatorRoster).left().grow();
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
        Table t = operatorInfoTable;
        t.clearChildren();
        t.label(() -> operator.name).top().left();
        t.button("Back", () -> showOperator(null, 0.5f));
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
        Table card = new OperatorCard(operator, o -> showOperator(o, 0.5f));
        row.add(card).pad(10f);

        operatorCount++;
    }
}
