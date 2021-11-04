/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lucypoulton.pronouns;

import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.SetPronounsEvent;
import net.lucypoulton.pronouns.api.StringUtils;
import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.ParsedPronounSet;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.SpecialPronounSet;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.PluginReloadEvent;
import net.lucypoulton.squirtgun.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class PronounHandlerImpl implements PronounHandler {
    private final Storage storage;
    private final List<Pattern> filterPatterns = new ArrayList<>();

    public Storage getStorage() {
        return storage;
    }

    private final ProNouns pl;
    private final Set<PronounProvider> providers = Collections.newSetFromMap(new IdentityHashMap<>());

    public PronounHandlerImpl(ProNouns pl, Storage storage) {
        this.pl = pl;
        this.storage = storage;
        pl.getPlatform().getEventManager().register(EventHandler.executes(PluginReloadEvent.class, e -> reloadFilterPatterns()));
        reloadFilterPatterns();
    }

    private void reloadFilterPatterns() {
        filterPatterns.clear();
        for (String pattern : pl.getConfigHandler().getFilterPatterns()) {
            filterPatterns.add(Pattern.compile(pattern));
        }
    }

    @Override
    public void setUserPronouns(SquirtgunPlayer player, @NotNull Set<PronounSet> set) {
        if (pl.getPlatform().getEventManager().dispatch(new SetPronounsEvent(player, set)).successful()) {

            storage.setPronouns(player.getUuid(),
                set.stream().map(Object::toString).collect(Collectors.toCollection(LinkedHashSet::new)));
        }
    }

    @Override
    public Set<PronounSet> getAllPronouns() {
        return providers.stream().flatMap(x -> x.get().stream()).collect((toSet()));
    }

    @Override
    public @NotNull Set<PronounSet> getPronouns(SquirtgunPlayer player) {
        return storage.getPronouns(player.getUuid()).stream()
            .map(this::parse)
            .flatMap(result -> result.results().stream())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void clearUserPronouns(SquirtgunPlayer player) {
        storage.clearPronouns(player.getUuid());
    }

    private int equalityScore(List<String> input, PronounSet two) {
        if (two instanceof SpecialPronounSet && input.get(0).equalsIgnoreCase(two.nameForConcatenation())) {
            return 7;
        }
        int i = 0;
        String[] setArray = two.asArray();
        while (i < input.size() && i < 6 && input.get(i).equalsIgnoreCase(setArray[i])) {
            i++;
        }
        return i;
    }

    @Override
    public ParseResult parse(String input, boolean enforceFilter) {
        final List<String> split = StringUtils.splitSet(input);

        if (pl.getConfigHandler().filterEnabled() && enforceFilter
            && filterPatterns.stream().anyMatch(pattern -> pattern.matcher(input).find())) {
            return new ParseResult(false, Set.of(), List.of(),
                pl.getConfigHandler().formatMain("You can't use that set."));
        }

        final Set<PronounSet> out = new LinkedHashSet<>();

        int i = 0;

        while (split.size() - i >= 6) {
            out.add(new ParsedPronounSet(split.get(i),
                split.get(i + 1),
                split.get(i + 2),
                split.get(i + 3),
                split.get(i + 4),
                split.get(i + 5)
            ));
            i += 6;
        }

        final List<Set<PronounSet>> ambiguities = new ArrayList<>();
        while (i < split.size()) {
            String pronoun = split.get(i);
            int finalI = i;
            List<Pair<PronounSet, Integer>> sets = providers.stream()
                .flatMap(provider -> provider.get().stream())
                .filter(set -> set.nameForConcatenation().equalsIgnoreCase(pronoun))
                .map(set -> new Pair<>(set, equalityScore(split.subList(finalI, split.size()), set)))
                .collect(toList());

            int max = sets.stream().mapToInt(Pair::value).max().orElse(0);
            if (max == 0) {
                i++;
                continue;
            }
            Set<PronounSet> potentialSets = sets.stream().filter(x -> x.value() == max)
                .map(Pair::key)
                .collect(Collectors.toSet());
            i += max;
            if (potentialSets.size() != 1) {
                ambiguities.add(potentialSets);
            }
            out.add(potentialSets.stream().findFirst().orElseThrow());
        }

        return new ParseResult(!out.isEmpty(), out, ambiguities, null);
    }

    @Override
    public void registerProvider(PronounProvider provider) {
        this.providers.add(provider);
    }
}