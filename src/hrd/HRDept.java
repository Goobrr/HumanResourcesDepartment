package hrd;

import arc.*;
import arc.util.*;
import hrd.operators.*;
import hrd.ui.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import hrd.gen.*;
import mindustry.type.*;

public class HRDept extends Mod{
    @Nullable
    public static Operator activeOperator = null;

    public HRDept(){
        Events.on(FileTreeInitEvent.class, e -> {
            HRStyles.load();
            HRFonts.load();
        });
    }

    @Override
    public void loadContent(){
        // testing
        Core.settings.put("hrd-minako-experience", 200f);

        EntityRegistry.register();
        Operators.load();

        Operators.minako.assign(SectorPresets.groundZero.sector);

        Events.on(ClientLoadEvent.class, e -> {
            HRUI.load();
        });

        Events.on(ResetEvent.class, e -> {
            Log.info("Reset Event Called");
            activeOperator = null;
        });

        Events.run(Trigger.update, () -> {
            if(activeOperator != null){
                activeOperator.update();
            }
        });

        Events.on(WorldLoadEvent.class, e -> {
            Log.info("World Load Called");

            Sector sec = Vars.state.getSector();
            if(sec != null){
                Log.info("Entered Sector");

                // get operator assigned to sector
                int id = Core.settings.getInt(sec.planet.name + "-" + sec.id + "-operator", -1);
                if(id != -1){
                    activeOperator = Operators.getByID(id);
                    Log.info("Loaded Operator " + activeOperator.name);
                }else{
                    Log.info("No Active Operator for Sector");
                }
            }else{
                Log.info("Entered non-Sector");
                activeOperator = null;
            }
        });

    }
}
