package me.lucyy.pronouns.api.event;

import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;
import java.util.UUID;

/**
 * An event that is called whenever a player sets their pronouns.
 */
@SuppressWarnings("unused")
public class PronounsSetEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final Collection<PronounSet> newPronouns;
	private final UUID player;

	public PronounsSetEvent(UUID player, Collection<PronounSet> newPronouns) {
		this.player = player;
		this.newPronouns = newPronouns;
	}

	@Override
	public HandlerList getHandlers() {
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
	public Collection<PronounSet> getNewPronouns() {
		return newPronouns;
	}
}
