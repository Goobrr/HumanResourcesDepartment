package hrd.content.logic;

import arc.func.Func;
import arc.func.Prov;
import arc.scene.ui.layout.*;
import hrd.operators.Operators;
import mindustry.gen.LogicIO;
import mindustry.logic.*;
import mindustry.ui.Styles;

public class HRLStatements {

    public static void load(){
        registerStatement("codecmessage", args -> new CodecMessageStatement(args[1], args[2], args[3], args[4]), CodecMessageStatement::new);
    }
    public static class CodecMessageStatement extends LStatement {
        public String operatorName = "@minako", region = "", message = "\"frog\"", messageLifetime = "10";

        public CodecMessageStatement(String operatorName, String region, String message, String messageLifetime){
            this.operatorName = operatorName;
            this.region = region;
            this.message = message;
            this.messageLifetime = messageLifetime;
        }

        public CodecMessageStatement() {
        }

        @Override
        public void build(Table table) {
            table.top().left();
            table.table(t -> {
                t.top().left();
                t.table(f -> {
                    f.top().left();
                    f.add("Operator ");
                    f.field(operatorName, Styles.nodeField, s -> operatorName = sanitize(s)).size(200f, 40f).pad(2f).color(f.color);
                });
                t.row();
                t.table(f -> {
                    f.top().left();
                    f.add("Texture ");
                    f.field(region, Styles.nodeField, s -> region = sanitize(s)).size(200f, 40f).pad(2f).color(f.color);
                });
                t.row();
                t.table(f -> {
                    f.top().left();
                    f.add("Lifetime ");
                    f.field(messageLifetime, Styles.nodeField, s -> messageLifetime = sanitize(s)).size(200, 40f).pad(2f).color(f.color);
                });
            }).top().left().padBottom(3f);
            table.row();
            table.table(t -> {
                t.field(message, Styles.nodeField, s -> message = sanitize(s)).height(40f).growX().pad(2f).color(t.color);
            }).width(0f).growX().padRight(3f).top().left();
        }

        @Override
        public void write(StringBuilder builder) {
            super.write(builder);
            builder
                    .append("codecmessage ")
                    .append(operatorName).append(" ")
                    .append(region).append(" ")
                    .append(message).append(" ")
                    .append(messageLifetime);
        }

        @Override
        public LCategory category() {
            return LCategory.world;
        }

        @Override
        public LExecutor.LInstruction build(LAssembler b) {
            int opid = -1;
            if(operatorName.startsWith("@")){
                opid = Operators.getByName(operatorName.substring(1)).id;
            }
            return new HRLInstructions.CodecMessageI(opid, b.var(region), b.var(message), b.var(messageLifetime));
        }
    }

    public static void registerStatement(String name, Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
