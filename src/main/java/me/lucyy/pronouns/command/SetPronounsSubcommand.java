package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetPronounsSubcommand implements Subcommand {
    private final ProNouns pl;
    public SetPronounsSubcommand(ProNouns plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "set";
    }

    public String getDescription() {
        return "Set your pronouns.";
    }

    public String getUsage() {
        return "/pronouns set <pronoun> [pronoun] ...\nExample: /pronouns set she they";
    }

    @Override
    public String getPermission() { return null; }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        if (!(target instanceof Player)) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "This command can only be run by a player.");
            return true;
        }

        if (args.length < 1) return false;

        ArrayList<PronounSet> set = new ArrayList<>();
        for (String arg: args) {
            try {
                PronounSet parsed = pl.getPronounHandler().FromString(arg);
                set.add(parsed);
            } catch (IllegalArgumentException _ignored) {
                sender.sendMessage(ConfigHandler.GetPrefix() + "The pronoun '" + arg +
                        "' is unrecognised.\n" +
                        "To use it, just write it out like it's shown in /pronouns list.");
                return true;
            }
        }

        pl.getPronounHandler().SetUserPronouns(((Player) target).getUniqueId(), set);
        sender.sendMessage(ConfigHandler.GetPrefix() + "Set pronouns to " +
                ConfigHandler.GetAccentColour() + PronounSet.FriendlyPrintSet(set.toArray(new PronounSet[0])));
        return true;
    }
}
