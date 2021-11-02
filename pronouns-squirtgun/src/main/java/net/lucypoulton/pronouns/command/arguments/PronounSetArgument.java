package net.lucypoulton.pronouns.command.arguments;

import com.google.common.base.Splitter;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.StringUtils;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PronounSetArgument implements CommandArgument<Set<PronounHandler.ParseResult>> {

    private final PronounHandler handler;

    public PronounSetArgument(PronounHandler handler) {
        this.handler = handler;
    }

    @Override
    public @NotNull String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "A list of pronoun sets.";
    }

    @Override
    public Set<PronounHandler.ParseResult> getValue(Queue<String> args, CommandContext context) {
        Set<PronounHandler.ParseResult> results = new LinkedHashSet<>();
        for (String arg : args) {
            results.add(handler.parse(arg, !context.getTarget().hasPermission("pronouns.bypass")));
        }
        return results.size() == 0 ? null : results;
    }

    @Override
    public @Nullable List<String> tabComplete(Queue<String> args, CommandContext context) {

        Set<PronounSet> sets = new HashSet<>();
        
        String tail = args.remove();
        while (!args.isEmpty()) tail = args.poll();
        
        List<String> current = Splitter.on("/").splitToList(tail);

        Set<PronounSet> all = handler.getAllPronouns();

        if (current.size() <= 1) {
            return all.stream().map(PronounSet::subjective).collect(Collectors.toUnmodifiableList());
        }

        for (String setString : current) {
            all.stream()
                .filter(set -> Arrays.asList(set.asArray()).contains(setString))
                .forEach(sets::add);
        }

        String base = String.join("/", current.subList(0, current.size() - 1)) + "/";

        return all.stream()
            .filter(x -> !sets.contains(x))
            .map(x -> base + x.subjective())
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String toString() {
        return "<pronouns> [pronouns]...";
    }
}
