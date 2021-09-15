package me.lucyy.pronouns.discord;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.storage.MysqlConnectionException;
import me.lucyy.pronouns.storage.MysqlFileStorage;
import me.lucyy.pronouns.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class ProNounsDiscordStandalone {

    private final Storage storage;

    public Storage getStorage() { 
        return storage;
    }

    public ProNounsDiscordStandalone() throws LoginException, MysqlConnectionException, IOException {
        TestConfigHandler handler = new TestConfigHandler();
        JDA jda = JDABuilder.createDefault(handler.getDiscordToken())
            .setChunkingFilter(ChunkingFilter.ALL)
            .setActivity(Activity.listening("~pronouns - ProNouns 2.0.0-pre6-SNAPSHOT"))
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .build();
        ProNounsPlatform platform = new ProNounsDiscordPlatform(jda, handler, this);

        ProNouns plugin = new ProNouns(platform);
        storage = new MysqlFileStorage(plugin);
        plugin.onEnable();
    }

    public static void main(String[] args) throws LoginException, MysqlConnectionException, IOException {
        new ProNounsDiscordStandalone();
    }
}
