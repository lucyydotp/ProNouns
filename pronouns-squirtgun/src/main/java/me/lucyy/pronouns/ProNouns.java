package me.lucyy.pronouns;

import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.command.*;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.command.node.NodeBuilder;
import me.lucyy.squirtgun.command.node.PluginInfoNode;
import me.lucyy.squirtgun.command.node.subcommand.SubcommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;
import me.lucyy.squirtgun.update.PolymartUpdateChecker;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public class ProNouns extends SquirtgunPlugin<ProNounsPlatform> {

    private PronounHandlerImpl pronounHandler;

    public ProNouns(@NotNull ProNounsPlatform platform) {
        super(platform);
    }

    @Override
    public @NotNull String getPluginName() {
        return "ProNouns";
    }

    @Override
    public @NotNull String getPluginVersion() {
        return "VERSION"; // TODO
    }

    @Override
    public @NotNull String[] getAuthors() {
        return new String[] {"__lucyy"};
    }


    @Override
    public void onEnable() {
        pronounHandler = new PronounHandlerImpl(this, getPlatform().getStorage());

        for (String set : getConfigHandler().getPredefinedSets()) {
            try {
                pronounHandler.addToIndex(pronounHandler.fromString(set));
            } catch (IllegalArgumentException e) {
                getPlatform().getLogger().warning("'" + set + "' is an invalid set, ignoring");
            }
        }

        final CommandNode<PermissionHolder> rootNode = SubcommandNode.withHelp("pronouns",
                "ProNouns root command",
                null,
                new SetPronounsNode(this),
                new ShowPronounsNode(getPlatform(), getPronounHandler()),
                new PreviewNode(this),
                new ClearPronounsNode(pronounHandler),
                new ListPronounsNode(pronounHandler),
                new PluginInfoNode<>("version", this),
                new NodeBuilder<>()
                        .name("reload")
                        .description("Reloads the plugin.")
                        .permission("pronouns.admin")
                        .executes(x -> {
                            getPlatform().reloadConfig();
                            return x.getFormat().getPrefix()
                                    .append(x.getFormat().formatMain("Reloaded"));
                        }).build()
        );
        getPlatform().registerCommand(rootNode);

        if (getConfigHandler().checkForUpdates()) {
            new PolymartUpdateChecker(this,
                    921,
                    getConfigHandler().getPrefix()
                            .append(getConfigHandler().formatMain("A new version of ProNouns is available!\nFind it at "))
                            .append(getConfigHandler().formatAccent("https://lucyy.me/pronouns",
                                    new TextDecoration[]{TextDecoration.UNDERLINED})
                                    .clickEvent(ClickEvent.openUrl("https://lucyy.me/pronouns"))),
                    "pronouns.admin");
        } else {
            getPlatform().getLogger().warning("Update checking is disabled. You might be running an old version!");
        }

        getPlatform().registerEventListener(new JoinLeaveListener(this));
    }

    public PronounHandler getPronounHandler() {
        return pronounHandler;
    }

    public ConfigHandler getConfigHandler() {
        return getPlatform().getConfigHandler();
    }
}
