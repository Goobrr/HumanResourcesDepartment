package hrd;

import arc.*;
import arc.util.*;
import hrd.audio.HRSoundControl;
import hrd.audio.HRSounds;
import hrd.content.logic.HRLStatements;
import hrd.operators.*;
import hrd.operators.meta.*;
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
            if(!Vars.headless){
                HRFonts.load();
                HRSounds.load();
//                HRModels.load();
//
//                Core.app.post(() -> {
//                    HRShaders.load();
//                });
            }
        });
    }

    @Override
    public void loadContent(){
        EntityRegistry.register();
        OperatorClass.load();
        Faction.load();
        Operators.load();
        HRLStatements.load();

        // HRPlanets.load();

        Events.on(ClientLoadEvent.class, e -> {
            HRStyles.load();
            HRUI.load();
            HRSoundControl.init();
        });

        Events.on(ResetEvent.class, e -> {
            Log.info("Reset Event Called");
            if(activeOperator != null){
                activeOperator.dialog.stop();
            }
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
                    activeOperator.dialog.start();
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
