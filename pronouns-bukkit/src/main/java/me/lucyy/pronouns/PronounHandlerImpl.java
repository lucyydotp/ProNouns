package me.lucyy.pronouns;

import lombok.Getter;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.set.AnyPronounSet;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;

import java.util.*;

public class PronounHandlerImpl implements PronounHandler {

    @Getter
    private Storage storage;

    private final HashMap<String, PronounSet> SetIndex = new HashMap<>();

    public void addToIndex(PronounSet set) {
        set.isPredefined = true;
        SetIndex.put(set.subjective, set);
    }

    public PronounHandlerImpl(Storage storage) {
        this.storage = storage;
        addToIndex(fromString("he/him/he's/his/his/himself"));
        addToIndex(fromString("she/her/she's/her/hers/herself"));
        addToIndex(fromString("they/them/they're/their/theirs/themself"));
        SetIndex.put("any", new AnyPronounSet(fromString("they")));
    }

    public void setUserPronouns(UUID uuid, List<PronounSet> set) {
        storage.setPronouns(uuid, set);
    }

    public Collection<PronounSet> getAllPronouns() {
        return SetIndex.values();
    }

    public Collection<PronounSet> getUserPronouns(UUID uuid) {
        List<PronounSet> pronounsList = new ArrayList<>();
        for (String pronoun : storage.getPronouns(uuid)) {
            pronounsList.add(fromString(pronoun));
        }
        return pronounsList;
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