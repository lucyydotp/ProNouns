package me.lucyy.pronouns.command;

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
        return "Sets your pronouns";
    }

    public String getUsage() {
        return "/pronouns set <pronoun> [pronoun] ...\nExample: /pronouns set she they";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return true;
        }

        if (args.length < 1) return false;

        ArrayList<PronounSet> set = new ArrayList<>();
        for (String arg: args) {
            try {
                set.add(pl.getPronounHandler().FromString(arg));
            } catch (IllegalArgumentException _ignored) {
                sender.sendMessage("The pronoun '" + arg + "' hasn't been used by anyone on this server yet.\n" +
                        "To use it, just write it out like it's shown in /pronouns list.");
                return true;
            }
        }

        pl.getPronounHandler().SetUserPronouns(((Player) sender).getUniqueId(), set);
        sender.sendMessage("Set pronouns to " + PronounSet.FriendlyPrintSet(set.toArray(new PronounSet[0])));
        return true;
    }
}
