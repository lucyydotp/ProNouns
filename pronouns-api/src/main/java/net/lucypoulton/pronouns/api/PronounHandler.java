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

package net.lucypoulton.pronouns.api;

import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.api.set.old.UnsetPronounSet;
import net.lucypoulton.squirtgun.Squirtgun;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

/**
 * Manages users' pronouns. When using sets, take care to use a sorted implementation
 * such as {@link java.util.LinkedHashSet}.
 *
 * @since 2.0.0
 */
public interface PronounHandler {
    /**
     * Sets a user's pronouns.
     *
     * @param uuid the player to set pronouns for
     * @param set  the pronouns to set
     */
    void setUserPronouns(SquirtgunPlayer player, Set<PronounSet> set);

    /**
     * Gets a list of all current pronoun sets in use by players.
     *
     * @return a list of all current pronoun sets in use by players
     */
    Set<PronounSet> getAllPronouns();

    /**
     * Gets a user's pronouns, or an {@link UnsetPronounSet} if they haven't specified any.
     *
     * @return the user's pronouns
     */
    Set<PronounSet> getPronouns(SquirtgunPlayer player);

    /**
    /**
     * Clears a user's pronouns.
     *
     * @param player the player to clear pronouns for
     */
    void clearUserPronouns(SquirtgunPlayer player);

    /**
     * Parses a string into a collection of pronoun sets. Appropriate for use in commands.
     *
     * @return the parsed sets
     * @throws IllegalArgumentException if a set can't be parsed due to either being unknown or censored
     */
    Set<PronounSet> parse(String... input);

    /**
     * Registers a {@link PronounProvider} so its pronoun sets can be used.
     *
     * @param provider the provider to register
     * @since 1.4.0
     */
    void registerProvider(PronounProvider provider);
}
