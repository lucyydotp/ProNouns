package me.lucyy.pronouns.set;

public class AnyPronounSet extends PronounSet {
    public AnyPronounSet(PronounSet base) {
        super(base.Subjective, base.Objective, base.Progressive, base.PossessiveAdjectival, base.PossessivePronoun, base.Reflexive, true);
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
