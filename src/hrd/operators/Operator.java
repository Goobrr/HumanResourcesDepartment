package hrd.operators;

import arc.*;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import hrd.operators.dialog.*;
import hrd.operators.dialog.dialogs.*;
import hrd.operators.meta.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.type.*;

public class Operator{
    public String name;

    /** Used as fallback if this operator lacks a title bundle entry **/
    public String title = "Operator";

    /** Used as fallback if this operator lacks a name bundle entry **/
    public String displayName;

    public int stars = 3;

    // Meta
    public OperatorClass operatorClass = OperatorClass.operator;

    public Faction faction = Faction.seventhBranch;

    public Seq<OperatorAbility> abilities = new Seq<>();

    // Internal
    public boolean assigned = false;
    protected boolean unlocked = false;
    public boolean alwaysUnlocked = false;

    public DialogProvider dialog;

    public int level = 1;
    protected float experience = 0;

    /* Amount of experience needed to level up */
    protected float maxExperience = 600;

    /* max XP scaling for each level */
    protected float maxExperienceScaling = 1.15f;

    /** Sector this operator is currently assigned to, -1 if none **/
    public int sectorid = -1;
    public Planet planet;
    public int id;

    private static int nextid = 0;

    public Operator(String name){
        this.name = name;
        this.dialog = new DefaultDialogProvider();

        Operators.all.put(nextid, this);

        id = nextid;
        nextid++;
    }

    public void abilities(OperatorAbility... abilities){
        this.abilities.set(abilities);
    }

    public void update(){

    }

    public void addXP(float xp){
        this.experience += xp;
        if(this.experience >= maxExperience){
            this.experience = this.experience - maxExperience;
            maxExperience = Mathf.ceil(maxExperience * maxExperienceScaling);
        }
    }

    public float getProgress(){
        return experience / maxExperience;
    }

    public float getExperience(){
        return experience;
    }

    public float getMaxExperience(){
        return maxExperience;
    }

    public boolean isUnlocked(){
        return Core.settings.getBool("hrd-" + name + "-unlocked", alwaysUnlocked);
    }

    public boolean assign(Sector sector){
        // fail if a different operator is already assigned to this sector
        // TODO fail if already assigned to sector?
        if(sector != null && Core.settings.getInt(sector.planet.name + "-" + sector.id + "-operator", id) != id){
            return false;
        }

        // remove previous assignment
        if(sectorid != -1){
            Core.settings.remove(planet.name + "-" + sectorid + "-operator");
        }

        // unassign if assigned to null
        if(sector == null){
            Core.settings.put("hrd-" + name + "-sector", -1);
            Core.settings.put("hrd-" + name + "-planet", "");
            sectorid = -1;
            planet = null;
            assigned = false;
            return true;
        }

        // assign to sector
        Core.settings.put("hrd-" + name + "-sector", sector.id);
        Core.settings.put("hrd-" + name + "-planet", sector.planet.name);
        sectorid = sector.id;
        planet = sector.planet;
        assigned = true;

        Core.settings.put(planet.name + "-" + sectorid + "-operator", id);
        return true;
    }

    protected void load(){
        this.planet = Vars.content.getByName(ContentType.planet, Core.settings.getString("hrd-" + name + "-planet", ""));
        this.sectorid = Core.settings.getInt("hrd-" + name + "-sector", -1);
        if(sectorid != -1){
            assigned = true;
        }

        this.experience = Core.settings.getFloat("hrd-" + name + "-experience", 0);
        this.level = Core.settings.getInt("hrd-" + name + "-level", 1);

        maxExperience = Mathf.ceil(maxExperience * Mathf.pow(maxExperienceScaling, this.level - 1));

        if(dialog != null) dialog.operator = this;

        Log.info("Loaded " + name);
    }
}
