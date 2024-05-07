package hrd.ui;

import arc.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.ui.fragments.MenuFragment.*;

public class HRUI{

    public static OperatorsDialog operators = new OperatorsDialog();

    public static void load(){
        // Operators Button
        Events.on(ResizeEvent.class, e -> {
            // Insert our button into the database submenu button with reflection magic
            // By *replacing ALL of the menu buttons*
            // May break itself or other mods if another overrides it
            Table buttons = (Table) ((Table) Reflect.get(Vars.ui.menufrag, "container")).getChildren().get(0);

            buttons.clear();

            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, new MenuButton[]{
                new MenuButton(Core.bundle.get("play"), Icon.play, () -> {},
                    new MenuButton(Core.bundle.get("campaign"), Icon.play, () -> checkPlay(Vars.ui.planet::show)),
                    new MenuButton(Core.bundle.get("joingame"), Icon.add, () -> checkPlay(Vars.ui.join::show)),
                    new MenuButton(Core.bundle.get("customgame"), Icon.terrain, () -> checkPlay(Vars.ui.custom::show)),
                    new MenuButton(Core.bundle.get("loadgame"), Icon.download, () -> checkPlay(Vars.ui.load::show))
                ),
                new MenuButton(Core.bundle.get("database.button"), Icon.menu, () -> {},
                    new MenuButton(Core.bundle.get("schematics"), Icon.paste, () -> Vars.ui.schematics.show()),
                    new MenuButton(Core.bundle.get("operators"), Icon.players, () -> operators.show()),
                    new MenuButton(Core.bundle.get("database"), Icon.book, () -> Vars.ui.database.show()),
                    new MenuButton(Core.bundle.get("about.button"), Icon.info, () -> Vars.ui.about.show())
                ),
                new MenuButton("@editor", Icon.terrain, () -> checkPlay(Vars.ui.maps::show)),
                (Vars.steam ? new MenuButton(Core.bundle.get("workshop"), Icon.steam, Vars.platform::openWorkshop) : null),
                new MenuButton(Core.bundle.get("mods"), Icon.book, Vars.ui.mods::show),
                new MenuButton(Core.bundle.get("settings"), Icon.settings, Vars.ui.settings::show)
            }}, Table.class, MenuButton[].class);

            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, ((Seq<MenuButton>) Reflect.get(Vars.ui.menufrag, "customButtons")).toArray(MenuButton.class)}, Table.class, MenuButton[].class);
            Reflect.invoke(Vars.ui.menufrag, "buttons", new Object[]{buttons, new MenuButton[]{new MenuButton(Core.bundle.get("quit"), Icon.exit, Core.app::exit)}}, Table.class, MenuButton[].class);

            buttons.pack();
        });


    }
    private static void checkPlay(Runnable run){
        if(!Vars.mods.hasContentErrors()){
            run.run();
        }else{
            Vars.ui.showInfo("@mod.noerrorplay");
        }
    }
}
