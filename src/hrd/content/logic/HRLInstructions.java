package hrd.content.logic;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import hrd.HREvents;
import hrd.operators.Operator;
import hrd.operators.Operators;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.LInstruction;

public class HRLInstructions {
    public static void init(){

    }
    public static class CodecMessageI implements LInstruction {
        public int opr = -1, region, message, lifetime;

        public CodecMessageI(int operatorName, int region, int message, int lifetime){
            this.opr = operatorName;
            this.message = message;
            this.lifetime = lifetime;
            this.region = region;
        }

        @Override
        public void run(LExecutor exec) {
            LExecutor.Var reg = exec.var(region);
            TextureRegion portrait;
            if(reg.isobj && region != 0){
                portrait = Core.atlas.find(LExecutor.PrintI.toString(reg.objval));
            }else{
                portrait = Core.atlas.find("human-resources-department-" + (opr != 1 ? Operators.getByID(opr).name + "-portrait-bust" : "default-portrait"));
            }


            if(opr != -1) Events.fire(new HREvents.DialogMessage(Operators.getByID(opr), portrait, LExecutor.PrintI.toString(exec.var(message).objval), (float) exec.var(lifetime).numval));
        }
    }
}
