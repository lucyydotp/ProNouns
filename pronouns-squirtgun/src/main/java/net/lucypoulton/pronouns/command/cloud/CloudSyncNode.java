package net.lucypoulton.pronouns.command.cloud;

import net.kyori.adventure.text.Component;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.provider.CloudPronounProvider;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import org.jetbrains.annotations.Nullable;

public class CloudSyncNode extends AbstractNode<PermissionHolder> {

    private final CloudPronounProvider provider;
    private final ConfigHandler config;

    public CloudSyncNode(CloudPronounProvider provider, ConfigHandler config) {
        super("sync", "Synchronises the dataset with the server", Condition.alwaysTrue());
        this.provider = provider;
        this.config = config;
    }

    @Override
    public @Nullable Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();
        if (!config.shouldSyncWithCloud()) {
            return fmt.getPrefix().append(fmt.formatMain("Cloud sync is disabled for this server."));
        }
        provider.update();
        return fmt.getPrefix().append(fmt.formatMain("Cloud sync started - see the console for more info."));
    }
}
