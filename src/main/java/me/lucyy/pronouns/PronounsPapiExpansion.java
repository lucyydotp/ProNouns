package me.lucyy.pronouns;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lucyy.pronouns.set.PronounSet;
import me.lucyy.pronouns.set.UnsetPronounSet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PronounsPapiExpansion extends PlaceholderExpansion {
    private final ProNouns plugin;

    public PronounsPapiExpansion(ProNouns pl) {
        plugin = pl;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pronouns";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (player == null) return "";

        PronounSet mainPronouns;
        try {
            mainPronouns = plugin.getPronounHandler().getUserPronouns(player.getUniqueId())[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            mainPronouns = new UnsetPronounSet(plugin.getPronounHandler().fromString("they"));
        }
        String ident = identifier.split("_")[0];
        String mod;
        try {
            mod = identifier.split("_")[1];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            mod = "";
        }

        String feedback;

        switch (ident) {
            case "pronouns":
                feedback = mainPronouns.getName();
                break;
            case "all":
                feedback = mainPronouns.toString();
                break;
            case "objective":
                feedback = mainPronouns.objective;
                break;
            case "subjective":
                feedback = mainPronouns.subjective;
                break;
            case "progressive":
                feedback = mainPronouns.progressive;
                break;
            case "possessiveadj":
                feedback = mainPronouns.possessiveAdjectival;
                break;
            case "possessivepro":
                feedback = mainPronouns.possessivePronoun;
                break;
            case "reflexive":
                feedback = mainPronouns.reflexive;
                break;
            default:
                return "";
        }

        switch (mod) {
            case "upper":
                feedback = feedback.toUpperCase();
                break;
            case "lower":
                feedback = feedback.toLowerCase();
                break;
            case "capital":
                feedback = feedback.substring(0, 1).toUpperCase() + feedback.substring(1).toLowerCase();
                break;
        }
        return feedback;
    }
}
