package hrd.content.cutscenes;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.util.Align;
import arc.util.Timer;
import arc.util.Tmp;
import hrd.HREvents;
import hrd.content.cutscenes.slides.*;
import hrd.graphics.HRDrawf;
import hrd.operators.Operators;
import hrd.ui.HRFonts;
import hrd.ui.cutscene.*;
import mindustry.*;

public class StartCutscene extends CutsceneSequence {

    public StartCutscene() {
        super(
                new BriefingStartSlide("st-1", "st-2") {
                    {
                        slideDuration = 11;
                    }

                    @Override
                    public void enter(CutsceneDialog d) {
                        super.enter(d);
                        Vars.ui.hudfrag.shown = false;
                        Vars.ui.consolefrag.hide();

                        d.showBackground(false);
                        d.lockInput(true);
                    }

                    @Override
                    public void drawContents(float time) {
                        Draw.color(Color.black, 1f - 0.25f * Mathf.curve(time, 60f * 7f, 60f * 9f));
                        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());

                        HRDrawf.text(100, Core.graphics.getHeight() - 100, Color.white, "SHARDED CORPS OS(R)", HRFonts.pixel);
                        HRDrawf.text(100, Core.graphics.getHeight() - 140, Color.white, "AUTHORIZED AS: BFR-1", HRFonts.pixel);

                        Tmp.c1.set(Color.white).a(1 - Mathf.curve(time, 60f * 9f, 60f * 11f));

                        float w1 = 0;
                        if (time > 60f * 6f)
                            HRDrawf.text(100, Core.graphics.getHeight() - 220, Tmp.c1, "DONE - LANDING SEQUENCE", HRFonts.pixel);
                        if (time > 60f * 6.5f)
                            HRDrawf.text(100, Core.graphics.getHeight() - 260, Tmp.c1, "DONE - PERSONNEL INIT", HRFonts.pixel);
                        if (time > 60f * 8f)
                            w1 = HRDrawf.text(100, Core.graphics.getHeight() - 300, Tmp.c1, "PERSONNEL ONBOARD: ", HRFonts.pixel);
                        if (time > 60f * 9.1f)
                            HRDrawf.text(100 + w1 + 40, Core.graphics.getHeight() - 300, Tmp.c1, "MNK2871 - OP. MINAKO", HRFonts.pixel);
                        if (time > 60f * 9.2f)
                            HRDrawf.text(100 + w1 + 40, Core.graphics.getHeight() - 340, Tmp.c1, "0000000 - MNGR.", HRFonts.pixel);
                        Draw.color();
                    }
                },
                new CutsceneSlide("st-2", "st-3"){
                    @Override
                    public void enter(CutsceneDialog d) {
                        super.enter(d);

                        d.setSpeaker("???");
                        d.setDialogueSpeech("Oh! You're finally awake. I've been waiting for a while.");

                        d.textLabel.pause();
                        Timer.schedule(() -> {
                            d.showOverlay(true);
                            d.textLabel.resume();
                        }, 1f);
                    }

                    @Override
                    public void drawBackground(float time) {
                        Draw.color(Color.black, 0.75f);
                        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());
                    }
                },
                new CutsceneSlide("st-3", "st-4"){
                    @Override
                    public void enter(CutsceneDialog d) {
                        super.enter(d);

                        d.setDialogueSpeech("We've been on cruise for quite some time, so i figured you get some rest while i prep your equipment.");
                    }
                    @Override
                    public void drawBackground(float time) {
                        Draw.color(Color.black, 0.75f);
                        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());
                    }
                },
                new CutsceneSlide("st-4", "st-5"){
                    @Override
                    public void enter(CutsceneDialog d) {
                        super.enter(d);

                        d.setSpeaker("???");
                        d.setDialogueSpeech("Speaking of, your coreship should be ready to launch by now.");
                    }
                    @Override
                    public void drawBackground(float time) {
                        Draw.color(Color.black, 0.75f);
                        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());
                    }
                },
                new CutsceneSlide("st-5", "st-6"){
                    @Override
                    public void enter(CutsceneDialog d) {
                        super.enter(d);

                        d.showOverlay(false);
                        canAuto = false;
                        slideDuration = 10;

                        Timer.schedule(() -> {
                            Vars.control.resume();
                        }, 1.5f);
                    }
                    @Override
                    public void drawBackground(float time) {
                        Draw.color(Color.black, 0.75f - 0.75f * Mathf.curve(time, 0, 90));
                        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());
                    }
                }
        );
    }

    public static void message(String message){
        Events.fire(new HREvents.DialogMessage(Operators.minako, Core.atlas.find("human-resources-department-minako-portrait-bust"), message, 10f));
    }

    @Override
    public CutsceneSlide start() {
        Vars.control.sound.stop();

        return super.start();
    }
}
