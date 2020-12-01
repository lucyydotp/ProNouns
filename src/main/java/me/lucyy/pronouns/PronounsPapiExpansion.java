package me.lucyy.pronouns;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PronounsPapiExpansion extends PlaceholderExpansion {
    private ProNouns plugin;

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
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "pronouns";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) return "";

        PronounSet mainPronouns;
        try {
            mainPronouns = plugin.getPronounHandler().GetUserPronouns(player.getUniqueId())[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            mainPronouns = plugin.getPronounHandler().FromString("they");
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
                feedback = mainPronouns.Objective;
                break;
            case "subjective":
                feedback = mainPronouns.Subjective;
                break;
            case "progressive":
                feedback = mainPronouns.Progressive;
                break;
            case "possessiveadj":
                feedback = mainPronouns.PossessiveAdjectival;
                break;
            case "possessivepro":
                feedback = mainPronouns.PossessivePronoun;
                break;
            case "reflexive":
                feedback = mainPronouns.Reflexive;
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
