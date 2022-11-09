package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Location;

public class SpawnRandomLocationAction extends Action {

    private final Location center;

    private final int lifespan;

    private final SpawnRandomSelectionAction.RandomSpawnData data;

    public SpawnRandomLocationAction(Location center, SpawnRandomSelectionAction.RandomSpawnData data, int lifespan) {
        this.data = data;
        this.center = center;
        this.lifespan = lifespan;
        getDescriptor().indent()
                .addLine("Center location: "+center)
                .addLine("Lifespan: "+lifespan)
                .add(data.getDescriptor()).outdent();
    }

    @Override
    protected void handler(TriggerContext context) {
        data.spawn(context, center, lifespan);
    }
}
