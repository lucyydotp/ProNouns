package net.lucypoulton.pronouns.provider;

import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.SpecialPronounSet;

import java.util.Set;

public class BuiltinPronounProvider implements PronounProvider {

    private final PronounSet baseSet = PronounSet.parse("they/them/they're/their/theirs/themself");
    private final Set<PronounSet> sets = Set.of(
        baseSet,
        PronounSet.parse("he/him/he's/his/his/himself"),
        PronounSet.parse("she/her/she's/her/hers/herself"),
        new SpecialPronounSet(baseSet, "Any", "Any"),
        new SpecialPronounSet(baseSet, "Ask", "Ask")
    );

    @Override
    public Set<PronounSet> get() {
        return sets;
    }
}
