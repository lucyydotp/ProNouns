package me.lucyy.pronouns;

import me.lucyy.pronouns.set.AnyPronounSet;
import me.lucyy.pronouns.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;

import java.util.*;

public class PronounHandler {

    private Storage storage;

    private final HashMap<String, PronounSet> SetIndex = new HashMap<>();

    public void addToIndex(PronounSet set) {
        set.isPredefined = true;
        SetIndex.put(set.subjective, set);
    }

    public PronounHandler(Storage storage) {
        this.storage = storage;
        addToIndex(fromString("he/him/he's/his/his/himself"));
        addToIndex(fromString("she/her/she's/her/hers/herself"));
        addToIndex(fromString("they/them/they're/their/theirs/themself"));
        SetIndex.put("any", new AnyPronounSet(fromString("they")));
    }

    public Storage getStorage() {
        return storage;
    }
    public void SetStorage(Storage storage) {
        this.storage = storage;
    }

    public void setUserPronouns(UUID uuid, List<PronounSet> set) {
        storage.setPronouns(uuid, set);
    }

    public Collection<PronounSet> getAllPronouns() {
        return SetIndex.values();
    }

    public PronounSet[] getUserPronouns(UUID uuid) {
        List<PronounSet> pronounsList = new ArrayList<>();
        for (String pronoun : storage.getPronouns(uuid)) {
            pronounsList.add(fromString(pronoun));
        }
        return pronounsList.toArray(new PronounSet[0]);
    }

    public void clearUserPronouns(UUID uuid) {
        storage.clearPronouns(uuid);
    }

    public PronounSet fromString(String set) throws IllegalArgumentException {
        String[] pronouns = set.split("/");
        if (pronouns.length > 6) throw new IllegalArgumentException(set);

        PronounSet retrieved = SetIndex.getOrDefault(pronouns[0].toLowerCase(), null);

        if (retrieved != null) return retrieved;

        if (pronouns.length != 6) throw new IllegalArgumentException(set);

        return new PronounSet(pronouns[0].replace(" ", ""),
                pronouns[1].replace(" ", ""),
                pronouns[2].replace(" ", ""),
                pronouns[3].replace(" ", ""),
                pronouns[4].replace(" ", ""),
                pronouns[5].replace(" ", ""));
    }
}