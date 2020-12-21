package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.set.PronounSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UnsetPronounsSubcommand implements Subcommand {
    private final ProNouns pl;

    public UnsetPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "unset";
    }

    public String getDescription() {
        return "Clear your pronouns.";
    }

    public String getUsage() {
        return "/pronouns unset";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        if (!(target instanceof Player)) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "This command can only be run by a player.");
            return true;
        }


        pl.getPronounHandler().UnsetUserPronouns(((Player) target).getUniqueId());
        sender.sendMessage(ConfigHandler.GetPrefix() + "Cleared pronouns");
        return true;
    }
}
