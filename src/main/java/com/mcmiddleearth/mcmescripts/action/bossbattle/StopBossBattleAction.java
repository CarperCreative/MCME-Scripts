package com.mcmiddleearth.mcmescripts.action.bossbattle;

import com.mcmiddleearth.mcmescripts.bossbattle.BossBattle;
import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;

public class StopBossBattleAction extends BossBattleAction{

    @Override
    protected void handler(BossBattle instance, TriggerContext context) {
        instance.stop();
    }
}
