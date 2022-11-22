package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.action.targeted.PlayerTargetedAction;
import com.mcmiddleearth.mcmescripts.event.eventTarget.PlayerEventTarget;
import com.mcmiddleearth.mcmescripts.listener.PlayerEyeEffectBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class EyeEffectAction extends PlayerTargetedAction {

    public EyeEffectAction(PlayerEventTarget target, int duration) {
        super(target, (player, context) -> {
            player.teleport(player.getLocation().toBlockLocation().add(new Vector(0.5,0,0.5)));
            Block block = player.getLocation().getWorld().getBlockAt(player.getLocation()).getRelative(BlockFace.UP);
            Block[] adjacent = new Block[4];
            adjacent[0] = block.getRelative(BlockFace.NORTH);
            adjacent[1] = block.getRelative(BlockFace.SOUTH);
            adjacent[2] = block.getRelative(BlockFace.WEST);
            adjacent[3] = block.getRelative(BlockFace.EAST);

            Set<BlockState> blockChanges = new HashSet<>();
            blockChanges.add(block.getState());
            BlockState tempState = block.getState();
            tempState.setType(Material.NETHER_PORTAL);
            block.setBlockData(tempState.getBlockData(),false);
            for(Block ad: adjacent) {
                if(ad.isPassable()) {
                    blockChanges.add(ad.getState());
                    tempState = ad.getState();
                    tempState.setType(Material.BARRIER);
                    ad.setBlockData(tempState.getBlockData(),false);
                }
            }
            float walkSpeed = player.getWalkSpeed();
            float flySpeed = player.getFlySpeed();
            Listener blockJump = new PlayerEyeEffectBlockListener(player,blockChanges);
            Bukkit.getPluginManager().registerEvents(blockJump,MCMEScripts.getInstance());
            player.setFlySpeed(0);
            player.setWalkSpeed(0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(BlockState state: blockChanges) {
                        state.update(true,false);
                    }
                    player.setFlySpeed(flySpeed);
                    player.setWalkSpeed(walkSpeed);
                    HandlerList.unregisterAll(blockJump);
                }
            }.runTaskLater(MCMEScripts.getInstance(),duration);
        });
        getDescriptor().indent().addLine("Duration: "+duration).outdent();
    }
}
