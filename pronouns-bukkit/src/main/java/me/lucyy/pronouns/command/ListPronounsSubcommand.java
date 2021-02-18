package me.lucyy.pronouns.command;

import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class ListPronounsSubcommand implements Subcommand {
    private final ProNouns pl;
    public ListPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "list";
    }

    public String getDescription() {
        return "Show all predefined pronoun sets.";
    }

    public String getUsage() {
        return "/pronouns list";
    }

    @Override
    public String getPermission() { return null; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        sender.sendMessage(pl.getConfigHandler().getPrefix() + "All Predefined Pronoun Sets:");
        for (PronounSet set : pl.getPronounHandler().getAllPronouns()) sender.sendMessage(set.toString());
        return true;
    }
}
