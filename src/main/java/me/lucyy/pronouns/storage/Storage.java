package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.PronounSet;

import java.util.UUID;

public interface Storage {
    String[] GetPronouns(UUID uuid);
    void SetPronouns(UUID uuid, PronounSet set);
    void AddPronouns(UUID uuid, PronounSet set);
    void RemovePronouns(UUID uuid, PronounSet set);
    void ClearPronouns(UUID uuid);
}
