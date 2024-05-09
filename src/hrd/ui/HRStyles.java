package hrd.ui;

import arc.graphics.Color;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.ui.*;

public class HRStyles {
    public static Label.LabelStyle title, pixel;
    public static TextButtonStyle flat;

    public static void load(){
        title = new Label.LabelStyle(){{
            fontColor = Color.white;
            font = HRFonts.title;
        }};

        pixel = new Label.LabelStyle(){{
            fontColor = Color.white;
            font = HRFonts.pixel;
        }};

        flat = new TextButtonStyle(){{
            over = Tex.whiteui;
            font = Fonts.def;
            fontColor = Color.white;
            down = Tex.whiteui;
            up = Tex.whiteui;
        }};
    }
}
