package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GetPronounsSubcommand implements Subcommand {
    private final ProNouns pl;
    public GetPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "show";
    }

    public String getDescription() {
        return "Shows your, or another player's, pronouns.";
    }

    public String getUsage() {
        return "/pronouns show [username]";
    }

    @Override
    public String getPermission() { return null; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        Player commandTarget;

        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "Please specify a username.");
            return true;
        }

        if (args.length > 0) {
            commandTarget = Bukkit.getPlayer(args[0]);
        } else commandTarget = (Player)sender;

        if (commandTarget == null) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "Player '" + args[0] + "' could not be found.");
            return true;
        }

        sender.sendMessage(ConfigHandler.GetPrefix() + commandTarget.getDisplayName() + "'s pronouns are " +
                ConfigHandler.GetAccentColour() + PronounSet.FriendlyPrintSet(pl.getPronounHandler().GetUserPronouns(commandTarget.getUniqueId())));

        return true;
    }
}
