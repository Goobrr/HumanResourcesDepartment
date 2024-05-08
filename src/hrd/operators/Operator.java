package hrd.operators;

import arc.*;

public class Operator{
    public String name;

    /** Used as fallback if this operator lacks a title bundle entry **/
    public String title = "Operator";

    /** Used as fallback if this operator lacks a name bundle entry **/
    public String displayName;

    public int stars = 3;

    public boolean assigned = false;
    protected boolean unlocked = false;
    public boolean alwaysUnlocked = true;

    /** Sector this operator is currently assigned to, -1 if none **/
    public int sectorid = -1;
    public int id;

    private static int nextid = 0;

    public Operator(String name){
        this.name = name;

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
