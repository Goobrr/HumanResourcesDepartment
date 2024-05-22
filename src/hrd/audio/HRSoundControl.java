package hrd.audio;

import arc.*;
import arc.audio.*;
import arc.math.*;
import arc.util.*;
import hrd.ui.HRUI;
import mindustry.audio.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class HRSoundControl extends SoundControl {

    public static void init(){
        control.sound = new HRSoundControl();
    }

    public void doSilence(){
        silence();
    }
    @Override
    public void update() {
        boolean paused = state.isGame() && Core.scene.hasDialog();
        boolean playing = state.isGame();

        //check if current track is finished
        if(current != null && !current.isPlaying()){
            current = null;
            fade = 0f;
        }

        //fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
        if(timer.get(1, 30f)){
            Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
        }

        //play/stop ordinary effects
        if(playing != wasPlaying){
            wasPlaying = playing;

            if(playing){
                Core.audio.soundBus.play();
                setupFilters();
            }else{
                //stopping a single audio bus stops everything else, yay!
                Core.audio.soundBus.stop();
                //play music bus again, as it was stopped above
                Core.audio.musicBus.play();

                Core.audio.soundBus.play();
            }
        }

        Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());

        // skip automatic playing behavior when cutscene UI is shown
        if(HRUI.cutsceneDialog.isShown()){
            updateLoops();
            return;
        }

        if(state.isMenu()){
            silenced = false;
            if(ui.planet.isShown()){
                play(ui.planet.state.planet.launchMusic);
            }else if(ui.editor.isShown()){
                play(Musics.editor);
            }else{
                play(Musics.menu);
            }
        }else if(state.rules.editor){
            silenced = false;
            play(Musics.editor);
        }else{
            //this just fades out the last track to make way for ingame music
            silence();

            //play music at intervals
            if(Time.timeSinceMillis(lastPlayed) > 1000 * musicInterval / 60f){
                //chance to play it per interval
                if(Mathf.chance(musicChance)){
                    lastPlayed = Time.millis();
                    playRandom();
                }
            }
        }

        updateLoops();
    }
}
