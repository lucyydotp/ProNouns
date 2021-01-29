package me.lucyy.pronouns.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PronounsTabCompleter implements TabCompleter {
    private final PronounsCommand cmd;

    public PronounsTabCompleter(PronounsCommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> results = new ArrayList<>();
            cmd.getUserSubcommands(sender).forEach(x -> results.add(x.getName()));
            return results;
        }

        Subcommand subcmd = cmd.getUserSubcommandsMap(sender).get(args[0]);
        if (subcmd == null) return null;
        return subcmd.tabComplete();
    }
}
