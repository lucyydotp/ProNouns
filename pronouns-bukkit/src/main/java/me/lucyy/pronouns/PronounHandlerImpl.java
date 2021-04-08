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

package me.lucyy.pronouns;

import lombok.Getter;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.event.PronounsSetEvent;
import me.lucyy.pronouns.api.set.AnyPronounSet;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;
import org.bukkit.Bukkit;
import java.util.*;

public class PronounHandlerImpl implements PronounHandler {
	@Getter
	private final Storage storage;
	private final ProNouns pl;
	private final HashMap<String, PronounSet> setIndex = new HashMap<>();

	public void addToIndex(PronounSet set) {
		set.isPredefined = true;
		setIndex.put(set.subjective, set);
	}

	public PronounHandlerImpl(ProNouns pl, Storage storage) {
		this.pl = pl;
		this.storage = storage;
		addToIndex(fromString("he/him/he's/his/his/himself"));
		addToIndex(fromString("she/her/she's/her/hers/herself"));
		addToIndex(fromString("they/them/they're/their/theirs/themself"));
		setIndex.put("any", new AnyPronounSet(fromString("they")));
	}

	public void setUserPronouns(UUID uuid, Set<PronounSet> set) {
		storage.setPronouns(uuid, set);
		Bukkit.getServer().getPluginManager().callEvent(new PronounsSetEvent(uuid, set));
	}

	public Set<PronounSet> getAllPronouns() {
		return new HashSet<>(setIndex.values());
	}

	public Set<PronounSet> getUserPronouns(UUID uuid) {
		Set<PronounSet> pronounsList = new HashSet<>();
		for (String pronoun : storage.getPronouns(uuid)) {
			try {
				pronounsList.add(fromString(pronoun));
			} catch (IllegalArgumentException e) {
				Bukkit.getLogger().warning("No definition for the pronoun set '" + pronoun + "' could be found!");
				Bukkit.getLogger().warning("If you're using MySQL, make sure predefinedSets matches on all servers!");
			}
		}
		return pronounsList;
	}

	public void clearUserPronouns(UUID uuid) {
		storage.clearPronouns(uuid);
	}

	public PronounSet fromString(String set) throws IllegalArgumentException {
		String[] pronouns = set.split("/");
		if (pronouns.length > 6) throw new IllegalArgumentException(set);

		PronounSet retrieved = setIndex.getOrDefault(pronouns[0].toLowerCase(), null);

		if (retrieved != null) return retrieved;

		if (pronouns.length != 6) throw new IllegalArgumentException(set);

		return new PronounSet(pronouns[0].replace(" ", ""),
				pronouns[1].replace(" ", ""),
				pronouns[2].replace(" ", ""),
				pronouns[3].replace(" ", ""),
				pronouns[4].replace(" ", ""),
				pronouns[5].replace(" ", ""));
	}

	@Override
	public Set<PronounSet> parseSets(String... input) {
		Set<PronounSet> out = new HashSet<>();
		for (String arg : input) {
			String[] splitArg = arg.split("/");
			if (splitArg.length == 6) {
				PronounSet parsed = pl.getPronounHandler().fromString(arg);
				out.add(parsed);
			} else {
				for (String _splitArg : splitArg) {
					// check for objective
					boolean cont = true;
					for (PronounSet _set : out) {
						if (_set.objective.toUpperCase(Locale.ROOT).equals(_splitArg.toUpperCase())) {
							cont = false;
							break;
						}
					}
					if (!cont) continue;
					PronounSet parsed = pl.getPronounHandler().fromString(_splitArg);
					out.add(parsed);
				}
			}
		}
		return out;
	}
}