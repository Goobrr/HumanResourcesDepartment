package hrd.ui;

import arc.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import hrd.ui.cutscene.*;
import hrd.ui.menu.*;
import hrd.ui.operator.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.fragments.MenuFragment.*;

public class HRUI{

    public static OperatorsDialog operators = new OperatorsDialog();
    public static CutsceneDialog cutsceneDialog = new CutsceneDialog();

    public static void load(){
        HRStyles.flat.over = HRStyles.flat.down = HRStyles.flat.up = Tex.whiteui; // woe
        HRStyles.flat.font = Fonts.def;

        // Operators Button
        Events.on(ResizeEvent.class, e -> {
            // Insert our button into the database submenu with reflection magic
            // By *replacing ALL of the menu buttons*
            // May break itself or other mods if another overrides it
            Table buttons = (Table) ((Table) Reflect.get(Vars.ui.menufrag, "container")).getChildren().get(0);

            buttons.clear();

            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, new MenuButton[]{
                new MenuButton(Core.bundle.get("play"), Icon.play, () -> {},
                    new MenuButton(Core.bundle.get("campaign"), Icon.play, () -> checkPlay(() -> {
                        if(true){
                            cutsceneDialog.show();
                        }else{
                            Vars.ui.planet.show();
                        }
                    })),
                    new MenuButton(Core.bundle.get("joingame"), Icon.add, () -> checkPlay(Vars.ui.join::show)),
                    new MenuButton(Core.bundle.get("customgame"), Icon.terrain, () -> checkPlay(Vars.ui.custom::show)),
                    new MenuButton(Core.bundle.get("loadgame"), Icon.download, () -> checkPlay(Vars.ui.load::show))
                ),
                new MenuButton(Core.bundle.get("database.button"), Icon.menu, () -> {},
                    new MenuButton(Core.bundle.get("schematics"), Icon.paste, () -> Vars.ui.schematics.show()),
                    new MenuButton(Core.bundle.get("hrd.operators"), Icon.players, () -> operators.show()),
                    new MenuButton(Core.bundle.get("database"), Icon.book, () -> Vars.ui.database.show()),
                    new MenuButton(Core.bundle.get("about.button"), Icon.info, () -> Vars.ui.about.show())
                ),
                new MenuButton(Core.bundle.get("editor"), Icon.terrain, () -> checkPlay(Vars.ui.maps::show)),
                (Vars.steam ? new MenuButton(Core.bundle.get("workshop"), Icon.steam, Vars.platform::openWorkshop) : null),
                new MenuButton(Core.bundle.get("mods"), Icon.book, Vars.ui.mods::show),
                new MenuButton(Core.bundle.get("settings"), Icon.settings, Vars.ui.settings::show)
            }}, Table.class, MenuButton[].class);

            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, ((Seq<MenuButton>) Reflect.get(Vars.ui.menufrag, "customButtons")).toArray(MenuButton.class)}, Table.class, MenuButton[].class);
            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, new MenuButton[]{new MenuButton(Core.bundle.get("quit"), Icon.exit, Core.app::exit)}}, Table.class, MenuButton[].class);

            buttons.pack();
        });

        Reflect.set(Vars.ui.menufrag, "renderer", new HRMenuRenderer());

        // Dialog overlay
        OperatorMessageOverlay.build(Vars.ui.hudGroup);
    }
    private static void checkPlay(Runnable run){
        if(!Vars.mods.hasContentErrors()){
            run.run();
        }else{
            Vars.ui.showInfo(Core.bundle.get("noerrorplay"));
        }
    }
}
