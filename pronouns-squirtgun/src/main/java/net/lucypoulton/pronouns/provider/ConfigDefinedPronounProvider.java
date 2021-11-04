package net.lucypoulton.pronouns.provider;

import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.PronounSet;

import java.util.HashSet;
import java.util.Set;

public class ConfigDefinedPronounProvider implements PronounProvider {

    private final ProNouns plugin;

    private final Set<PronounSet> sets = new HashSet<>();

    public ConfigDefinedPronounProvider(ProNouns plugin) {
        this.plugin = plugin;
        reload();
    }

    private void reload() {
        sets.clear();

        for (String set : plugin.getConfigHandler().getPredefinedSets()) {
            PronounHandler.ParseResult parsed = plugin.getPronounHandler().parse(set);
            if (parsed.success()) {
                sets.addAll(parsed.results());
            } else {
                plugin.getPlatform().getLogger().warning("Predefined set '" + set + "' is invalid, ignoring");
            }
        }
    }

    @Override
    public Set<PronounSet> get() {
        return null;
    }
}
