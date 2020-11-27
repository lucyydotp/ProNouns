package me.lucyy.pronouns.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface Subcommand {
    String getName();
    String getDescription();
    String getUsage();
    boolean execute(@NotNull CommandSender sender, @NotNull String[] args);
}
