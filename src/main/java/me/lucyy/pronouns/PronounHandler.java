package me.lucyy.pronouns;

import me.lucyy.pronouns.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PronounHandler {

    private Storage storage;

    private HashMap<String, PronounSet> SetIndex = new HashMap<>();

    public PronounHandler(Storage storage) {
        this.storage = storage;
        for (String set : storage.GetAllPronouns()) FromString(set, false);
    }

    public void SetStorage(Storage storage) {
        this.storage = storage;
    }

    public void SetUserPronouns(UUID uuid, List<PronounSet> set) {
        storage.SetPronouns(uuid, set);
    }

    public List<String> GetAllPronouns() {
        List<String> pronouns = new ArrayList<>();
        SetIndex.values().forEach(set -> pronouns.add(set.toString()));
        return pronouns;
    }

    public PronounSet[] GetUserPronouns(UUID uuid) {
        List<PronounSet> pronounsList = new ArrayList<>();
        for (String pronoun : storage.GetPronouns(uuid)) {
            pronounsList.add(FromString(pronoun));
        }
        return pronounsList.toArray(new PronounSet[0]);
    }

    public PronounSet FromString(String set) {
        return FromString(set, true);
    }

    public PronounSet FromString(String set, boolean addToStorage) throws IllegalArgumentException {
        String[] pronouns = set.split("/");
        if (pronouns.length != 1 && pronouns.length != 6) throw new IllegalArgumentException(set);
        if (SetIndex.containsKey(pronouns[0].toLowerCase())) return SetIndex.get(pronouns[0].toLowerCase());

        if (pronouns.length != 6) throw new IllegalArgumentException(set);

        PronounSet newSet = new PronounSet(pronouns[0].replace(" ", ""),
                pronouns[1].replace(" ", ""),
                pronouns[2].replace(" ", ""),
                pronouns[3].replace(" ", ""),
                pronouns[4].replace(" ", ""),
                pronouns[5].replace(" ", ""));
        if (SetIndex.putIfAbsent(newSet.Subjective, newSet) == null && addToStorage) storage.AddPronounSet(newSet);
        return SetIndex.get(newSet.Subjective);
    }
}