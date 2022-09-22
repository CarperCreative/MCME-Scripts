package com.mcmiddleearth.mcmescripts.quest.party;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.quest.QuestData;
import com.mcmiddleearth.mcmescripts.quest.QuestManager;
import com.mcmiddleearth.mcmescripts.utils.JsonUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A party is a group of players who do quests together.
 *
 * A party need to be active in order to do quests. There are two possible rule for a party to be active:
 * 1. Any of the players is online and has this party chosen as active party
 * 2. All players are online and have this party chosen as active party
 */
public class Party {

    /**
     * Unique identifier of this party. Is created randomly when the party is created.
     */
    private final UUID uuid;

    /**
     * Name of this party, can be chosen freely. There may be more than one party with the same name.
     */
    private String name;

    /**
     * Flag to indicate if a party is active (can do quests) only if all members are online and have this party activated
     */
    private boolean requireAllActive = true;

    /**
     * List of all players in this party no matter if they are online or not.
     */
    private final List<PartyPlayer> players = new ArrayList<>();

    /**
     * Set of all players in this party who are online.
     */
    private Set<Player> onlinePlayers = new HashSet<>();

    /**
     * Creator of this party. If the founder leaves the party the next oldest player in this party becomes new founder.
     */
    private PartyPlayer founder;

    /**
     * Folder to store party data.
     */
    private static final File partiesFolder = new File(MCMEScripts.getInstance().getDataFolder(),"parties");

    /**
     * File to store data of this party.
     */
    private final File dataFile;

    /**
     * Keys in party data json file.
     */
    private static final String KEY_QUESTS = "quests",
                                KEY_MEMBERS = "members",
                                KEY_FOUNDER = "founder",
                                KEY_NAME    = "name";

    static {
        if(!partiesFolder.exists()) {
            if(partiesFolder.mkdir()) {
                Logger.getLogger(MCMEScripts.class.getSimpleName()).info("Party folder created.");
            }
        }
    }

    public Party(UUID uuid, String name, PartyPlayer founder) {
        this.founder = founder;
        this.uuid = uuid;
        this.name = name;
        dataFile = new File(partiesFolder, uuid.toString()+".json");
    }

    public void addPlayer(PartyPlayer player) {
        players.add(player);
    }

    public void removePlayer(PartyPlayer player) {
        players.remove(player);
        if(founder.equals(player)) {
            founder = players.get(0);
        }
    }

    public boolean isOnline() {
        return players.stream().allMatch(PartyPlayer::isOnline);
    }

    public boolean isActive() {
        if(requireAllActive) {
            return players.stream().allMatch(player -> player.isOnline() && player.getActiveParty().equals(this));
        } else {
            return players.stream().anyMatch(player -> player.isOnline() && player.getActiveParty().equals(this));
        }
    }

    public Collection<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void updateOnlinePlayers() {
        onlinePlayers = players.stream().map(PartyPlayer::getOnlinePlayer).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public PartyPlayer getPartyPlayer(UUID playerId) {
        return players.stream().filter(player -> player.getUniqueId().equals(playerId)).findAny().orElse(null);
    }

    public List<PartyPlayer> getPartyPlayers() {
        return players;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isRequireAllActive() {
        return requireAllActive;
    }

    public void setRequireAllActive(boolean requireAllActive) {
        this.requireAllActive = requireAllActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return new File(partiesFolder, uuid+".json");
    }

    public void deleteFile() {
        if(getFile().delete()) {
            try {
                throw new IOException("Can't delete party file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static PartyData loadPartyData(UUID partyId) {
        File dataFile = new File(partiesFolder,partyId+".json");
        if(dataFile.exists()) {
            try {
                PartyData data = new PartyData();
                data.members = new HashSet<>();
                JsonObject jsonData = JsonUtils.loadJsonData(dataFile);
                assert jsonData != null;
                jsonData.get(KEY_MEMBERS).getAsJsonArray().forEach(party -> {
                    data.members.add(UUID.fromString(party.getAsString()));
                });
                data.name = jsonData.get(KEY_NAME).getAsString();
                data.founder = UUID.fromString(jsonData.get(KEY_FOUNDER).getAsString());

                Set<QuestData> questData = new HashSet<>();
                JsonObject jsonQuestData = jsonData.get(KEY_QUESTS).getAsJsonObject();
                jsonQuestData.entrySet().forEach(questEntry -> {
                    questData.add(QuestData.loadQuestData(questEntry.getKey(), questEntry.getValue().getAsJsonObject()));
                });
                data.questData = questData;
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void save() {
        //save party data file including quest data
//Logger.getGlobal().info("Save party data file!");
//DebugManager.printStackTrace();
        try(JsonWriter writer = JsonUtils.getGson().newJsonWriter(new FileWriter(dataFile))) {
            writer.beginObject()
                .name(KEY_NAME).value(name)
                .name(KEY_FOUNDER).value(founder.getUniqueId().toString())
                .name(KEY_MEMBERS).beginArray();
                    for(PartyPlayer player: players) { writer.value(player.getUniqueId().toString());}
                writer.endArray()
                .name(KEY_QUESTS)
                .beginObject();
                    QuestManager.getQuests(this).forEach(quest -> quest.getQuestData().writeJson(writer));
                writer.endObject();
            writer.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return uuid.equals(party.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public static class PartyData {
        public Set<UUID> members;
        public UUID founder;
        public String name;
        public Set<QuestData> questData;
    }
}