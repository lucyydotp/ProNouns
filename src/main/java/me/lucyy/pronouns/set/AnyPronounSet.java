package me.lucyy.pronouns.set;

public class AnyPronounSet extends PronounSet {
    public AnyPronounSet(PronounSet base) {
        super(base.subjective, base.objective, base.progressive, base.possessiveAdjectival, base.possessivePronoun, base.reflexive, true);
    }

    @Override
    public String getName() {
        return "Any";
    }

    @Override
    public String toString() {
        return "Any";
    }
}
