package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class PronounsCommand implements CommandExecutor {

    private final ProNouns pl;
    private HashMap<String, Subcommand> subcommands = new HashMap<>();

    private void register(Subcommand cmd) {
        subcommands.put(cmd.getName(), cmd);
    }

    public PronounsCommand(ProNouns plugin) {
        pl = plugin;
        register(new GetPronounsSubcommand(pl));
        register(new SetPronounsSubcommand(pl));
        register(new ListPronounsSubcommand(pl));
    }

    private void showDefault(CommandSender sender) {
        sender.sendMessage("--- All Pronoun Commands ---");
        subcommands.forEach((String label, Subcommand cmd) ->
                sender.sendMessage("/pronouns " + label + ": " + cmd.getDescription())
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 1) {
            showDefault(sender);
            return true;
        }

        Subcommand subcommand = subcommands.get(args[0]);

        if (subcommand == null) {
            showDefault(sender);
            return true;
        }

        if (!subcommand.execute(sender, Arrays.copyOfRange(args, 1, args.length))) {
            sender.sendMessage("Usage: " + subcommand.getUsage());
        }

        return true;
    }
}
