package hrd.game.script.scripts;

import arc.Events;
import arc.graphics.g2d.Draw;
import hrd.HREvents.DialogMessage;
import hrd.game.script.GameScript;
import hrd.game.script.ScriptObjectives.*;
import hrd.operators.Operators;
import hrd.ui.HRUI;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.game.MapObjectives.*;
import mindustry.graphics.Layer;

// TODO use bundles
public class TutorialScript extends GameScript {
    public TutorialScript(String name) {
        super(name);
    }

    @Override
    public void start() {
        super.start();
        state = "tutorial-start";
    }

    @Override
    public void update(float time) {
        // Starting messages
        if(state == "tutorial-start" && wait("start-wait", 2f, false)){
            schedule("d1", 1f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "My name's [accent]Minako[], by the way."));
            });

            schedule("d2", 4f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "I'll reintroduce myself properly when we finish this task list."));
            });

            schedule("d3", 7f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "First off, check if the coreship is functioning properly. Try moving around."));
            });

            schedule("d4", 11f, false, () -> {
                state = "tutorial-move";

                // Timer cleanup.
                removeTimers("d1", "d2", "d3", "d4", "d5", "start-wait");

                // Return player controls
                HRUI.inputLock = false;
                Vars.ui.hudfrag.shown = true;

                // save player pos for movement tutorial
                flags.put("player-start-x", Vars.player.x);
                flags.put("player-start-y", Vars.player.y);
            });

            return;
        }

        // Movement Tutorial
        if(state == "tutorial-move" && !(Vars.player.x == flags.get("player-start-x") || Vars.player.y == flags.get("player-start-y"))){
            state = "tutorial-move-done";
            removeFlags("player-start-x", "player-start-y");
        }

        if(state == "tutorial-move-done" && wait("tutorial-move-done-wait", 3f, false)){
            schedule("d1", 1f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "Great!"));
            });

            schedule("d2", 3f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "We should continue with our tasks so you can get used to the controls"));
            });

            schedule("d3", 5f, false, () -> {
                state = "tutorial-drill";

                removeTimers("d1", "d2", "d3", "tutorial-move-done-wait");
            });

            return;
        }

        // Drilling tutorial
        if(state == "tutorial-drill" && wait("tutorial-drill-wait", 2f, false)){
            if(!flags.containsKey("tutorial-drill-markers")){
                // Markers
                ShapeTextMarker m = new ShapeTextMarker();
                m.sides = 4;
                m.radius = 6;
                m.text = "Tap to [accent]drill[] " + Blocks.oreCopper.emoji() + "[accent] Copper Ore[] and collect resources.";
                m.world = true;
                m.pos.set(137 * 8, 52 * 8);

                Vars.state.markers.add(0, m);

                Events.fire(new DialogMessage(Operators.minako, "I've set a marker to an " + Blocks.oreCopper.emoji() + "[accent]Ore Vein[], I need you to collect some of it for some structures."));

                flags.put("tutorial-drill-markers", 0f);
            }

            if(objective("tutorial-drill-objective", () -> new GatherItemObjective(Items.copper, Vars.state.rules.defaultTeam.items().get(Items.copper), 25))){
                state = "tutorial-drill-done";

                removeFlags("tutorial-drill-markers");
                removeTimers("tutorial-drill-wait");
                removeObjectives("tutorial-drill-objective");

                Vars.state.markers.remove(0);
            };

            return;
        }

        if(state == "tutorial-drill-done" && wait("tutorial-drill-done-wait", 1f, false)){
            schedule("d1", 1f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "Now we should automate the extraction by constructing " + Blocks.mechanicalDrill.emoji() + "[accent] Drills[]." ));
            });

            schedule("d2", 5f, false, () -> {
                Events.fire(new DialogMessage(Operators.minako, "You can route them to the Core structure using " + Blocks.conveyor.emoji() + " [accent]Conveyors[]."));
            });

            schedule("d3", 8f, false, () -> {
                state = "tutorial-build-drills";

                removeTimers("d1", "d2", "d3", "tutorial-drill-done-wait");
            });
        }

        // Build drills objective
        if(state == "tutorial-build-drills"){

        }
    }

    @Override
    public void draw(float time) {
        // Build drill objective
        Draw.draw(Layer.blockOver, () -> {

        });
    }
}
