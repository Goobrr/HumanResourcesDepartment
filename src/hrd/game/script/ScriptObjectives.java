package hrd.game.script;

import arc.func.Prov;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import arc.util.io.*;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.type.Item;

// similar to MapObjectives, except it works for the HRD script system
public class ScriptObjectives {
    private static ObjectMap<String, Prov<? extends ScriptObjective>> classmap = new ObjectMap<>();
    @SafeVarargs
    public static void register(Prov<? extends ScriptObjective>... providers){
        for(var prov : providers) {
            classmap.put(prov.get().getClass().getSimpleName(), prov);
        }
    }

    public static ScriptObjective get(String classname){
        return classmap.get(classname).get();
    }

    public static void load(){
        register(
                GatherItemObjective::new
        );
    }

    public static class ScriptObjective {
        public boolean finished;
        public boolean update() {
            finished = get();
            return finished;
        }

        public boolean get(){
            return false;
        }

        public String text(){
            return "";
        }

        public String progressText(){
            return "";
        }

        public void write(Writes write) {
        }

        public void read(Reads read) {
        }
    }

    public static class GatherItemObjective extends ScriptObjective{
        public Item item = Items.copper;
        public int startAmount = 0, gatherAmount = 1;
        public GatherItemObjective(){}

        public GatherItemObjective(Item item, int startAmount, int gatherAmount){
            this.item = item;
            this.startAmount = startAmount;
            this.gatherAmount = gatherAmount;
        }

        @Override
        public boolean get(){
            return Vars.state.rules.defaultTeam.items().get(item) - startAmount >= gatherAmount;
        }

        @Override
        public String text() {
            return "Gather [accent]" + gatherAmount + "[] " + item.emoji() + "[accent]" + item.localizedName + "[]";
        }

        @Override
        public String progressText() {
            return Math.min(Vars.state.rules.defaultTeam.items().get(item) - startAmount, gatherAmount) + "/" + gatherAmount;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(item.id);
            write.i(startAmount);
            write.i(gatherAmount);
        }

        @Override
        public void read(Reads read) {
            super.read(read);
            item = Vars.content.item(read.i());
            startAmount = read.i();
            gatherAmount = read.i();
        }
    }
}
