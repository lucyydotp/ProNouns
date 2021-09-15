package me.lucyy.pronouns.discord;

import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordPlatform;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ProNounsDiscordPlatform extends StandaloneDiscordPlatform implements ProNounsPlatform {

    private final JDA jda;
    private final ConfigHandler handler;
    private final ProNounsDiscordStandalone host;

    public ProNounsDiscordPlatform(JDA jda, ConfigHandler handler, ProNounsDiscordStandalone host) {
        super(jda);
        this.jda = jda;
        this.handler = handler;
        this.host = host;
    }

    @Override
    public ConfigHandler getConfigHandler() {
        return handler;
    }

    @Override
    public Storage getStorage() {
        return host.getStorage();
    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public void onPronounsSet(UUID uuid, Set<PronounSet> sets) {
        // FIXME - hardcoded. if we get an npe here then something is seriously wrong
        User user = Objects.requireNonNull(getPlayer(uuid)).discordUser();
        Guild guild = Objects.requireNonNull(jda.getGuildById(814933329215619083L));
        Member member = Objects.requireNonNull(guild.getMember(user));

        for (Role role : member.getRoles()) {
            if (role.getName().startsWith("Pronouns ")) {
                guild.removeRoleFromMember(member, role).queue();
            }
        }

        for (PronounSet set : sets) {
            Role matchingRole = guild.getRoleCache().streamUnordered()
                .filter(role -> role.getName().equals("Pronouns " + set.getName()) && role.getPermissionsRaw() == 0)
                .findFirst().orElseGet(() ->
                    guild.createRole().setName("Pronouns "+ set.getName()).setPermissions(0L).complete()
                );
            guild.addRoleToMember(member, matchingRole).queue();
        }
    }
}
