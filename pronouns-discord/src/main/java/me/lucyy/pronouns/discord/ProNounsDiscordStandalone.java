package me.lucyy.pronouns.discord;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.MysqlConnectionException;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.lucypoulton.squirtgun.platform.scheduler.Task;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class ProNounsDiscordStandalone {

    private final Storage storage;
    private final JDA jda;
    private final ProNounsPlatform platform;

    public Storage getStorage() { 
        return storage;
    }

    private void cleanRoles() {
        platform.getLogger().info("Cleaning roles");
        for (Guild guild : jda.getGuilds()) {
            guild.getRoleCache().parallelStreamUnordered()
                .filter(role -> role.getName().startsWith("Pronouns ")
                    && guild.getMembers().stream().noneMatch(member -> member.getRoles().contains(role)))
                .forEach(role -> {
                    role.delete().queue();
                    platform.getLogger().info("Deleting unused role " + role.getName());
                });
        }
        platform.getLogger().info("Finished cleaning roles");
    }

    public ProNounsDiscordStandalone() throws LoginException, MysqlConnectionException, IOException {
        TestConfigHandler configHandler = new TestConfigHandler();
        jda = JDABuilder.createDefault(configHandler.getDiscordToken())
            .setChunkingFilter(ChunkingFilter.ALL)
            .setActivity(Activity.listening(configHandler.getCommandPrefix() + "pronouns - ProNouns for Discord Beta"))
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .build();
        platform = new ProNounsDiscordPlatform(jda, configHandler, this);

        ProNouns plugin = new ProNouns(platform);
        storage = new MysqlFileStorage(plugin);
        plugin.onEnable();

        platform.getTaskScheduler().start(Task.builder()
            .action(this::cleanRoles)
                .interval(36000)
            .build());
    }

    public static void main(String[] args) throws LoginException, MysqlConnectionException, IOException {
        new ProNounsDiscordStandalone();
    }
}
