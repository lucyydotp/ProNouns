package me.lucyy.pronouns.discord;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.ProNounsPlatform;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.command.node.NodeBuilder;
import net.lucypoulton.squirtgun.command.node.subcommand.SubcommandNode;
import net.lucypoulton.squirtgun.discord.DiscordFormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.scheduler.Task;
import org.jetbrains.annotations.NotNull;

public class ProNounsDiscord extends ProNouns {

    private final JDA jda;

    private void cleanRoles() {
        getPlatform().getLogger().info("Cleaning roles");
        for (Guild guild : jda.getGuilds()) {
            guild.getRoleCache().parallelStreamUnordered()
                .filter(role -> role.getName().startsWith("Pronouns ")
                    && guild.getMembers().stream().noneMatch(member -> member.getRoles().contains(role)))
                .forEach(role -> {
                    role.delete().queue();
                    getPlatform().getLogger().info("Deleting unused role " + role.getName());
                });
        }
    }


    private void updateRoles() {
        getPlatform().getLogger().info("Updating roles");
        getPlatform().getLogger().info("Finished cleaning roles");
        for (Guild guild : jda.getGuilds()) {
            guild.getRoleCache().parallelStreamUnordered()
                .filter(role -> role.getName().startsWith("Pronouns "))
                .forEach(role -> role.delete().complete()); // todo ensure this doesnt block entirely
        }

        getPronounHandler().getAllUserPronouns().forEach(getPlatform()::onPronounsSet);
        getPlatform().getLogger().info("Updated roles successfully");
    }

    public ProNounsDiscord(@NotNull ProNounsPlatform platform, JDA jda) {
        super(platform);
        this.jda = jda;

        CommandNode<PermissionHolder> node = SubcommandNode.withHelp("pnda",
            "ProNouns Discord admin commands",
            Condition.hasPermission("pronouns.discord.admin"),
            new NodeBuilder<>()
                .name("clean")
                .description("Cleans all roles")
                .condition(Condition.alwaysTrue())
                .executes(ctx -> {
                    cleanRoles();
                    return Component.text("Cleaning roles");
                })
                .build(),
            new NodeBuilder<>()
                .name("update")
                .description("Deletes and resets all pronoun roles")
                .condition(Condition.alwaysTrue())
                .executes(ctx -> {
                    updateRoles();
                    return Component.text("Updating roles");
                }).build()
        );

        platform.registerCommand(node, DiscordFormatProvider.INSTANCE);

        platform.getTaskScheduler().start(Task.builder()
            .action(this::cleanRoles)
            .interval(36000)
            .build());
    }
}
