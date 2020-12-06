package me.lucyy.pronouns.command;

import me.lucyy.pronouns.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.command.admin.ReloadSubcommand;
import me.lucyy.pronouns.command.admin.SudoSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PronounsCommand implements CommandExecutor {

    private final ProNouns pl;
    private final HashMap<String, Subcommand> subcommands = new HashMap<>();

    private void register(Subcommand cmd) {
        subcommands.put(cmd.getName(), cmd);
    }

    public PronounsCommand(ProNouns plugin) {
        pl = plugin;
        register(new GetPronounsSubcommand(pl));
        register(new SetPronounsSubcommand(pl));
        register(new ListPronounsSubcommand(pl));
        register(new PreviewSubcommand(pl));
        register(new SudoSubcommand(this));
        register(new ReloadSubcommand(pl));

        pl.getCommand("pronouns").setTabCompleter(new PronounsTabCompleter(this));
    }
    public List<Subcommand> getUserSubcommands(CommandSender sender) {
        List<Subcommand> cmds = new ArrayList<>();
        subcommands.forEach((String label, Subcommand cmd) -> {
                    if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
                        cmds.add(cmd);
                }
        );
        return cmds;
    }

    public List<String> getSubcommands() {
        return new ArrayList<>(subcommands.keySet());
    }

    private void showDefault(CommandSender sender) {
        sender.sendMessage(ConfigHandler.GetAccentColour() + "ProNouns v" + pl.getDescription().getVersion() +
                ConfigHandler.GetMainColour() + " by " + ConfigHandler.GetAccentColour() + "__lucyy");
        sender.sendMessage(ConfigHandler.GetMainColour() + "Commands:");
        getUserSubcommands(sender).forEach(cmd -> {
            if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
                sender.sendMessage(ConfigHandler.GetMainColour() + "/pronouns "
                        + ConfigHandler.GetAccentColour() + cmd.getName()
                        + ConfigHandler.GetMainColour() + " - " + cmd.getDescription());
                }
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return onCommand(sender, sender, args);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull CommandSender target, String[] args) {
        if (args.length < 1) {
            showDefault(sender);
            return true;
        }

        Subcommand subcommand = subcommands.get(args[0]);

        if (subcommand == null) {
            showDefault(sender);
            return true;
        }

        if (subcommand.getPermission() == null || sender.hasPermission(subcommand.getPermission())) {
            if (!subcommand.execute(sender, target, Arrays.copyOfRange(args, 1, args.length))) {
                sender.sendMessage(ConfigHandler.GetPrefix() + "Usage: " + subcommand.getUsage());
            }
        } else {
            sender.sendMessage(ConfigHandler.GetPrefix() + "No permission!");
        }

        return true;
    }
}
