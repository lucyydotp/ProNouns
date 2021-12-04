package net.lucypoulton.pronouns;

import com.google.common.io.CharStreams;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.command.ClearOtherNode;
import net.lucypoulton.pronouns.command.ClearPronounsNode;
import net.lucypoulton.pronouns.command.ListPronounsNode;
import net.lucypoulton.pronouns.command.PreviewNode;
import net.lucypoulton.pronouns.command.SetOtherNode;
import net.lucypoulton.pronouns.command.SetPronounsNode;
import net.lucypoulton.pronouns.command.ShowPronounsNode;
import net.lucypoulton.pronouns.command.cloud.CloudInfoNode;
import net.lucypoulton.pronouns.command.cloud.CloudSyncNode;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.listener.CloudUploadListener;
import net.lucypoulton.pronouns.listener.FilteredSetAttemptListener;
import net.lucypoulton.pronouns.listener.JoinLeaveListener;
import net.lucypoulton.pronouns.provider.BuiltinPronounProvider;
import net.lucypoulton.pronouns.provider.CloudPronounProvider;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.command.node.NodeBuilder;
import net.lucypoulton.squirtgun.command.node.PluginInfoNode;
import net.lucypoulton.squirtgun.command.node.subcommand.SubcommandNode;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.PluginReloadEvent;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import net.lucypoulton.squirtgun.update.PolymartUpdateChecker;
import net.lucypoulton.squirtgun.util.SemanticVersion;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ProNouns extends SquirtgunPlugin<ProNounsPlatform> {

    private PronounHandlerImpl pronounHandler;

    private final SemanticVersion version;

    public ProNouns(@NotNull ProNounsPlatform platform) {
        super(platform);
        String version;
        try (InputStream stream = ProNouns.class.getResourceAsStream("/pronouns-version.txt")) {
            version = CharStreams.toString(new InputStreamReader(Objects.requireNonNull(stream)));
        } catch (IOException e) {
            e.printStackTrace();
            version = "ERROR - see console";
        }
        this.version = SemanticVersion.parse(version);
    }

    @Override
    public @NotNull String getPluginName() {
        return "ProNouns";
    }

    @Override
    public @NotNull SemanticVersion getPluginVersion() {
        return version;
    }

    @Override
    public @NotNull String[] getAuthors() {
        return new String[]{"__lucyy"};
    }


    @Override
    public void onEnable() {
        pronounHandler = new PronounHandlerImpl(this, getPlatform().getStorage());

        CloudPronounProvider provider = new CloudPronounProvider(this);

        pronounHandler.registerProvider(new BuiltinPronounProvider());
        pronounHandler.registerProvider(provider);

        getPlatform().getEventManager().register(new CloudUploadListener(this, provider));
        getPlatform().getEventManager().register(EventHandler.executes(PluginReloadEvent.class, e -> getPlatform().reloadConfig()));

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
                    reload();
                    return x.getFormat().getPrefix().append(x.getFormat().formatMain("Reloaded"));
                }).build(),
            SubcommandNode.withHelp("cloud",
                "Cloud admin commands",
                Condition.hasPermission("pronouns.cloud"),
                new CloudInfoNode(provider),
                new CloudSyncNode(provider, getConfigHandler())
            )
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

        getPlatform().getEventManager().register(new JoinLeaveListener(this));
        getPlatform().getEventManager().register(new FilteredSetAttemptListener(this.getPlatform()));
    }

    public PronounHandler getPronounHandler() {
        return pronounHandler;
    }

    public ConfigHandler getConfigHandler() {
        return getPlatform().getConfigHandler();
    }
}
