package hrd;

import arc.Core;
import arc.graphics.g2d.*;
import hrd.operators.*;

public class HREvents{
    public static class DialogMessage{
        public final Operator sender;
        public final TextureRegion portrait;
        public final String text;
        public final Runnable callback;

        public final float lifetime;

        public DialogMessage(Operator sender, String text){
            this.sender = sender;
            this.text = text;
            this.portrait = Core.atlas.find("human-resources-department-" + sender.name + "-portrait-bust");
            this.callback = () -> {};
            this.lifetime = 10f;
        }

        public DialogMessage(Operator sender, String text, float lifetime){
            this.sender = sender;
            this.text = text;
            this.portrait = Core.atlas.find("human-resources-department-" + sender.name + "-portrait-bust");
            this.callback = () -> {};
            this.lifetime = lifetime;
        }

        public DialogMessage(Operator sender, TextureRegion portrait, String text, float lifetime){
            this.sender = sender;
            this.text = text;
            this.portrait = portrait;
            this.callback = () -> {};
            this.lifetime = lifetime;
        }
        public DialogMessage(Operator sender, TextureRegion portrait, String text, Runnable callback, float lifetime){
            this.sender = sender;
            this.text = text;
            this.portrait = portrait;
            this.callback = callback;
            this.lifetime = lifetime;
        }
    }
}
