package me.lucyy.pronouns;

import me.lucyy.pronouns.set.AnyPronounSet;
import me.lucyy.pronouns.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;

import java.util.*;

public class PronounHandler {

    private Storage storage;

    private final HashMap<String, PronounSet> SetIndex = new HashMap<>();

    public void AddToIndex(PronounSet set) {
        set.IsPredefined = true;
        SetIndex.put(set.Subjective, set);
    }

    public PronounHandler(Storage storage) {
        this.storage = storage;
        AddToIndex(FromString("he/him/he's/his/his/himself"));
        AddToIndex(FromString("she/her/she's/her/hers/herself"));
        AddToIndex(FromString("they/them/they're/their/theirs/themself"));
        SetIndex.put("any", new AnyPronounSet(FromString("they")));
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

    public void UnsetUserPronouns(UUID uuid) {
        storage.ClearPronouns(uuid);
    }

    public PronounSet FromString(String set) throws IllegalArgumentException {
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