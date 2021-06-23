/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.pronouns.api;

import me.lucyy.pronouns.api.set.PronounSet;

import java.util.*;

/**
 * Manages users' pronouns.
 */
public interface PronounHandler {
	/**
	 * Sets a user's pronouns.
	 *
	 * @param uuid the player to set pronouns for
	 * @param set  the pronouns to set
	 */
	void setUserPronouns(UUID uuid, Set<PronounSet> set);

	/**
	 * Gets a list of all current pronoun sets in use by players.
	 *
	 * @return a list of all current pronoun sets in use by players
	 */
	Set<PronounSet> getAllPronouns();

	/**
	 * Gets a user's pronouns, or an {@link me.lucyy.pronouns.api.set.UnsetPronounSet} if they haven't specified any.
	 *
	 * @return the user's pronouns
	 */
	Set<PronounSet> getPronouns(UUID uuid);

	/**
	 * @deprecated use {@link #getPronouns(UUID)}
	 */
	@Deprecated
	default Collection<PronounSet> getUserPronouns(UUID uuid) {
		return getPronouns(uuid);
	}

	/**
	 * Clears a user's pronouns.
	 *
	 * @param uuid the player to clear pronouns for
	 */
	void clearUserPronouns(UUID uuid);

	/**
	 * Parses a pronoun set in string format.
	 *
	 * @param input the string to parse. Can be formatted in a number of ways:
	 *              <ul>
	 *                           <li>full format as provided by {@link PronounSet#toString()}</li>
	 *                           <li>if the set is a registered default (currently he/she/they/any), the
	 *                           subjective pronoun. Note that input is sanitised to ignore all content after a
	 *                           slash, making this method suitable for command processing</li>
	 *              </ul>
	 * @return the parsed set
	 * @throws IllegalArgumentException if a predefined set matching the input cannot be found and if the input is not a
	 *                                  valid set
	 */
	PronounSet fromString(String input);

	/**
	 * Parses a string into a collection of pronoun sets. Appropriate for use in commands.
	 *
	 * @return the parsed sets
	 * @throws IllegalArgumentException if a set can't be parsed due to either being unknown or censored
	 */
	Set<PronounSet> parseSets(String... input);

	/**
	 * Get all users and the pronouns they have set.
	 *
	 * @return a map of users and pronouns
	 */
	Map<UUID, Set<PronounSet>> getAllUserPronouns();
}
