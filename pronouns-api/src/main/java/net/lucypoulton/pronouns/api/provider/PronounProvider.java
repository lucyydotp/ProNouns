package net.lucypoulton.pronouns.api.provider;

import net.lucypoulton.pronouns.api.set.PronounSet;

import java.util.Set;

@FunctionalInterface
public interface PronounProvider {
    Set<PronounSet> get();
}
