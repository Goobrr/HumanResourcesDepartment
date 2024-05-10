package hrd;

import arc.audio.Sound;
import mindustry.Vars;

public class HRSounds{
    public static Sound codecOpen, message, type;
    public static void load(){
        codecOpen = Vars.tree.loadSound("ui/codec");
        message = Vars.tree.loadSound("ui/codecmessage");
        type = Vars.tree.loadSound("ui/codectype");
    }
}
