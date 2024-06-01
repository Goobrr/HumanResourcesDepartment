package hrd.game.script;

import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.util.io.*;
import mindustry.Vars;

import java.io.*;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

// Stores sequence data
public class GameScriptIO {
    public static final byte[] header = {'H', 'R', 'S', 'D'};
    public static void save(GameScript sequence){
        Fi file = Vars.saveDirectory.child(sequence.name + ".hrsd");
        write(file, sequence);
    }

    public static void load(GameScript sequence){
        Fi file = Vars.saveDirectory.child(sequence.name + ".hrsd");
        if(file.exists()) read(file, sequence);
    }

    public static void write(Fi file, GameScript sequence){
        OutputStream os = new FastDeflaterOutputStream(file.write(false, Vars.bufferSize));
        try(DataOutputStream stream = new DataOutputStream(os)){
            stream.write(header);

            stream.writeUTF(sequence.state);
            stream.writeFloat(sequence.timer);

            stream.writeInt(sequence.flags.size);
            for(ObjectMap.Entry<String, Float> entry : sequence.flags.iterator()){
                stream.writeUTF(entry.key);
                stream.writeFloat(entry.value);
            }

            stream.writeInt(sequence.timers.size);
            for(ObjectMap.Entry<String, GameScript.WaitTimer> entry : sequence.timers.iterator()){
                stream.writeFloat(entry.value.startTime);
                stream.writeFloat(entry.value.waitTime);
                stream.writeBoolean(entry.value.repeat);

                stream.writeBoolean(entry.value.finished);
                stream.writeUTF(entry.key);
            }

            stream.writeInt(sequence.objectives.size);
            for(ObjectMap.Entry<String, ScriptObjectives.ScriptObjective> entry : sequence.objectives.iterator()){
                stream.writeUTF(entry.value.getClass().getSimpleName());
                entry.value.write(Writes.get(stream));
                stream.writeUTF(entry.key);
            }

        }catch(Throwable e){
            throw new RuntimeException(e);
        }
    }

    public static void read(Fi file, GameScript sequence){
        InputStream is = new InflaterInputStream(file.read(Vars.bufferSize));
        try(CounterInputStream counter = new CounterInputStream(is); DataInputStream stream = new DataInputStream(counter)){
            byte[] bytes = new byte[header.length];
            stream.readFully(bytes);
            if(!Arrays.equals(bytes, header)){
                throw new IOException("Womp womp, incorrect header! Expecting: " + Arrays.toString(header) + "; Actual: " + Arrays.toString(bytes));
            }

            sequence.state = stream.readUTF();
            sequence.timer = stream.readFloat();

            float flags = stream.readInt();
            for(int i = 0; i < flags; i++){
                sequence.flags.put(stream.readUTF(), stream.readFloat());
            }

            float timers = stream.readInt();
            for(int i = 0; i < timers; i++){
                GameScript.WaitTimer t = new GameScript.WaitTimer(
                        stream.readFloat(),
                        stream.readFloat(),
                        stream.readBoolean()
                );
                t.finished = stream.readBoolean();
                sequence.timers.put(stream.readUTF(), t);
            }

            float objectives = stream.readInt();
            for(int i = 0; i < objectives; i++){
                ScriptObjectives.ScriptObjective objective = ScriptObjectives.get(stream.readUTF());
                objective.read(Reads.get(stream));
                sequence.objectives.put(stream.readUTF(), objective);
            }

        }catch(Throwable e){
            throw new RuntimeException(e);
        };
    }
}
