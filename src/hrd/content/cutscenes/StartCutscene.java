package hrd.content.cutscenes;

import hrd.audio.*;
import hrd.ui.cutscene.*;
import mindustry.Vars;

public class StartCutscene extends CutsceneSequence {

    public StartCutscene(){
        super(
                new CutsceneSlide("st-1", "st-2"){{
                    slideDuration = 4f;
                    canAuto = false;
                }
                    @Override
                    public void enter(CutsceneDialog d) {
                        d.showOverlay(false);
                    }
                }
        );
    }

    @Override
    public CutsceneSlide start() {
        ((HRSoundControl) Vars.control.sound).doSilence();

        return super.start();
    }
}
