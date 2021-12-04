package net.lucypoulton.pronouns.api.set;

public class SpecialPronounSet extends PronounSet {

    private final PronounSet parent;

    private final String formatted;
    private final String toStringValue;

    public SpecialPronounSet(PronounSet parent, String formatted, String toStringValue) {
        this.parent = parent;
        this.formatted = formatted;
        this.toStringValue = toStringValue;
    }

    @Override
    public String formatted() {
        return formatted;
    }

    @Override
    public String toString() {
        return toStringValue;
    }

    @Override
    public String nameForConcatenation() {
        return formatted;
    }

    @Override
    public String subjective() {
        return parent.subjective();
    }

    @Override
    public String objective() {
        return parent.objective();
    }

    @Override
    @Deprecated
    public String progressive() {
        return parent.progressive();
    }

    @Override
    public String possessiveAdjective() {
        return parent.possessiveAdjective();
    }

    @Override
    public String possessivePronoun() {
        return parent.possessivePronoun();
    }

    @Override
    public String reflexive() {
        return parent.reflexive();
    }

    @Override
    public boolean isPlural() {
        return parent.isPlural();
    }
}
