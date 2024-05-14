package hrd.operators;

import arc.struct.*;
import arc.util.*;
import hrd.operators.dialog.dialogs.*;
import hrd.operators.meta.OperatorAbility;
import mindustry.*;
import mindustry.gen.*;

public class Operators{
    public static IntMap<Operator> all = new IntMap<>();

    public static Operator minako, rhyo;

    public static void load(){
        minako = new Operator("minako"){
            {
                alwaysUnlocked = true;
                stars = 5;
                maxExperience = 750;

                dialog = new MinakoDialogProvider();

                abilities(
                        new OperatorAbility("micromanage", () -> Icon.defense.getRegion(), o -> true),
                        new OperatorAbility("rush-hour", () -> Icon.defense.getRegion(), o -> false),
                        new OperatorAbility("hands-on", () -> Icon.defense.getRegion(), o -> false)
                );
            }
            @Override
            public void update(){
                Groups.build.each(b -> b.team() == Vars.player.team(), b -> {
                    if(b.block.canOverdrive){
                        b.applyBoost(1.2f, 1f);
                    }
                });
            }
        };
        rhyo = new Operator("rhyo"){
            {
                alwaysUnlocked = true;
                stars = 5;
                maxExperience = 600;
            }
        };

        for(Operator operator : all.values().toArray()){
            operator.load();
        }
    }

    public static Operator getByID(int id){
        return all.get(id);
    }
}
