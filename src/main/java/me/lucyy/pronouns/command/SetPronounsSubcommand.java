package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.set.PronounSet;
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
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull CommandSender target, @NotNull String[] args) {
        if (!(target instanceof Player)) {
            sender.sendMessage(ConfigHandler.GetPrefix() + "This command can only be run by a player.");
            return true;
        }

        if (args.length < 1) return false;

        ArrayList<PronounSet> set = new ArrayList<>();
        for (String arg : args) {
            try {
                String[] splitArg = arg.split("/");
                if (splitArg.length == 6) {
                    PronounSet parsed = pl.getPronounHandler().FromString(arg);
                    if (!set.contains(parsed)) set.add(parsed);
                } else {
                    for (String _splitArg : splitArg) {
                        PronounSet parsed = pl.getPronounHandler().FromString(_splitArg);
                        if (!set.contains(parsed)) set.add(parsed);
                    }
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ConfigHandler.GetPrefix() + "The pronoun '" + e.getMessage() +
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
