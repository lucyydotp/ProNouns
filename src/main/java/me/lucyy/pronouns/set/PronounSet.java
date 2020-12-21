package me.lucyy.pronouns.set;

public class PronounSet {
    public String Subjective;
    public String Objective;
    public String Progressive;
    public String PossessiveAdjectival;
    public String PossessivePronoun;
    public String Reflexive;
    public boolean IsPredefined;

    public static String Capitalise(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String FriendlyPrintSet(PronounSet[] pronounSets) {
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
        Subjective = subjective.toLowerCase();
        Objective = objective.toLowerCase();
        Progressive = progressive.toLowerCase();
        PossessiveAdjectival = possessiveAdjectival.toLowerCase();
        PossessivePronoun = possessivePronoun.toLowerCase();
        Reflexive = reflexive.toLowerCase();
        this.IsPredefined = isPredefined;
    }

    public String getName() {
        return Capitalise(Subjective) + "/" + Capitalise(Objective);
    }

    @Override
    public String toString() {
        return Subjective + "/" + Objective + "/" + Progressive + "/" + PossessiveAdjectival + "/" + PossessivePronoun + "/" + Reflexive;
    }
}