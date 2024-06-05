package hrd.ui.debug;

import arc.scene.*;
import arc.util.Align;
import hrd.game.script.GameScript;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class DebugOverlay {
    public static GameScript active;

    public static void build(Group parent){
        parent.fill(cont -> {
            cont.top().right();
            cont.table(c -> {
                c.top().right();
                c.defaults().pad(5f);
                c.table(Styles.black8, t -> {
                    t.margin(5f);
                    t.defaults().top().left().pad(5f);
                    t.top().left();
                    t.label(() -> "Active Script").get().setAlignment(Align.left);
                    t.label(() -> active == null ? "NONE" : "[accent]" + active.name + "[]").get().setAlignment(Align.left);
                    t.row();
                    t.label(() -> "State").top().left().get().setAlignment(Align.left);
                    t.label(() -> active == null ? "" : active.state).get().setAlignment(Align.left);
                    t.row();
                    t.label(() -> "Time").top().left().get().setAlignment(Align.left);
                    t.label(() -> active == null ? "" : "" + active.timer).get().setAlignment(Align.left);

                    t.layout();
                }).top().right().expand();

                c.table(Styles.black8, t -> {
                    Runnable rebuild = () -> {
                        t.clearChildren();

                        t.margin(5f);
                        t.defaults().top().left().pad(2.5f);
                        t.top().left();

                        t.add("Flags").colspan(2).color(Pal.accent).get().setAlignment(Align.left);
                        t.row();

                        t.label(() -> "Flag").get().setAlignment(Align.left);
                        t.label(() -> "Value").get().setAlignment(Align.left);
                        t.row();
                        t.table().height(10f);

                        if(active != null){
                            active.flags.entries().forEach(entry -> {
                                t.row();
                                t.add(entry.key).get().setAlignment(Align.left);
                                t.add("" + entry.value).get().setAlignment(Align.left);
                            });
                        }
                    };

                    t.update(rebuild);

                    rebuild.run();
                }).top().right().expand();

                c.table(Styles.black8, t -> {
                    Runnable rebuild = () -> {
                        t.clearChildren();

                        t.margin(5f);
                        t.defaults().top().left().pad(5f);
                        t.top().left();

                        t.add("Timers").colspan(5).color(Pal.accent).get().setAlignment(Align.left);
                        t.row();

                        t.label(() -> "Timer").get().setAlignment(Align.left);
                        t.label(() -> "Start").get().setAlignment(Align.left);
                        t.label(() -> "End").get().setAlignment(Align.left);
                        t.label(() -> "Repeat").get().setAlignment(Align.left);
                        t.label(() -> "Status").get().setAlignment(Align.left);
                        t.row();
                        t.table().height(10f);

                        if(active != null){
                            active.timers.entries().forEach(entry -> {
                                t.row();
                                t.add(entry.key).get().setAlignment(Align.left);
                                t.add("" + entry.value.startTime).get().setAlignment(Align.left);
                                t.add("" + (entry.value.waitTime + entry.value.startTime)).get().setAlignment(Align.left);
                                t.add((entry.value.repeat ? "REPEATS" : "")).get().setAlignment(Align.left);
                                t.add((entry.value.finished ? "FINISHED" : "")).get().setAlignment(Align.left);
                            });
                        }
                    };

                    t.update(rebuild);

                    rebuild.run();
                }).top().right().expand();

                c.table(Styles.black8, t -> {
                    Runnable rebuild = () -> {
                        t.clearChildren();

                        t.margin(5f);
                        t.defaults().top().left().pad(5f);
                        t.top().left();

                        t.add("Objectives").colspan(3).color(Pal.accent).get().setAlignment(Align.left);
                        t.row();

                        t.label(() -> "Key").get().setAlignment(Align.left);
                        t.label(() -> "Type").get().setAlignment(Align.left);
                        t.label(() -> "Status").get().setAlignment(Align.left);
                        t.row();
                        t.table().height(10f);

                        if(active != null){
                            active.objectives.entries().forEach(entry -> {
                                t.row();
                                t.add(entry.key).get().setAlignment(Align.left);
                                t.add(entry.value.getClass().getSimpleName()).get().setAlignment(Align.left);
                                t.add(entry.value.finished ? "FINISHED" : "").get().setAlignment(Align.left);
                            });
                        }
                    };

                    t.update(rebuild);

                    rebuild.run();
                }).top().right().expand();
            }).top().right().expand();
        });
    }
}
