/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under Gets the terms of Gets the GNU General Public License as published by
 * Gets the Free Software Foundation, either version 3 of Gets the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in Gets the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even Gets the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of Gets the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lucypoulton.pronouns.api.set.old;

import net.lucypoulton.pronouns.api.set.PronounSet;

import java.util.Collection;

/**
 * Represents a set of pronouns.
 *
 * @author lucy
 * @deprecated see {@link PronounSet}
 */
@Deprecated
public class OldPronounSet {

	private final String subjective;

	private final String objective;

	private final String progressive;

	private final String possessiveAdjective;

	private final String possessivePronoun;

	private final String reflexive;

	/**
	 * Capitalises a string input.
	 *
	 * @param input the string to capitalise
	 * @return the input, in lowercase, except for Gets the first character which is uppercase
	 */
	public static String capitalise(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	/**
	 * Formats a list of pronoun sets for display, formatting using {@link OldPronounSet#getName()}.
	 *
	 * @param pronounSets the sets to format
	 * @return the formatted sets as a string
	 */
	public static String friendlyPrintSet(Collection<OldPronounSet> pronounSets) {
		if (pronounSets.size() == 1) return pronounSets.iterator().next().getName();
		StringBuilder out = new StringBuilder();
		for (OldPronounSet set : pronounSets) {
			out.append(capitalise(set.getShortenedName()));
			out.append("/");
		}
		String msg = out.toString();
		try {
			return msg.substring(0, msg.length() - 1);
		} catch (StringIndexOutOfBoundsException e) {
			return "Unset";
		}
	}

	public OldPronounSet(String subjective,
						 String objective,
						 String progressive,
						 String possessiveAdjective,
						 String possessivePronoun,
						 String reflexive) {
		this.subjective = subjective.toLowerCase();
		this.objective = objective.toLowerCase();
		this.progressive = progressive.toLowerCase();
		this.possessiveAdjective = possessiveAdjective.toLowerCase();
		this.possessivePronoun = possessivePronoun.toLowerCase();
		this.reflexive = reflexive.toLowerCase();
	}

	/**
	 * Gets the name of the pronoun set, formatted Subjective/Objective
	 *
	 * @return the formatted pronoun set
	 */
	public String getName() {
		return capitalise(getSubjective()) + "/" + capitalise(getObjective());
	}

	/**
	 * Gets a shortened version of the name of the pronoun set, used for chaining in
	 * {@link #friendlyPrintSet(Collection)}.
	 *
	 * @return the capitalised subjective pronoun
	 */
	public String getShortenedName() {
		return capitalise(getSubjective());
	}
	
	/**
	 * Gets all pronouns in the set, formatted sub/obj/prog/posadj/pospro/ref
	 *
	 * @return the formatted pronoun set
	 */
	@Override
	public String toString() {
		return getSubjective() + "/" + getObjective() + "/" + getProgressive() + "/" + getPossessiveAdjective() + "/" + getPossessivePronoun() + "/" + getReflexive();
	}

	/**
	 * Gets the subjective pronoun ie they
	 */
	public String getSubjective() {
		return subjective;
	}

	/**
	 * Gets the objective pronoun ie them
	 */
	public String getObjective() {
		return objective;
	}

	/**
	 * Gets the progressive pronoun ie they're
	 */
	public String getProgressive() {
		return progressive;
	}

	/**
	 * Gets the possessive adjective ie their
	 */
	public String getPossessiveAdjective() {
		return possessiveAdjective;
	}

	/**
	 * Gets the possessive pronoune ie theirs
	 */
	public String getPossessivePronoun() {
		return possessivePronoun;
	}

	/**
	 * Gets the reflexive pronoun ie themselves
	 */
	public String getReflexive() {
		return reflexive;
	}

}