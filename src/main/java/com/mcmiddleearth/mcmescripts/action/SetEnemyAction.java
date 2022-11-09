package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.action.targeted.VirtualEntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import com.mcmiddleearth.mcmescripts.event.target.VirtualEntityEventTarget;

import java.util.HashSet;
import java.util.List;

public class SetEnemyAction extends VirtualEntityTargetedAction {

    public SetEnemyAction(VirtualEntityEventTarget target, EntityEventTarget enemyTarget) {
        super(target, (entity, context)  -> {
            List<McmeEntity> enemies = enemyTarget.getTargets(context);
            if(enemies.isEmpty()) {
                context.getDescriptor().addLine("Selected Enemies: --none--");
            } else {
                context.getDescriptor().addLine("Selected Enemies:").indent();
                enemies.forEach(enemy -> context.getDescriptor().addLine(enemy.getName()));
                context.getDescriptor().outdent();
            }
            entity.setEnemies(new HashSet<>(enemies));
        });
        getDescriptor().indent().addLine("Enemy selector: " + target.toString()).outdent();
    }
}
