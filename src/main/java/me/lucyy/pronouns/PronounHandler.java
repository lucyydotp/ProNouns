package me.lucyy.pronouns;

import me.lucyy.pronouns.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PronounHandler {

    private static Storage storage;

    private static HashMap<String, PronounSet> SetIndex = new HashMap<>();

    static {
        SetIndex.put("he/him", new PronounSet("he", "him",
                "he's", "his", "his", "himself"));
        SetIndex.put("she/her", new PronounSet("she", "her",
                "she's", "her", "hers", "herself"));
        SetIndex.put("they/them", new PronounSet("they", "them",
                "they're", "their", "theirs", "themself"));
    }

    public void SetStorage(Storage storage) {
        this.storage = storage;
    }

    public static List<String> GetAllPronouns() {
        List<String> pronouns = new ArrayList<>();
        SetIndex.values().forEach(set -> pronouns.add(set.toString()));
        return pronouns;
    }

    public static PronounSet fromString(String set) {
        String[] pronouns = set.split("/");
        if ( pronouns.length < 2 ) throw new IllegalArgumentException(set);
        if ( SetIndex.containsKey(pronouns[0] + "/" + pronouns[1]) ) return SetIndex.get(set);

        if ( pronouns.length != 6 ) throw new IllegalArgumentException(set);

        PronounSet newSet = new PronounSet(pronouns[0], pronouns[1], pronouns[2], pronouns[3], pronouns[4], pronouns[5]);
        SetIndex.putIfAbsent(newSet.getName(), newSet);
        return SetIndex.get(set);
    }
}