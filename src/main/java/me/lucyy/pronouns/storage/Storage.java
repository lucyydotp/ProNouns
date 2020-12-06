package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.PronounSet;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Storage {
    List<String> GetPronouns(UUID uuid);
    List<String> GetAllPronouns();
    void SetPronouns(UUID uuid, List<PronounSet> set);
    void ClearPronouns(UUID uuid);
}
