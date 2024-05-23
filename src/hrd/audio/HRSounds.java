package hrd.audio;

import arc.audio.Sound;
import mindustry.Vars;

public class HRSounds{
    public static Sound codecOpen, message, type, briefingStart;
    public static void load(){
        codecOpen = Vars.tree.loadSound("ui/codec");
        message = Vars.tree.loadSound("ui/codecmessage");
        type = Vars.tree.loadSound("ui/codectype");
        briefingStart = Vars.tree.loadSound("cutscene/briefingstart");
    }
}
