package hrd.game.script;

import hrd.game.script.scripts.TutorialScript;

public class GameScripts {
    public static GameScript tutorial;
    public static void init(){
        tutorial = new TutorialScript("tutorial");
    }
}
