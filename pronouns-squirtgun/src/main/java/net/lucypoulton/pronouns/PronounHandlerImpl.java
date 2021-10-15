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
import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.provider.BuiltinPronounProvider;
import net.lucypoulton.pronouns.provider.CloudPronounProvider;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class PronounHandlerImpl implements PronounHandler {
    private final Storage storage;

    public Storage getStorage() {
        return storage;
    }

    private final ProNouns pl;
    private final Set<PronounProvider> providers = Collections.newSetFromMap(new IdentityHashMap<>());

    public PronounHandlerImpl(ProNouns pl, Storage storage) {
        this.pl = pl;
        this.storage = storage;
        providers.add(new BuiltinPronounProvider());
        providers.add(new CloudPronounProvider(pl));
    }

    @Override
    public void setUserPronouns(SquirtgunPlayer player, Set<PronounSet> set) {
        if (pl.getPlatform().getEventManager().dispatch(new SetPronounsEvent(player, set)).successful()) {
            storage.setPronouns(player.getUuid(), set);
        }
    }

    public Set<PronounSet> getAllPronouns() {

    }

    public Set<OldPronounSet> getPronouns(UUID uuid) {
        Set<OldPronounSet> pronounsList = new LinkedHashSet<>();
        for (String pronoun : storage.getPronouns(uuid)) {
            try {
                pronounsList.add(fromString(pronoun));
            } catch (IllegalArgumentException e) {
                pl.getPlatform().getLogger().warning("No definition for the pronoun set '" + pronoun +
                    "' could be found!\nIf you're using MySQL, make sure predefinedSets matches on all servers!");
            }
        }
        return pronounsList;
    }

    public void clearUserPronouns(UUID uuid) {
        storage.clearPronouns(uuid);
    }

    public OldPronounSet fromString(String set) throws IllegalArgumentException {
        String[] pronouns = set.split("/");
        if (pronouns.length > 6) throw new IllegalArgumentException(set);

        OldPronounSet retrieved = setIndex.getOrDefault(pronouns[0].toLowerCase(), null);

        if (retrieved != null) return retrieved;

        if (pronouns.length != 6) throw new IllegalArgumentException(set);

        return new OldPronounSet(pronouns[0].replace(" ", ""),
            pronouns[1].replace(" ", ""),
            pronouns[2].replace(" ", ""),
            pronouns[3].replace(" ", ""),
            pronouns[4].replace(" ", ""),
            pronouns[5].replace(" ", ""));
    }

    @Override
    public Set<OldPronounSet> parseSets(String... input) {
        Set<OldPronounSet> out = new LinkedHashSet<>();
        for (String arg : input) {
            String[] splitArg = arg.split("/");
            if (splitArg.length == 6) {
                OldPronounSet parsed = pl.getPronounHandler().fromString(arg);
                out.add(parsed);
            } else {
                for (String _splitArg : splitArg) {
                    // check for objective
                    boolean cont = true;
                    for (OldPronounSet _set : out) {
                        if (_set.getObjective().toUpperCase(Locale.ROOT).equals(_splitArg.toUpperCase())) {
                            cont = false;
                            break;
                        }
                    }
                    if (!cont) continue;
                    OldPronounSet parsed = pl.getPronounHandler().fromString(_splitArg);
                    out.add(parsed);
                }
            }
        }
        return out;
    }
}