package hrd.content;

import arc.struct.StringMap;
import mindustry.Vars;
import mindustry.maps.Map;
import mindustry.maps.Maps;

public class HRMaps {
    public static Map tutorial;
    public static void load(){
        tutorial = Vars.maps.loadInternalMap("human-resources-department-tutorial");
    }
}
