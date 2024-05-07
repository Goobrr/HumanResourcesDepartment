package hrd.ui;

import arc.*;
import arc.scene.ui.*;
import mindustry.ui.dialogs.*;

public class OperatorsDialog extends BaseDialog{
    public OperatorsDialog(){
        super(Core.bundle.get("operators"));
        addCloseButton();
    }
}
