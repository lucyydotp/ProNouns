package me.lucyy.pronouns.command;

import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        ConfigHandler cfg = pl.getConfigHandler();
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(cfg.getPrefix() + "Please specify a username.");
            return true;
        }

        if (args.length > 0) {
            commandTarget = Bukkit.getPlayer(args[0]);
        } else commandTarget = (Player)sender;

        if (commandTarget == null) {
            sender.sendMessage(cfg.getPrefix() + "Player '" + args[0] + cfg.getMainColour() + "' could not be found.");
            return true;
        }

        sender.sendMessage(cfg.getPrefix() + commandTarget.getDisplayName() + cfg.getMainColour() +
                "'s pronouns are " + cfg.getAccentColour() +
                PronounSet.friendlyPrintSet(pl.getPronounHandler().getUserPronouns(commandTarget.getUniqueId())));

        return true;
    }

    @Override
    public List<String> tabComplete() {
        List<String> names = new ArrayList<>();
        for (Player player :Bukkit.getOnlinePlayers()) names.add(player.getName());
        return names;
    }
}
