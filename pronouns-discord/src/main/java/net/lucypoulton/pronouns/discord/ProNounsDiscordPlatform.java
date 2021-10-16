package net.lucypoulton.pronouns.discord;

import net.lucypoulton.pronouns.ProNounsPlatform;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordPlatform;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ProNounsDiscordPlatform extends StandaloneDiscordPlatform implements ProNounsPlatform {

    private final ConfigHandler handler;
    private final ProNounsDiscordStandalone host;

    public ProNounsDiscordPlatform(JDA jda, TestConfigHandler handler, ProNounsDiscordStandalone host) {
        super(jda, handler.getCommandPrefix());
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

    public void setRoles(UUID uuid, Set<PronounSet> sets) {
        User user = Objects.requireNonNull(getPlayer(uuid)).discordUser();

        for (Guild guild : user.getMutualGuilds()) {

            Member member = Objects.requireNonNull(guild.getMember(user));

            for (Role role : member.getRoles()) {
                if (role.getName().startsWith("Pronouns ")) {
                    guild.removeRoleFromMember(member, role).queue();
                    break;
                }
            }

            String setName = PronounSet.format(sets);

            Role matchingRole = guild.getRolesByName("Pronouns " + setName, false).stream()
                .filter(role -> role.getPermissionsRaw() == 0)
                .findFirst().orElseGet(() ->
                    guild.createRole().setName("Pronouns " + setName).setPermissions(0L).complete()
                );

            guild.addRoleToMember(member, matchingRole).queue();
        }
    }
}
