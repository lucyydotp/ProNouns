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

package net.lucypoulton.pronouns.bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.api.set.PronounSet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class ProNounsPapi extends PlaceholderExpansion {

    private PronounSet unsetPronounSet;

    private final ProNouns plugin;

    public ProNounsPapi(ProNouns plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getAuthors());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pronouns";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (unsetPronounSet == null) {
            unsetPronounSet = plugin.getPronounHandler().parse("unset").results().stream().findFirst().orElseThrow();
        }

        if (player == null) return "";

        Set<PronounSet> allSets = plugin.getPronounHandler().getPronouns(plugin.getPlatform().getPlayer(player.getUniqueId()));
        PronounSet mainPronouns = allSets.size() > 0 ? allSets.iterator().next() : unsetPronounSet;

        String[] split = identifier.split("_");
        String ident = split[0];

        String feedback;

        switch (ident) {
            case "pronouns":
                feedback = PronounSet.format(allSets.size() == 0 ? Collections.singleton(unsetPronounSet) : allSets);
                break;
            case "all":
                feedback = mainPronouns.toString();
                break;
            case "objective":
                feedback = mainPronouns.objective();
                break;
            case "subjective":
                feedback = mainPronouns.subjective();
                break;
            case "progressive":
                feedback = mainPronouns.progressive();
                break;
            case "possessiveadj":
                feedback = mainPronouns.possessiveAdjective();
                break;
            case "possessivepro":
                feedback = mainPronouns.possessivePronoun();
                break;
            case "reflexive":
                feedback = mainPronouns.reflexive();
                break;
            default:
                return "";
        }

        for (int idx = 1; idx < split.length; idx++) {
            String mod = split[idx];
            switch (mod.toLowerCase(Locale.ROOT)) {
                case "upper":
                    feedback = feedback.toUpperCase();
                    break;
                case "lower":
                    feedback = feedback.toLowerCase();
                    break;
                case "capital":
                    feedback = feedback.substring(0, 1).toUpperCase() + feedback.substring(1).toLowerCase();
                    break;
                case "nounset":
                    if (feedback.equalsIgnoreCase("unset")) return "";
            }
        }
        return feedback;
    }
}
