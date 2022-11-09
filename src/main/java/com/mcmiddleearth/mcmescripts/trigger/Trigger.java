package com.mcmiddleearth.mcmescripts.trigger;


import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.logging.Logger;

public abstract class Trigger {

    private ITriggerContainer triggerContainer;

    private String name;

    private boolean callOnce = false;

    private Selector entity;
    private Selector player;

    private World world;

    public void register(ITriggerContainer triggerContainer) {
        triggerContainer.addTrigger(this);

        Logger.getGlobal().info("Registered trigger: " + getName() + " with trigger container " + triggerContainer.getName());
        this.triggerContainer = triggerContainer;
        //DebugManager.info(Modules.Trigger.register(this.getClass()),
        //        "Scrip: "+script.getName()+" Call once: "+callOnce);
    }

    public void unregister() {
        triggerContainer.removeTrigger(this);
        //DebugManager.info(Modules.Trigger.unregister(this.getClass()),
        //        "Scrip: "+script.getName());
    }

    public ITriggerContainer getTriggerContainer() {
        return triggerContainer;
    }

    public Selector getEntity() {
        return entity;
    }

    public void setEntity(Selector entity) {
        this.entity = entity;
    }

    public Selector getPlayer() {
        return player;
    }

    public void setPlayer(Selector player) {
        this.player = player;
    }

    public void setWorld(String worldName) {
        this.world = Bukkit.getWorld(worldName);
    }

    public World getWorld(){
        return world;
    }

    public void setTriggerContainer(ITriggerContainer triggerContainer) {
        this.triggerContainer = triggerContainer;
    }

    public boolean isCallOnce() {
        return callOnce;
    }

    public void setCallOnce(boolean callOnce) {
        this.callOnce = callOnce;
    }

    public void call(TriggerContext context) {
        if(callOnce) {
            unregister();
            context.getDescriptor().addLine("Unregistering call once event!");
        }
        DebugManager.info(Modules.Trigger.call(this.getClass()),context.getDescriptor().print(""));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Descriptor getDescriptor() {
        return new Descriptor(this.getClass().getSimpleName() + ": "+name).indent()
                .addLine("Call once: "+callOnce);
                /*.addLine("Trigger entity: "+(entity!=null?entity.getSelector():"--none--"))//+" at "+entity.getLocation().toString():"--none--"))
                .addLine("Trigger player: "+(entity!=null?player.getSelector():"--none--"))//+" at "+player.getLocation().toString():"--none--"))
                .addLine("Trigger location: "+(location!=null?location:"--none--")).outdent();*/
    }

    /*public String print(String indent) {
        return getDescriptor().print(indent);
    }*/

}
