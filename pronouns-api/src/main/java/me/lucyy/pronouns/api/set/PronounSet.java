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

import java.util.Collection;

/**
 * Represents a set of pronouns.
 * @author lucy
 */
public class PronounSet {

    /**
     * The subjective pronoun ie they
     */
    public String subjective;

    /**
     * The objective pronoun ie them
     */
    public String objective;

    /**
     * The progressive pronoun ie they're
     */
    public String progressive;

    /**
     * The possessive adjective ie their
     */
    public String possessiveAdjective;

    /**
     * The possessive pronoune ie theirs
     */
    public String possessivePronoun;

    /**
     * The reflexive pronoun ie themselves
     */
    public String reflexive;

    /**
     * Whether the pronoun set is predefined by the plugin
     */
    public boolean isPredefined;

    /**
     * Capitalises a string input.
     * @param input the string to capitalise
     * @return the input, in lowercase, except for the first character which is uppercase
     */
    public static String capitalise(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Formats a list of pronoun sets for display, formatting using {@link PronounSet#getName()}, comma-separated.
     * @param pronounSets The sets to format
     * @return The formatted sets as a string
     */
    public static String friendlyPrintSet(Collection<PronounSet> pronounSets) {
        if (pronounSets.size() == 1) return pronounSets.iterator().next().getName();
        StringBuilder out = new StringBuilder();
        for (PronounSet set : pronounSets) {
            out.append(capitalise(set.subjective));
            out.append("/");
        }
        String msg = out.toString();
        try {
            return msg.substring(0, msg.length() - 1);
        } catch (StringIndexOutOfBoundsException e) {
            return "Unset";
        }
    }

    public PronounSet(String subjective, String objective, String progressive, String possessiveAdjective,
                      String possessivePronoun, String reflexive) {
        this(subjective, objective, progressive, possessiveAdjective, possessivePronoun, reflexive, false);
    }

    PronounSet(String subjective,
                      String objective,
                      String progressive,
                      String possessiveAdjective,
                      String possessivePronoun,
                      String reflexive,
                      boolean isPredefined) {
        this.subjective = subjective.toLowerCase();
        this.objective = objective.toLowerCase();
        this.progressive = progressive.toLowerCase();
        this.possessiveAdjective = possessiveAdjective.toLowerCase();
        this.possessivePronoun = possessivePronoun.toLowerCase();
        this.reflexive = reflexive.toLowerCase();
        this.isPredefined = isPredefined;
    }

    /**
     * Gets the name of the pronoun set, formatted Subjective/Objective
     * @return the formatted pronoun set
     */
    public String getName() {
        return capitalise(subjective) + "/" + capitalise(objective);
    }

    /**
     * Gets all pronouns in the set, formatted sub/obj/prog/posadj/pospro/ref
     * @return the formatted pronoun set
     */
    @Override
    public String toString() {
        return subjective + "/" + objective + "/" + progressive + "/" + possessiveAdjective + "/" + possessivePronoun + "/" + reflexive;
    }
}