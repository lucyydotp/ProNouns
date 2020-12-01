package me.lucyy.pronouns.command;

import me.lucyy.pronouns.PronounSet;

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
