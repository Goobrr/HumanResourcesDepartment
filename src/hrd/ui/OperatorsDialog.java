package hrd.ui;

import arc.*;
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
    int operatorCount;
    public OperatorsDialog(){
        super("Operators");
        addCloseButton();
        titleTable.clear();

        cont.margin(100f);
        cont.table(main -> {
            main.table(Styles.grayPanel, d -> {

            }).width(700f).growY().left().get().setZIndex(1);
            main.table(Styles.black5, c -> {
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
            }).left().grow();
        }).grow();

        for(Operator operator : Operators.all.values().toArray()){
            addOperator(operator);
        }

        // Dummies for testing

        addOperator(new Operator("Theodore"));
        addOperator(new Operator("Wyoming"));
        addOperator(new Operator("Downwind"));
    }

    public void addOperator(Operator operator){
        Table row = operatorCount % 2 == 0 ? rosterTopRow : rosterBottomRow;
        Table card = new OperatorCard(operator, o -> Log.info(o.name));
        row.add(card).pad(10f);

        operatorCount++;
    }
}
