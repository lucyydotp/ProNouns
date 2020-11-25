package me.lucyy.pronouns;

public class PronounSet {
    public String Subjective;
    public String Objective;
    public String Progressive;
    public String PossessiveAdjectival;
    public String PossessivePronoun;
    public String Reflexive;

    public static String Capitalise(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public PronounSet(String subjective, String objective, String progressive, String possessiveAdjectival, String possessivePronoun, String reflexive) {
        Subjective = subjective;
        Objective = objective;
        Progressive = progressive;
        PossessiveAdjectival = possessiveAdjectival;
        PossessivePronoun = possessivePronoun;
        Reflexive = reflexive;
    }

    public String getName() {
        return Capitalise(Subjective) + "/" + Capitalise(Objective);
    }

    @Override
    public String toString() {
        return Subjective + "/" + Objective + "/" + Progressive + "/" + PossessiveAdjectival + "/" + PossessivePronoun + "/" + Reflexive;
    }
}