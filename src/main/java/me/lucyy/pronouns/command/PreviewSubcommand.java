package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.set.PronounSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PreviewSubcommand implements Subcommand {
    private final ProNouns pl;
    public PreviewSubcommand(ProNouns plugin) {
        pl = plugin;
    }
    @Override
    public String getName() {
        return "preview";
    }

    @Override
    public String getDescription() {
        return "Test out your pronoun selection!";
    }

    @Override
    public String getUsage() {
        return "/pronouns preview";
    }

    @Override
    public String getPermission() { return null; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return true;
        }

        Player player = (Player)target;

        PronounSet[] set = pl.getPronounHandler().getUserPronouns(player.getUniqueId());
        if (set.length == 0) {
            sender.sendMessage(pl.getConfigHandler().getPrefix() + "You haven't set any pronouns yet!");
            return true;
        }
        sender.sendMessage(player.getDisplayName() + " is testing " + set[0].possessiveAdjectival +
                " pronoun selection. If this sentence seems right, then " + set[0].subjective +
                " will be pleased with " + set[0].possessiveAdjectival + " choices.");
        return true;
    }
}
