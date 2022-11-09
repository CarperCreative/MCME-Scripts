package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.event.target.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

public class ExecuteCommandAction extends Action {

    private final String command;
    private final EventPosition executePosition;
    private final EventRotation executeRotation;

    public ExecuteCommandAction(String command, EventPosition executePosition, EventRotation executeRotation) {
        this.command = command;
        this.executePosition = executePosition;
        this.executeRotation = executeRotation;
        getDescriptor().indent().addLine("Command: "+command).outdent();
    }

    @Override
    public void handler(TriggerContext context){
        String prefix = "";
        if(executePosition != null || executeRotation != null)
        {
            prefix = "execute ";
            if(executePosition != null){
                Vector position = executePosition.getPosition(context);
                prefix += "positioned " + position.getX() + " " + position.getY() + " " + position.getZ() + " ";
            }
            if(executeRotation != null){
                Rotation rotation = executeRotation.getRotation(context);
                prefix += "rotated " + (rotation.getYaw()) + " " + (rotation.getPitch()) + " ";
            }
            prefix += "run ";
        }
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),prefix + command);
        DebugManager.verbose(Modules.Action.execute(ExecuteCommandAction.class),"Executed command: " + prefix + command);
    }
}
