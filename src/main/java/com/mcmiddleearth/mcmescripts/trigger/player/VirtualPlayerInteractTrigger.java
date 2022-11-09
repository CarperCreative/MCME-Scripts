package com.mcmiddleearth.mcmescripts.trigger.player;

import com.mcmiddleearth.entities.events.events.player.VirtualPlayerInteractAtEvent;
import com.mcmiddleearth.entities.events.events.player.VirtualPlayerInteractEvent;
import com.mcmiddleearth.entities.events.handler.EntityEventHandler;
import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.trigger.EntitiesEventTrigger;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;

public class VirtualPlayerInteractTrigger extends EntitiesEventTrigger {
    /**
     * Stores the last interaction for each player. Because a single right click on an entity is sent as multiple
     * packets, it's possible that the packets will be interspersed with other players' interactions. We keep track of
     * interactions per player, to prevent a single player right click causing multiple triggers.
     */
    private final HashMap<UUID, InteractionStage> lastPlayerInteraction = new HashMap<>();

    public VirtualPlayerInteractTrigger(Action action) {
        super(action);
    }

    @SuppressWarnings("unused")
    @EntityEventHandler
    public void playerInteract(VirtualPlayerInteractEvent event) {
        TriggerContext context = new TriggerContext(this)
                .withPlayer(event.getPlayer().getBukkitPlayer())
                .withEntity(event.getVirtualEntity());

        // Prevent calling the trigger twice for a single entity interaction
        if (updateLastInteraction(event.getPlayer().getUniqueId(), InteractionStage.fromSlot(false, event.getHand()))) return;

        call(context);
    }

    @SuppressWarnings("unused")
    @EntityEventHandler
    public void playerInteractAt(VirtualPlayerInteractAtEvent event) {
        TriggerContext context = new TriggerContext(this)
                .withPlayer(event.getPlayer().getBukkitPlayer())
                .withEntity(event.getVirtualEntity());

        // Prevent calling the trigger twice for a single entity interaction
        if (updateLastInteraction(event.getPlayer().getUniqueId(), InteractionStage.fromSlot(true, event.getHand()))) return;

        call(context);
    }

    /**
     * Called to verify that the trigger isn't being called multiple times for a single interaction.
     *
     * Returns {@code true} if the interaction should be ignored.
     */
    private boolean updateLastInteraction(UUID playerUuid, InteractionStage stage) {
        InteractionStage lastStage = lastPlayerInteraction.get(playerUuid);

        // If `lastStage == null`, no previous interaction stored - player is definitely starting a new interaction. Do not ignore.
        // If `lastStage < stage`, player is continuing a previous interaction. Ignore.
        if (lastStage == null || lastStage.ordinal() >= stage.ordinal()) {
            // No need to store the last interaction stage
            if (stage == InteractionStage.INTERACT_OFF_HAND) {
                lastPlayerInteraction.remove(playerUuid);
            } else {
                lastPlayerInteraction.put(playerUuid, stage);
            }

            return false;
        }

        // Ignore
        return true;
    }

    private enum InteractionStage {
        INTERACT_AT_MAIN_HAND,
        INTERACT_MAIN_HAND,
        INTERACT_AT_OFF_HAND,
        INTERACT_OFF_HAND,
        ;

        public static InteractionStage fromSlot(boolean interactAt, EquipmentSlot slot) {
            switch (slot) {
                case HAND:
                    return interactAt ? INTERACT_AT_MAIN_HAND : INTERACT_MAIN_HAND;
                case OFF_HAND:
                    return interactAt ? INTERACT_AT_OFF_HAND : INTERACT_OFF_HAND;
                default:
                    throw new IllegalArgumentException("Invalid equipment slot: " + slot);
            }
        }
    }
}
