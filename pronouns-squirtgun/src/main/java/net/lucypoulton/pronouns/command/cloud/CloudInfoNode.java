package net.lucypoulton.pronouns.command.cloud;

import net.kyori.adventure.text.Component;
import net.lucypoulton.pronouns.provider.CloudPronounProvider;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;

public class CloudInfoNode extends AbstractNode<PermissionHolder> {

    private final CloudPronounProvider provider;

    public CloudInfoNode(CloudPronounProvider provider) {
        super("info", "Shows info about the cloud dataset currently in use", Condition.alwaysTrue());
        this.provider = provider;
    }

    @Override
    public @Nullable Component execute(CommandContext context) {
        CloudPronounProvider.DatabaseFile database = provider.getDatabase();
        FormatProvider fmt = context.getFormat();
        return fmt.getPrefix()
            .append(fmt.formatMain("Database last updated from "))
            .append(fmt.formatAccent(database.source))
            .append(fmt.formatMain(" at "))
            .append(fmt.formatAccent(DateFormat.getDateTimeInstance().format(database.updatedAt)))
            .append(fmt.formatMain(" and contains "))
            .append(fmt.formatAccent(database.sets.size() + " sets"));
    }
}
