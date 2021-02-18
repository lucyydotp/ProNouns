package me.lucyy.pronouns.command;

import lombok.Getter;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.command.admin.ReloadSubcommand;
import me.lucyy.pronouns.command.admin.SudoSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PronounsCommand implements CommandExecutor {
    @Getter
    private final ProNouns plugin;
    private final HashMap<String, Subcommand> subcommands = new HashMap<>();

    private void register(Subcommand cmd) {
        subcommands.put(cmd.getName(), cmd);
    }

    public PronounsCommand(ProNouns plugin) {
        this.plugin = plugin;
        register(new GetPronounsSubcommand(this.plugin));
        register(new SetPronounsSubcommand(this.plugin));
        register(new ListPronounsSubcommand(this.plugin));
        register(new UnsetPronounsSubcommand(this.plugin));
        register(new PreviewSubcommand(this.plugin));
        register(new SudoSubcommand(this));
        register(new ReloadSubcommand(this.plugin));

        this.plugin.getCommand("pronouns").setTabCompleter(new PronounsTabCompleter(this));
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
    public Map<String, Subcommand> getUserSubcommandsMap(CommandSender sender) {
        Map<String, Subcommand> cmds = new HashMap<>();
        subcommands.forEach((String label, Subcommand cmd) -> {
                    if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
                        cmds.put(label, cmd);
                }
        );
        return cmds;
    }

    public List<String> getSubcommands() {
        return new ArrayList<>(subcommands.keySet());
    }

    private void showDefault(CommandSender sender) {

        ConfigHandler cfg = plugin.getConfigHandler();
        sender.sendMessage(cfg.getAccentColour() + "ProNouns v" + plugin.getDescription().getVersion() +
                cfg.getMainColour() + " by " + cfg.getAccentColour() + "__lucyy");
        sender.sendMessage(cfg.getMainColour() + "Commands:");
        getUserSubcommands(sender).forEach(cmd -> {
            if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
                sender.sendMessage(cfg.getMainColour() + "/pronouns "
                        + cfg.getAccentColour() + cmd.getName()
                        + cfg.getMainColour() + " - " + cmd.getDescription());
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
                sender.sendMessage(plugin.getConfigHandler().getPrefix() + "Usage: " + subcommand.getUsage());
            }
        } else {
            sender.sendMessage(plugin.getConfigHandler().getPrefix() + "No permission!");
        }

        return true;
    }
}
