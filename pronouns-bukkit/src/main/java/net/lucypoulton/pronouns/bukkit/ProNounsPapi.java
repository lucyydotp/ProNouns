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
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.api.set.old.UnsetPronounSet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

public class ProNounsPapi extends PlaceholderExpansion {

    private OldPronounSet unsetPronounSet;

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
            unsetPronounSet = new UnsetPronounSet(plugin.getPronounHandler().fromString("they"));
        }

        if (player == null) return "";

        Set<OldPronounSet> allSets = plugin.getPronounHandler().getPronouns(player.getUniqueId());
        OldPronounSet mainPronouns = allSets.size() > 0 ? allSets.iterator().next() : unsetPronounSet;

        String[] split = identifier.split("_");
        String ident = split[0];

        String feedback;

        switch (ident) {
            case "pronouns":
                feedback = OldPronounSet.friendlyPrintSet(allSets);
                break;
            case "all":
                feedback = mainPronouns.toString();
                break;
            case "objective":
                feedback = mainPronouns.getObjective();
                break;
            case "subjective":
                feedback = mainPronouns.getSubjective();
                break;
            case "progressive":
                feedback = mainPronouns.getProgressive();
                break;
            case "possessiveadj":
                feedback = mainPronouns.getPossessiveAdjective();
                break;
            case "possessivepro":
                feedback = mainPronouns.getPossessivePronoun();
                break;
            case "reflexive":
                feedback = mainPronouns.getReflexive();
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
