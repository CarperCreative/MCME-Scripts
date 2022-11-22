package com.mcmiddleearth.mcmescripts.condition;

import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.event.target.IEventTarget;
import com.mcmiddleearth.mcmescripts.quest.tags.AbstractTag;
import com.mcmiddleearth.mcmescripts.quest.tags.IntegerTag;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.logging.Logger;

public class ScoreboardValueCondition extends Condition {

    private final Criterion test;
    private final String scoreboardPlayerName;
    private final String scoreboardObjectiveName;

    public ScoreboardValueCondition(String scoreboardPlayerName, String scoreboardObjectiveName, Criterion test) {
        this.test = test;
        this.scoreboardPlayerName = scoreboardPlayerName;
        this.scoreboardObjectiveName = scoreboardObjectiveName;
    }

    @Override
    public boolean test(TriggerContext context) {

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective = board.getObjective(scoreboardObjectiveName);
        if(objective == null){
            context.getDescriptor().addLine("Scoreboard objective doesn't exist").outdent();
            Logger.getGlobal().warning("Scoreboard objective + " + scoreboardObjectiveName + " doesn't exist");
            board.getObjectives().forEach(o -> Logger.getGlobal().info(o.getName()));
        } else {
            Score score = objective.getScore(scoreboardPlayerName);
            int value = score.getScore();
            Logger.getGlobal().warning("Name: "+scoreboardPlayerName);
            Logger.getGlobal().warning("Value: "+value);
            boolean result = test.apply(value);
            context.getDescriptor().addLine("Test result: "+result).outdent();
            Logger.getGlobal().warning("Test result: "+result);
            return result;
        }
        return false;
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                .addLine("Scoreboard Player Name Name: " + scoreboardPlayerName)
                .addLine("Scoreboard Objective Name: " + scoreboardObjectiveName)
                .addLine("Criterion: "+test.getComparator()+test.getLimit())
                .outdent();
    }

}
