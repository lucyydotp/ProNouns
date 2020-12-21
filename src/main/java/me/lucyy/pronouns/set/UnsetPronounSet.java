package me.lucyy.pronouns.set;

public class UnsetPronounSet extends PronounSet {
    public UnsetPronounSet(PronounSet base) {
        super(base.Subjective, base.Objective, base.Progressive, base.PossessiveAdjectival, base.PossessivePronoun, base.Reflexive, true);
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
