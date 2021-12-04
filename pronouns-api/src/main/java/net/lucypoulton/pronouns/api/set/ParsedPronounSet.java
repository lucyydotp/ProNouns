package net.lucypoulton.pronouns.api.set;

public class ParsedPronounSet extends PronounSet {

    private final String subjective, objective, possessiveAdjective, possessivePronoun, reflexive;
    private final boolean isPlural;

    public ParsedPronounSet(String subjective, String objective,
                            String possessiveAdjective, String possessivePronoun, String reflexive, boolean isPlural) {
        this.subjective = subjective;
        this.objective = objective;
        this.possessiveAdjective = possessiveAdjective;
        this.possessivePronoun = possessivePronoun;
        this.reflexive = reflexive;
        this.isPlural = isPlural;
    }

    @Override
    public String subjective() {
        return subjective;
    }

    @Override
    public String objective() {
        return objective;
    }

    @Override
    @Deprecated
    public String progressive() {
        return subjective + (isPlural ? "'re" : "'s");
    }

    @Override
    public String possessiveAdjective() {
        return possessiveAdjective;
    }

    @Override
    public String possessivePronoun() {
        return possessivePronoun;
    }

    @Override
    public String reflexive() {
        return reflexive;
    }

    @Override
    public boolean isPlural() {
        return isPlural;
    }
}
