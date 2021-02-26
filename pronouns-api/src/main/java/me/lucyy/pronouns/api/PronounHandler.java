package me.lucyy.pronouns.api;

import me.lucyy.pronouns.api.set.PronounSet;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Manages users' pronouns.
 */
public interface PronounHandler {
    /**
     * Sets a user's pronouns.
     * @param uuid the player to set pronouns for
     * @param set the pronouns to set
     */
    void setUserPronouns(UUID uuid, List<PronounSet> set);

    /**
     * Gets a list of all current pronoun sets in use by players.
     * @return a list of all current pronoun sets in use by players
     */
    Collection<PronounSet> getAllPronouns();

    /**
     * Gets a user's pronouns, or an {@link me.lucyy.pronouns.api.set.UnsetPronounSet} if they haven't specified any.
     * @return the user's pronouns
     */
    Collection<PronounSet> getUserPronouns(UUID uuid);

    /**
     * Clears a user's pronouns.
     * @param uuid the player to clear pronouns for
     */
    void clearUserPronouns(UUID uuid);

    /**
     * Parses a pronoun set in string format.
     * @param input the string to parse. Can be formatted in a number of ways:
     * <ul>
     *              <li>full format as provided by {@link PronounSet#toString()}</li>
     *              <li>if the set is a registered default (currently he/she/they as of RC1), the
     *              {@link PronounSet#subjective} pronoun. Note that input is sanitised to ignore all content after a
     *              slash, making this method suitable for command processing</li>
     * </ul>
     * @return the parsed set
     * @throws IllegalArgumentException if a predefined set matching the input cannot be found and if the input is not a
     * valid set
     */
    PronounSet fromString(String input);
}
