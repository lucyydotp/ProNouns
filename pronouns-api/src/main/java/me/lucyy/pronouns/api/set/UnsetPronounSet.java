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
