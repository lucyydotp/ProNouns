package net.lucypoulton.pronouns.api.set;

public class ParsedPronounSet extends PronounSet {

    private final String subjective, objective, progressive, possessiveAdjective, possessivePronoun, reflexive;

    public ParsedPronounSet(String subjective, String objective, String progressive,
                            String possessiveAdjective, String possessivePronoun, String reflexive) {
        this.subjective = subjective;
        this.objective = objective;
        this.progressive = progressive;
        this.possessiveAdjective = possessiveAdjective;
        this.possessivePronoun = possessivePronoun;
        this.reflexive = reflexive;
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
    public String progressive() {
        return progressive;
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
}
