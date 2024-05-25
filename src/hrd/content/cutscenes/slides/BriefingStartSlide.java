package hrd.content.cutscenes.slides;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import hrd.audio.*;
import hrd.graphics.*;
import hrd.ui.*;
import hrd.ui.cutscene.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class BriefingStartSlide extends CutsceneSlide {
    public BriefingStartSlide(String id, String next) {
        super(id, next);

        slideDuration = 7;
        canAuto = false;
    }

    @Override
    public void enter(CutsceneDialog d) {
        d.showOverlay(false);

        Timer.schedule(() -> Core.app.post(() -> {
            HRSounds.briefingStart.play();
        }), 90 / 60f);
    }

    public void drawContents(float time){

    }

    @Override
    public void drawBackground(float time) {
        drawContents(time);

        float f2 = Mathf.curve(time, 120, 130);
        float f3 = Mathf.curve(time, 110, 130);
        float f4 = Mathf.curve(time, 110, 175);

        float f5 = Mathf.curve(time, 150, 180);
        float f6 = Mathf.curve(time, 150, 260);
        float f7 = Mathf.curve(time, 280, 300);

        float f2i = Mathf.curve(time, 190, 220);
        float f3i = Mathf.curve(time, 330, 345);


        Draw.color(Tmp.c1.set(Color.black).lerp(Pal.darkestGray, 0.5f * f3), 1f - (f3i * 0.8f));
        Fill.rect(Core.graphics.getWidth() / 2f, Core.graphics.getHeight() / 2f, Core.graphics.getWidth(), Core.graphics.getHeight());

        if(time > 110) {
            float xo = 240f - (90f * Interp.pow3Out.apply(f4));
            Lines.stroke(2f - (2f * f3i), Pal.darkestGray);
            for (float i = 0; i < Core.graphics.getWidth() + xo; i += xo) {
                Lines.line(i - (time / (2f - 1.5f * Interp.pow3Out.apply(f3i)) % xo), 0, i - (time / 2f % xo), Core.graphics.getHeight());
            }

            for (float i = 0; i < Core.graphics.getHeight(); i += 240f - (90f * Interp.pow3Out.apply(f4))) {
                Lines.line(0, i, Core.graphics.getWidth(), i);
            }
        }

        Draw.color(Tmp.c1.set(Pal.darkerGray).lerp(Color.black, 0.5f * Interp.pow3Out.apply(f5)), Interp.pow3Out.apply(f2 - f2i));
        Draw.rect("human-resources-department-seventh-branch", Core.graphics.getWidth() / 2f, (Core.graphics.getHeight() / 2f) + 125, 500 * Interp.pow3Out.apply(f2), 500);
        Draw.color();

        HRDrawf.text(70, 75, Tmp.c1.set(Pal.gray).a(f3 -f3i), "T " + Time.nanos(), HRFonts.pixel);

        Lines.stroke(time > 110 && time < 346 ? 5 : 0, Tmp.c1.set(Pal.darkestGray).lerp(Pal.gray, f3 - f3i));

        Lines.rect(50f, 50f, Core.graphics.getWidth() - 100f, Mathf.lerp(50f,Core.graphics.getHeight() - 100f, Interp.pow3Out.apply(f3)) * Interp.pow3Out.apply(1 - f3i));

        Draw.color(Pal.darkerGray);
        Fill.rect(Core.graphics.getWidth() / 2f - (250 * (1 - Interp.pow3Out.apply(f5))), Core.graphics.getHeight() / 2f, 500 * Interp.pow3Out.apply(f5), 30 * Interp.pow3Out.apply(1 - f3i));

        Draw.color(time < 280 || (time > 283 && time < 286) || time > 289 ? Tmp.c1.set(Color.white).lerp(Pal.accent, f7) : Tmp.c1.set(Color.white).a(0));
        Fill.rect(Core.graphics.getWidth() / 2f - (250 * (1 - f6)), Core.graphics.getHeight() / 2f, 500 * Interp.pow3Out.apply(f5) * f6 , 30 * Interp.pow3Out.apply(1 - f3i));

        if(time < 260 || (time > 263 && time < 266) || time > 269){
            HRDrawf.text(Core.graphics.getWidth() / 2f - 250 + ((500 - 53) * Interp.pow3Out.apply(f7)), Core.graphics.getHeight() / 2f + 40, Tmp.c1.set(Color.white).a(f5 - f3i), " BRF-1", HRFonts.pixel);
            HRDrawf.text(Core.graphics.getWidth() / 2f - 250, Core.graphics.getHeight() / 2f + (40f * Interp.pow3Out.apply(f7)), Tmp.c1.set(Pal.darkerGray).lerp(Color.white, Interp.pow3Out.apply(f7)).a(f5 - f3i), time > 260 ? " // AUTHORIZED" : " // ACCESSING", HRFonts.pixel);
        }

        Draw.color(Tmp.c1.set(Pal.darkestGray).a(f7 > 0 ? 1 - f3i : 0));
        Draw.rect(Icon.lockOpen.getRegion(), Core.graphics.getWidth() / 2f - 220 + (450 * Interp.pow3Out.apply(f7)), Core.graphics.getHeight() / 2f, 20f, 20f);
    }
}
