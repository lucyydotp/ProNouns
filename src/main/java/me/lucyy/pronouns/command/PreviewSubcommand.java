package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return true;
        }

        Player player = (Player)sender;

        PronounSet[] set = pl.getPronounHandler().GetUserPronouns(player.getUniqueId());
        if (set.length == 0) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "You haven't set any pronouns yet!");
            return true;
        }
        sender.sendMessage(player.getDisplayName() + " is testing " + set[0].PossessiveAdjectival +
                " pronoun selection. If this sentence seems right, then " + set[0].Subjective +
                " will be pleased with " + set[0].PossessiveAdjectival + " choices.");
        return true;
    }
}
