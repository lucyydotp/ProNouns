package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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

        Collection<PronounSet> sets = pl.getPronounHandler().getUserPronouns(player.getUniqueId());
        if (sets.size() == 0) {
            sender.sendMessage(pl.getConfigHandler().getPrefix() + "You haven't set any pronouns yet!");
            return true;
        }

        PronounSet set = sets.iterator().next();

        sender.sendMessage(player.getDisplayName() + " is testing " + set.possessiveAdjective +
                " pronoun selection. If this sentence seems right, then " + set.subjective +
                " will be pleased with " + set.possessiveAdjective + " choices.");
        return true;
    }
}
