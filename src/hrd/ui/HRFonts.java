package hrd.ui;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.AssetManager;
import arc.files.Fi;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGeneratorLoader;
import arc.freetype.FreetypeFontLoader;
import arc.graphics.g2d.Font;
import arc.freetype.FreeTypeFontGenerator.*;
import arc.freetype.FreetypeFontLoader.*;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;

public class HRFonts {
    public static Font title, pixel;
    public static void load() {
        var fontSuffix = ".hrd.gen";
        Core.assets.setLoader(FreeTypeFontGenerator.class, ".hrd.gen", new FreeTypeFontGeneratorLoader(Vars.tree){
            @Override
            public FreeTypeFontGenerator load(AssetManager assetManager, String fileName, Fi file, FreeTypeFontGeneratorParameters parameter){
                return new FreeTypeFontGenerator(resolve(fileName.substring(0, fileName.length() - fontSuffix.length())));
            }
        });

        Core.assets.setLoader(Font.class, "-hrd", new FreetypeFontLoader(Vars.tree){
            @Override
            public Font loadSync(AssetManager manager, String fileName, Fi file, FreeTypeFontLoaderParameter parameter){
                if(parameter == null) throw new IllegalArgumentException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
                return manager
                        .get(parameter.fontFileName + fontSuffix, FreeTypeFontGenerator.class)
                        .generateFont(parameter.fontParameters);
            }

            @Override
            @SuppressWarnings("rawtypes")
            public Seq<AssetDescriptor> getDependencies(String fileName, Fi file, FreeTypeFontLoaderParameter parameter){
                return Seq.with(new AssetDescriptor<>(parameter.fontFileName + fontSuffix, FreeTypeFontGenerator.class));
            }
        });

        Core.assets.load("title-hrd", Font.class, new FreeTypeFontLoaderParameter("fonts/title.ttf", new FreeTypeFontParameter(){{
            size = 85;
        }})).loaded = f -> HRStyles.title.font = title = f;

        Core.assets.load("pixel-hrd", Font.class, new FreeTypeFontLoaderParameter("fonts/pixel.ttf", new FreeTypeFontParameter(){{
            size = 30;
        }})).loaded = f -> HRStyles.flat.font = HRStyles.pixel.font = pixel = f;
    }
}
