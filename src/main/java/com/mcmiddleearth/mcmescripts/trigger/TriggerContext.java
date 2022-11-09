package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.entities.ai.goal.Goal;
import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.entities.events.events.McmeEntityEvent;
import com.mcmiddleearth.mcmescripts.IEntityContainer;
import com.mcmiddleearth.mcmescripts.ITagContainer;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.quest.party.Party;
import com.mcmiddleearth.mcmescripts.quest.Stage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.swing.text.Position;
import java.util.*;

public class TriggerContext {

    private final Trigger trigger;
    private Player player;
    private String message;
    private String name;

    private Party party;

    private boolean firstJoin;

    private McmeEntity entity;

    private Goal goal;

    private McmeEntityEvent entityEvent;

    private Descriptor descriptor;

    private World world;

    private final Map<String, Vector> positionContextMap = new HashMap<>();
    private final Map<String, Rotation> rotationContextMap = new HashMap<>();
    private final Map<String,List<Player>> playerTargetContextMap = new HashMap<>();
    private final Map<String,List<VirtualEntity>> virtualEntityTargetContextMap = new HashMap<>();

    public TriggerContext(Trigger trigger) {
        this.trigger = trigger;

        if(trigger.getTriggerContainer() instanceof Stage) {
            party = ((Stage) trigger.getTriggerContainer()).getQuest().getParty();
        }
        this.world = trigger.getWorld();
        this.player = (trigger.getPlayer()!=null?trigger.getPlayer().selectOnlyPlayers(this).stream().findFirst().orElse(null):null);
        this.entity = (trigger.getEntity()!=null?trigger.getEntity().selectOnlyVirtualEntities(this).stream().findFirst().orElse(null):null);
        descriptor = new Descriptor("Event Log:").indent()
                .addLine(trigger.getTriggerContainer().getName()+"."+trigger.getName())
                .addLine("Event player: "+(player!=null?player.getName():"--none--"))
                .addLine("Event entity: "+(entity!=null?entity.getName():"--none--"));
    }

    public TriggerContext(TriggerContext context) {
        this.player = context.player;
        this.trigger = context.trigger;
        this.party = context.party;
        this.message = context.message;
        this.firstJoin = context.firstJoin;
        this.entity = context.entity;
        this.goal = context.goal;
        this.entityEvent = context.entityEvent;
        this.name = context.name;
        this.descriptor = context.descriptor;
        this.world = context.world;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public ITriggerContainer getTriggerContainer() {
        return trigger.getTriggerContainer();
    }

    public IEntityContainer getEntityContainer() {
        return getTriggerContainer().getEntityContainer();
    }

    public ITagContainer getTagContainer() {
        return getTriggerContainer().getBossBattle();
    }

    public World getWorld(){
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public TriggerContext withPlayer(Player player) {
        this.player = player;
        getDescriptor().addLine("Override event player: " + player.getName());
        return this;
    }

    public Party getParty() {
        return party;
    }

    public TriggerContext withParty(Party party) {
        this.party = party;
        return this;
    }

    public boolean isQuestContext() {
        return party != null;
    }

    public String getMessage() {
        return message;
    }


    public TriggerContext withContext(String key, Vector position) {
        positionContextMap.put(key,position);
        getDescriptor().addLine("Added position context. key: " + key + ", position: " + position);
        return this;
    }

    public TriggerContext withContext(String key, Rotation rotation) {
        rotationContextMap.put(key,rotation);
        getDescriptor().addLine("Added rotation context. key: " + key + ", Rotation: " + rotation);
        return this;
    }

    public TriggerContext withContext(String key, Player player) {
        List<Player> playersWithKey = playerTargetContextMap.getOrDefault(key,new ArrayList<>());
        playersWithKey.add(player);
        playerTargetContextMap.put(key,playersWithKey);
        getDescriptor().addLine("Added Player context. key: " + key + ", Player: " + player);
        return this;
    }

    public TriggerContext withContext(String key, VirtualEntity virtualEntity) {
        List<VirtualEntity> virtualEntitiesWithKey = virtualEntityTargetContextMap.getOrDefault(key,new ArrayList<>());
        virtualEntitiesWithKey.add(virtualEntity);
        virtualEntityTargetContextMap.put(key,virtualEntitiesWithKey);
        getDescriptor().addLine("Added Virtual Entity context. key: " + key + ", Virtual Entity: " + virtualEntity);
        return this;
    }
    public TriggerContext withMessage(String message) {
        this.message = message;
        getDescriptor().addLine("Message: "+message);
        return this;
    }

    public String getName() {
        return name;
    }

    public TriggerContext withName(String name) {
        this.name = name;
        getDescriptor().addLine("Context name: "+name);
        return this;
    }

    public McmeEntityEvent getEntityEvent() {
        return entityEvent;
    }

    public TriggerContext withEntityEvent(McmeEntityEvent entityEvent) {
        this.entityEvent = entityEvent;
        getDescriptor().addLine("Entity event: "+entityEvent.getClass().getSimpleName());
        return this;
    }

    public boolean isFirstJoin() {
        return firstJoin;
    }

    public TriggerContext withFirstJoin(boolean firstJoin) {
        this.firstJoin = firstJoin;
        getDescriptor().addLine("First join: "+firstJoin);
        return this;
    }

    public McmeEntity getEntity() {
        return entity;
    }

    public TriggerContext withEntity(McmeEntity entity) {
        this.entity = entity;
        getDescriptor().addLine("Override event entity: " + entity.getName());
        return this;
    }

    public List<Player> getPlayerContext(String key){
        return playerTargetContextMap.get(key);
    }
    public List<VirtualEntity> getVirtualEntityContext(String key){
        return virtualEntityTargetContextMap.get(key);
    }
    public Vector getPositionContext(String key){
        return positionContextMap.get(key);
    }

    public Rotation getRotationContext(String key){
        return rotationContextMap.get(key);
    }

    public Goal getGoal() {
        return goal;
    }

    public TriggerContext withGoal(Goal goal) {
        this.goal = goal;
        getDescriptor().addLine("Context goal: "+(goal!=null?goal.getType().name():"--none--"));
        return this;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }
}
