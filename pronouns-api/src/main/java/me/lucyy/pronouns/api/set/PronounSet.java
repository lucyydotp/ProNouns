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

    public static String capitalise(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Formats a list of pronoun sets for display, formatting using {@link PronounSet#getName()}, comma-separated.
     * @param pronounSets The sets to format
     * @return The formatted sets as a string
     */
    public static String friendlyPrintSet(Collection<PronounSet> pronounSets) {
        StringBuilder out = new StringBuilder();
        for (PronounSet set : pronounSets) {
            out.append(set.getName());
            out.append(", ");
        }
        String msg = out.toString();
        try {
            return msg.substring(0, msg.length() - 2);
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