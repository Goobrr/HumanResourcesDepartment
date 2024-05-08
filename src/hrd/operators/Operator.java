package hrd.operators;

import arc.*;

public class Operator{
    public String name;
    public String displayName;
    public String title = "Operator";
    public int stars = 3;
    public boolean assigned = false;
    protected boolean unlocked = false;
    public boolean alwaysUnlocked = true;
    public int sectorid = -1;
    public int id;

    private static int nextid = 0;

    public Operator(String name){
        this.name = name;
        this.displayName = Core.bundle.get(this.name + ".displayname", "");

        load();

        Operators.all.put(nextid, this);

        id = nextid;
        nextid++;
    }

    public void update(){

    }

    public boolean isUnlocked(){
        return alwaysUnlocked || unlocked;
    }

    protected void load(){
        this.assigned = Core.settings.getBool(name + "-assigned", false);
        this.sectorid = Core.settings.getInt(name + "-sector", -1);
    }
}
