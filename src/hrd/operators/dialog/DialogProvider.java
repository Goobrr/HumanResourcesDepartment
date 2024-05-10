package hrd.operators.dialog;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Timer.*;
import hrd.*;
import hrd.HREvents.*;
import hrd.operators.*;

public class DialogProvider{
    protected boolean active = false;

    protected Queue<Message> messageQueue = new Queue<>();
    protected boolean queueRunning = false;
    public Operator operator;
    public DialogProvider(){

    };

    public void update(){

    }

    public <T> void addListener(Class<T> event, Cons<T> listener){
        Events.on(event, e -> {
            if(!active) return;
            listener.get(e);
        });
    }

    public void start(){
        active = true;
    }

    public void stop(){
        active = false;
    }

    public void sendMessage(String message, float lifetime, float delay){
        sendMessage(message, "human-resources-department-" + operator.name + "-portrait-bust", () -> {}, lifetime, delay);
    }

    public void sendMessage(String message, String region, Runnable callback, float lifetime, float delay){
        messageQueue.addLast(new Message(operator, message, region, callback, lifetime, delay));
        if(!queueRunning){
            runQueue();
        }
    }

    public void runQueue(){
        queueRunning = true;
        if(messageQueue.isEmpty()){
            queueRunning = false;
            return;
        }

        Message msg = messageQueue.removeFirst();
        Timer.schedule(() -> {
            Core.app.post(() -> Events.fire(msg.event));
            runQueue();
        }, msg.delay);
    }

    static class Message{
        DialogMessage event;

        float delay;
        public Message(Operator sender, String message, String region, Runnable callback, float lifetime, float delay){
            this.event = new DialogMessage(sender, Core.atlas.find(region, "human-resources-department-default-portrait"), message, callback == null ? () -> {} : callback, lifetime);
            this.delay = delay;
        }
    }
}
