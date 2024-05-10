package hrd.operators.dialog.dialogs;

import arc.*;
import hrd.operators.dialog.*;

public class DefaultDialogProvider extends DialogProvider{
    @Override
    public void start(){
        super.start();

        sendMessage("Assigned operator: " + Core.bundle.get("hrd.operator." + operator.name + ".name", operator.name), 10f, 3f);
    }
}
