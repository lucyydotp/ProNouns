package me.lucyy.pronouns.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Subcommand {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();
    boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args);

    default List<String> tabComplete() {
        return null;
    }
}
