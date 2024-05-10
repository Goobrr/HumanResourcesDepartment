package hrd.operators.dialog.dialogs;

import hrd.operators.dialog.*;

public class MinakoDialogProvider extends DialogProvider{
    @Override
    public void start(){
        super.start();

        sendMessage("Welcome back, manager. How has it been?", 10f, 3f);
        sendMessage("Everything's in tip-top shape and production is smooth as usual. I personally made sure of that.", 10f, 3f);
    }
}