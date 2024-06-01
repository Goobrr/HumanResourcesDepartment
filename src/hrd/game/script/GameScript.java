package hrd.game.script;

import arc.Core;
import arc.func.Prov;
import hrd.game.script.ScriptObjectives.*;
import arc.Events;
import arc.struct.ObjectMap;
import arc.util.Time;
import hrd.ui.HRUI;
import hrd.ui.debug.DebugOverlay;
import mindustry.Vars;
import mindustry.game.EventType;

// terrible state machine for scripted sectors/games, because the vanilla objective system is ass
public class GameScript {
    public boolean running = false;
    public boolean started = false;

    public float timer;

    public String state = "";

    public ObjectMap<String, Float> flags = new ObjectMap<>();

    public ObjectMap<String, WaitTimer> timers = new ObjectMap<>();

    public ObjectMap<String, ScriptObjective> objectives = new ObjectMap<>();

    public String name;

    public GameScript(String name){
        this.name = name;
        Events.run(EventType.Trigger.update, () -> {
            if(Vars.state.isGame() && !Vars.state.isPaused() && running && started){
                timer += (Time.delta / Core.graphics.getFramesPerSecond());
                update(timer);
            }
        });

        Events.run(EventType.Trigger.draw, () -> {
            draw(timer);
        });
    }

    public void reset(){
        timer = 0f;
        timers.clear();
        flags.clear();
        objectives.clear();

        state = "";
    }

    public void start(){
        started = true;
        running = true;

        reset();

        DebugOverlay.active = this;
        HRUI.activeGameScript = this;

        //GameSequenceIO.load(this);
    }

    public void stop(){
        started = false;
        running = false;

        //GameSequenceIO.save(this);
    }

    public void pause(){
        running = false;
    }

    public void resume(){
        running = true;
    }

    public void update(float time){
    }

    public void draw(float time){

    }

    protected boolean wait(String identifier, float duration, boolean repeat){
        boolean exists = timers.containsKey(identifier);

        if(exists){
            return timers.get(identifier).finished(timer);
        }else{
            WaitTimer wait = new WaitTimer(timer, duration, repeat);
            timers.put(identifier, wait);
            return wait.finished(timer);
        }
    };

    protected void schedule(String identifier, float delay, boolean repeat, Runnable run){
        boolean exists = timers.containsKey(identifier);

        WaitTimer wait;
        if(exists){
            wait = timers.get(identifier);
        }else{
            wait = new WaitTimer(timer, delay, repeat);
            timers.put(identifier, wait);
        }

        // Neat hack. Only executes once when the timer finishes
        // Because wait.finished() sets wait.finished to true if it returns true
        if(!wait.finished && wait.finished(timer)) run.run();
    }

    protected boolean objective(String identifier, Prov<ScriptObjective> prov){
        boolean exists = objectives.containsKey(identifier);

        ScriptObjective objective;
        if(exists){
            objective = objectives.get(identifier);
        }else{
            objective = prov.get();
            objectives.put(identifier, objective);
        }

        return objective.update();
    }

    protected void removeTimers(String... identifiers){
        for(String identifier : identifiers){
            timers.remove(identifier);
        }
    }

    protected void removeFlags(String... identifiers){
        for(String identifier : identifiers){
            flags.remove(identifier);
        }
    }

    protected void removeObjectives(String... identifiers){
        for(String identifier : identifiers){
            objectives.remove(identifier);
        }
    }

    public static class WaitTimer {
        public float startTime;
        public float waitTime;
        public boolean finished;

        public boolean repeat;
        public WaitTimer(float startTime, float waitTime, boolean repeat) {
            this.startTime = startTime;
            this.waitTime = waitTime;
            this.repeat = repeat;
        }

        public boolean finished(float time){
            boolean done = time >= (startTime + waitTime);
            if(!finished && done){
                finished = !repeat;
                if(repeat) startTime = time; // restart the timer
            }
            return done;
        };
    };
}
