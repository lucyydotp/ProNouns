package me.lucyy.pronouns.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import me.lucyy.pronouns.api.event.PronounsSetEvent;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DiscordRoleManager implements Listener {
	private final PronounsDiscord pl;
	private final DiscordSRV discord;

	public DiscordRoleManager(PronounsDiscord pl, DiscordSRV discord) {
		this.pl = pl;
		this.discord = discord;
	}

	private void applyRole(final String member, final Role role) {
		Guild guild = role.getGuild();
		//noinspection ConstantConditions - discordsrv wont provide users that arent in the guild
		final List<Role> roles = guild.getMemberById(member).getRoles();
		for (Role roleToCheck : roles) {
			if (roleToCheck.getName().startsWith("Pronouns ")) {
				guild.removeRoleFromMember(member, roleToCheck).queue();
			}
		}
		guild.addRoleToMember(member, role).queue();
	}

	public void syncRole(UUID uuid, Set<PronounSet> pronouns) {
		final String discordId = discord.getAccountLinkManager().getDiscordId(uuid);
		if (discordId == null) return;

		final Guild guild = discord.getMainGuild();
		final String roleName = "Pronouns " +  PronounSet.friendlyPrintSet(pronouns);
		Role target = null;
		final List<Role> preeexistingRoles = guild.getRolesByName(roleName, true);

		for (Role role : preeexistingRoles) {
			if (!role.getName().equals(roleName)) continue;
			if (role.getPermissionsRaw() != 0) {
				pl.getLogger().warning("Discord role '" + role.getName() + "' (id " + role.getId()
						+ ") has permissions assigned to it, not using");
				continue;
			}
			target = role;
			break;
		}

		if (target == null) {
			guild.createRole().queue(role -> role.getManager()
					.setName(roleName)
					.setPermissions(0)
					.queue((ignored) -> applyRole(discordId, role)));
			return;
		}
		applyRole(discordId, target);
	}

	@EventHandler
	public void on(final PronounsSetEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				syncRole(e.getPlayer(), e.getNewPronouns());
			}
		}.runTaskAsynchronously(pl);
	}
}
