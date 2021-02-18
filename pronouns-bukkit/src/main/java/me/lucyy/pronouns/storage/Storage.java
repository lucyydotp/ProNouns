package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.api.set.PronounSet;

import java.util.List;
import java.util.UUID;

public interface Storage {
    List<String> getPronouns(UUID uuid);
    void setPronouns(UUID uuid, List<PronounSet> set);
    void clearPronouns(UUID uuid);
}
