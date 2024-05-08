package hrd.operators;

import arc.struct.*;
import arc.util.*;
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
            }
        };
    }

    public static Operator getByID(int id){
        return all.get(id);
    }
}
