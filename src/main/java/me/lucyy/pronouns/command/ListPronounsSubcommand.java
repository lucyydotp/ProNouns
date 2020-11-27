package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        sender.sendMessage("--- All Predefined Pronoun Sets ---");
        for (String set : pl.getPronounHandler().GetAllPronouns()) sender.sendMessage(set);
        return true;
    }
}
