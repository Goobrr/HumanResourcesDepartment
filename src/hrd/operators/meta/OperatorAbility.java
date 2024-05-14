package hrd.operators.meta;

import arc.Core;
import arc.func.Boolf;
import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import hrd.operators.Operator;
import hrd.ui.HRStyles;
import mindustry.ui.Styles;

// Operator ability entry used in the operator info UI, purely visual.
public class OperatorAbility {
    public String name;
    public Prov<TextureRegion> icon;

    public Boolf<Operator> unlockCondition;
    public OperatorAbility(String name, Boolf<Operator> unlockCondition){
        this.name = name;
        this.icon = () -> Core.atlas.find("human-resources-department-" + name + "-ability-icon");
        this.unlockCondition = unlockCondition;
    }

    public OperatorAbility(String name, Prov<TextureRegion> icon, Boolf<Operator> unlockCondition){
        this.name = name;
        this.icon = icon;
        this.unlockCondition = unlockCondition;
    }

    public void build(Table container){
        container.setBackground(Styles.black3);

        container.margin(15f);
        container.top().left();

        container.table(t -> {
            t.image(icon).size(30f).padRight(20f).left();
            t.label(() -> Core.bundle.get("hrd.ability." + name + ".name")).left().growX().style(HRStyles.pixel);
        }).top().left().height(40f).growX();

        container.row();

        container.table(Styles.black5, t -> {
            t.margin(10f);
            Label l = t.label(() -> Core.bundle.get("hrd.ability." + name + ".description")).top().left().grow().get();
            l.setAlignment(Align.topLeft);
            l.setWrap(true);
        }).top().left().grow().padTop(15f);
    }
}
