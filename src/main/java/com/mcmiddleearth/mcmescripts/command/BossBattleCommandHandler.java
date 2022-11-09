package com.mcmiddleearth.mcmescripts.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcmescripts.Permission;
import com.mcmiddleearth.mcmescripts.bossbattle.BossBattle;
import com.mcmiddleearth.mcmescripts.bossbattle.BossBattleManager;
import com.mcmiddleearth.mcmescripts.quest.party.Party;
import com.mcmiddleearth.mcmescripts.quest.party.PartyManager;
import com.mcmiddleearth.mcmescripts.quest.party.PartyPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class BossBattleCommandHandler extends AbstractCommandHandler implements TabExecutor {

    public BossBattleCommandHandler(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(sender -> ((ScriptsCommandSender)sender).getCommandSender().hasPermission(Permission.USER.getNode()))
            .then(HelpfulLiteralBuilder.literal("start")
                .then(HelpfulRequiredArgumentBuilder.argument("name", word())
                    .executes(context -> {
                        String bossBattleName = context.getArgument("name",String.class);
                        context.getSource().sendMessage("Starting boss battle '" + bossBattleName + "'.");
                        BossBattleManager.createBossBattle(bossBattleName, context);
                        return 0;
                    })))
            .then(HelpfulLiteralBuilder.literal("stop")
                .then(HelpfulRequiredArgumentBuilder.argument("name", word())
                    .executes(context -> {
                        String bossBattleName = context.getArgument("name",String.class);
                        context.getSource().sendMessage("Stopping boss battle '" + bossBattleName + "'.");
                        BossBattleManager.removeBossBattle(bossBattleName);
                        return 0;
                    })))
            .then(HelpfulLiteralBuilder.literal("refresh")
                        .executes(context -> {
                            BossBattleManager.removeBossBattles();
                            BossBattleManager.readBossBattles();
                            return 0;
                        }))
            .then(HelpfulLiteralBuilder.literal("update")
                        .executes(context -> {
                            List<String> currentBossBattles = BossBattleManager.getAllActiveBossBattles().stream().map(BossBattle::getName).collect(Collectors.toList());
                            BossBattleManager.removeBossBattles();
                            BossBattleManager.readBossBattles();
                            currentBossBattles.forEach(name -> BossBattleManager.createBossBattle(name,context));
                            return 0;
                        }))
            .then(HelpfulLiteralBuilder.literal("list")
                    .then(HelpfulLiteralBuilder.literal("active")
                        .executes(context -> {
                            context.getSource().sendMessage("Active boss battles: ");
                            BossBattleManager.getAllActiveBossBattles().forEach(bossBattle -> {
                                context.getSource().sendMessage("- "+bossBattle.getName());
                            });
                            return 0;
                        }))
                    .then(HelpfulLiteralBuilder.literal("loaded")
                            .executes(context -> {
                                context.getSource().sendMessage("Loaded boss battles: ");
                                BossBattleManager.getAllBossBattleLoaders().keySet().forEach(bossBattle -> {
                                    context.getSource().sendMessage("- "+bossBattle);
                                });
                                return 0;
                            })));
        return commandNodeBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ScriptsCommandSender wrappedSender = new ScriptsCommandSender(sender);
        execute(wrappedSender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(new ScriptsCommandSender(sender),
                                                                  String.format("/%s %s", alias, Joiner.on(' ').join(args)));
        onTabComplete(request);
        return request.getSuggestions();
    }

}
