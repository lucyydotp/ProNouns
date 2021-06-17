package me.lucyy.pronouns.bukkit.event;

import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * An event that is called whenever a player sets their pronouns.
 */
@SuppressWarnings("unused")
public class PronounsSetEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final Set<PronounSet> newPronouns;
	private final UUID player;

	public PronounsSetEvent(UUID player, Set<PronounSet> newPronouns) {
		this.player = player;
		this.newPronouns = newPronouns;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * @return the UUID of the player whose pronouns have changed
	 */
	public UUID getPlayer() {
		return player;
	}

	/**
	 * @return the player's new pronoun sets. If they have been cleared, this will be empty.
	 */
	public Set<PronounSet> getNewPronouns() {
		return newPronouns;
	}
}
