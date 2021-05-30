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
 * A special pronoun set indicating no preference, using a second pronoun set as a base.
 * @author lucy
 */
public class AnyPronounSet extends PronounSet {
    /**
     * @param base the pronoun set to use for placeholders
     */
    public AnyPronounSet(PronounSet base) {
        super(base.getSubjective(), base.getObjective(), base.getProgressive(), base.getPossessiveAdjective(),
                base.getPossessivePronoun(), base.getReflexive());
    }

	/**
	 * @return the string "Any"
	 */
	@Override
	public String getShortenedName() {
		return "Any";
	}

    /**
     * @return the string "Any"
     */
    @Override
    public String getName() {
        return "Any";
    }

    /**
     * @return the string "Any"
     */
    @Override
    public String toString() {
        return "Any";
    }
}
