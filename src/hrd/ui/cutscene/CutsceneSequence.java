package hrd.ui.cutscene;

import arc.struct.*;

public class CutsceneSequence{
    public ObjectMap<String, CutsceneSlide> slides = new ObjectMap<>();

    public CutsceneSlide startingSlide, current;
    public CutsceneSequence(CutsceneSlide... slides){
        for(CutsceneSlide slide : slides){
            this.slides.put(slide.id, slide);
        }

        this.startingSlide = slides[0];
    }

    public CutsceneSlide start(){
        current = startingSlide;
        return current;
    }

    public CutsceneSlide next(){
        current = slides.get(current.next);
        return current;
    }

    public CutsceneSlide jumpTo(String slide){
        current = slides.get(slide);
        return current;
    }
}
