package hrd.ui.cutscene;

import arc.func.*;

public class CutsceneSlide{
    public String id;
    public String next;

    public Cons<CutsceneDialog> onEnter, onExit;

    public float slideDuration = -1;

    public CutsceneSlide(String id, String next, Cons<CutsceneDialog> enter){
        this.id = id;
        this.next = next;
        this.onEnter = enter;
        this.onExit = d -> {};
    }
    public CutsceneSlide(String id, String next, Cons<CutsceneDialog> enter, Cons<CutsceneDialog> exit){
        this.id = id;
        this.next = next;
        this.onEnter = enter;
        this.onExit = exit;
    }

    public void enter(CutsceneDialog d){
        onEnter.get(d);
    }

    public void exit(CutsceneDialog d){
        onExit.get(d);
    }
}
