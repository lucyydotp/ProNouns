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

package me.lucyy.pronouns.api.set;

/**
 * A special pronoun set used when the user has not specified a set, inheriting pronouns from another.
 * @author lucy
 */
public class UnsetPronounSet extends PronounSet {
    /**
     * @param base the pronoun set to use for placeholders
     */
    public UnsetPronounSet(PronounSet base) {
        super(base.subjective, base.objective, base.progressive, base.possessiveAdjective, base.possessivePronoun, base.reflexive, true);
    }

    /**
     * @return the string "Unset"
     */
    @Override
    public String getName() {
        return "Unset";
    }

    /**
     * @return the string "Unset"
     */
    @Override
    public String toString() {
        return "Unset";
    }
}
