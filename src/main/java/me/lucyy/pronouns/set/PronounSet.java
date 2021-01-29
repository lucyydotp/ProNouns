package me.lucyy.pronouns.set;

public class PronounSet {
    public String subjective;
    public String objective;
    public String progressive;
    public String possessiveAdjectival;
    public String possessivePronoun;
    public String reflexive;
    public boolean isPredefined;

    public static String capitalise(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String friendlyPrintSet(PronounSet[] pronounSets) {
        StringBuilder out = new StringBuilder();
        for (PronounSet set : pronounSets) {
            out.append(set.getName());
            out.append(", ");
        }
        String msg = out.toString();
        try {
            return msg.substring(0, msg.length() - 2);
        } catch (StringIndexOutOfBoundsException e) {
            return "Unset";
        }
    }

    public PronounSet(String subjective, String objective, String progressive, String possessiveAdjectival,
                      String possessivePronoun, String reflexive) {
        this(subjective, objective, progressive, possessiveAdjectival, possessivePronoun, reflexive, false);
    }

    public PronounSet(String subjective,
                      String objective,
                      String progressive,
                      String possessiveAdjectival,
                      String possessivePronoun,
                      String reflexive,
                      boolean isPredefined) {
        this.subjective = subjective.toLowerCase();
        this.objective = objective.toLowerCase();
        this.progressive = progressive.toLowerCase();
        this.possessiveAdjectival = possessiveAdjectival.toLowerCase();
        this.possessivePronoun = possessivePronoun.toLowerCase();
        this.reflexive = reflexive.toLowerCase();
        this.isPredefined = isPredefined;
    }

    public String getName() {
        return capitalise(subjective) + "/" + capitalise(objective);
    }

    @Override
    public String toString() {
        return subjective + "/" + objective + "/" + progressive + "/" + possessiveAdjectival + "/" + possessivePronoun + "/" + reflexive;
    }
}