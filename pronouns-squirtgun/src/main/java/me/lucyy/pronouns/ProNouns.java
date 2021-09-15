package me.lucyy.pronouns;

import com.google.common.io.CharStreams;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.command.*;
import me.lucyy.pronouns.config.ConfigHandler;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.command.node.NodeBuilder;
import net.lucypoulton.squirtgun.command.node.PluginInfoNode;
import net.lucypoulton.squirtgun.command.node.subcommand.SubcommandNode;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import net.lucypoulton.squirtgun.update.PolymartUpdateChecker;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ProNouns extends SquirtgunPlugin<ProNounsPlatform> {

    private PronounHandlerImpl pronounHandler;

    private final String version;

    public ProNouns(@NotNull ProNounsPlatform platform) {
        super(platform);
        String version;
        try (InputStream stream = ProNouns.class.getResourceAsStream("/pronouns-version.txt")) {
            version = CharStreams.toString(new InputStreamReader(Objects.requireNonNull(stream)));
        } catch (IOException e) {
            e.printStackTrace();
            version = "ERROR - see console";
        }
        this.version = version;
    }

    @Override
    public @NotNull String getPluginName() {
        return "ProNouns";
    }

    @Override
    public @NotNull String getPluginVersion() {
        return version;
    }

    @Override
    public @NotNull String[] getAuthors() {
        return new String[]{"__lucyy"};
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
                Condition.alwaysTrue(),
                new SetPronounsNode(this),
                new ShowPronounsNode(getPlatform(), getPronounHandler()),
                new PreviewNode(this),
                new ClearPronounsNode(pronounHandler),
                new ListPronounsNode(pronounHandler),
                new PluginInfoNode("version", this),
                new SetOtherNode(this),
                new ClearOtherNode(this),
                new NodeBuilder<>()
                        .name("reload")
                        .description("Reloads the plugin.")
                        .condition(Condition.hasPermission("pronouns.admin"))
                        .executes(x -> {
                            getPlatform().reloadConfig();
                            return x.getFormat().getPrefix()
                                    .append(x.getFormat().formatMain("Reloaded"));
                        }).build()
        );
        getPlatform().registerCommand(rootNode, getConfigHandler());

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
