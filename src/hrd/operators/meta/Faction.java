package hrd.operators.meta;

// might be redundant
public class Faction{
    public String name;

    public static Faction seventhBranch;

    public Faction(String name){
        this.name = name;
    }
    public static void load(){
        // fallback operator faction
        seventhBranch = new Faction("seventh-branch");
    };
}
