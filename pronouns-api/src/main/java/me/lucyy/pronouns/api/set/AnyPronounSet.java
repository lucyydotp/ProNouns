package me.lucyy.pronouns.api.set;

/**
 * A special pronoun set indicating no preference, using a second pronoun set as a base.
 * @author lucy
 */
public class AnyPronounSet extends PronounSet {
    public AnyPronounSet(PronounSet base) {
        super(base.subjective, base.objective, base.progressive, base.possessiveAdjective, base.possessivePronoun, base.reflexive, true);
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
