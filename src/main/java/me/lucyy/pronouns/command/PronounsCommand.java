package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        register(new PreviewSubcommand(pl));

        pl.getCommand("pronouns").setTabCompleter(new PronounsTabCompleter(this));
    }

    public List<String> getSubcommands() {
        return new ArrayList<>(subcommands.keySet());
    }

    private void showDefault(CommandSender sender) {
        sender.sendMessage(ConfigHandler.GetMainColour() + "All " +
                        ConfigHandler.GetAccentColour() + "Pronouns " +
                        ConfigHandler.GetMainColour() + "Commands");
        subcommands.forEach((String label, Subcommand cmd) ->
                sender.sendMessage(ConfigHandler.GetMainColour() + "/pronouns "
                        + ConfigHandler.GetAccentColour() + label
                        + ConfigHandler.GetMainColour() + " - " + cmd.getDescription())
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
            sender.sendMessage(ConfigHandler.GetPrefix() + "Usage: " + subcommand.getUsage());
        }

        return true;
    }
}
