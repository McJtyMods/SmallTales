package com.mcjty.smalltales.commands;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.playerdata.StoryTools;
import com.mcjty.smalltales.setup.Config;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> commands = dispatcher.register(
                Commands.literal(SmallTales.MODID)
                        .then(registerSetChapter(dispatcher))
                        .then(registerReset(dispatcher))
                        .then(registerList(dispatcher))
                        .then(registerListKnown(dispatcher))
                        .then(registerAdd(dispatcher))
        );

        dispatcher.register(Commands.literal("tales").redirect(commands));
    }

    public static ArgumentBuilder<CommandSource, ?> registerSetChapter(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("setchapter")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("page", StringArgumentType.word())
                        .executes(context -> {
                            String chapter = context.getArgument("page", String.class);
                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
                            player.getMainHandItem().getOrCreateTag().putString("chapter", chapter);
                            return 0;
                        }));
    }

    public static ArgumentBuilder<CommandSource, ?> registerAdd(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("add")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("page", StringArgumentType.word())
                        .executes(context -> {
                            String page = context.getArgument("page", String.class);
                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
                            player.getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                                story.addDiscoveredPage(page);
                                PacketSyncStory.syncStory(story, player);
                            });
                            return 0;
                        }));
    }

    public static ArgumentBuilder<CommandSource, ?> registerReset(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reset")
                .requires(cs -> cs.hasPermission(2))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrException();
                    player.getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                        story.reset();
                        PacketSyncStory.syncStory(story, player);
                    });
                    return 0;
                });
    }

    public static ArgumentBuilder<CommandSource, ?> registerList(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("list")
                .requires(cs -> cs.hasPermission(2))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrException();
                    Config.getChapters().forEach((s, text) -> player.sendMessage(new StringTextComponent("Chapter:" + s), Util.NIL_UUID));
                    return 0;
                });
    }

    public static ArgumentBuilder<CommandSource, ?> registerListKnown(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("listknown")
                .requires(cs -> cs.hasPermission(2))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrException();
                    player.getCapability(StoryTools.PLAYER_STORY).ifPresent(story -> {
                        story.getDiscoveredPages().forEach(s -> player.sendMessage(new StringTextComponent("Chapter:" + s), Util.NIL_UUID));
                    });
                    return 0;
                });
    }
}
