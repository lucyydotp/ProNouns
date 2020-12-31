package me.lucyy.pronouns.command.admin;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.command.Subcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.lucyy.pronouns.config.ConfigHandler;

public class ReloadSubcommand implements Subcommand {

    private final ProNouns pl;

    public ReloadSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "ADMIN - reloads config";
    }

    @Override
    public String getUsage() {
        return "/pronouns reload";
    }

    @Override
    public String getPermission() {
        return "pronouns.admin";
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final CommandSender target, @NotNull final String[] args) {
        pl.reloadConfig();
        sender.sendMessage(ConfigHandler.GetPrefix() + "Reloaded");
        return true;
    }
}
