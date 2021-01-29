package me.lucyy.pronouns.set;

public class UnsetPronounSet extends PronounSet {
    public UnsetPronounSet(PronounSet base) {
        super(base.subjective, base.objective, base.progressive, base.possessiveAdjectival, base.possessivePronoun, base.reflexive, true);
    }

    @Override
    public String getName() {
        return "Unset";
    }

    @Override
    public String toString() {
        return "Unset";
    }
}
