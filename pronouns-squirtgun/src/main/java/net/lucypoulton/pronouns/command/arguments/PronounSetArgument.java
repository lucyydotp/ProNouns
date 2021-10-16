package net.lucypoulton.pronouns.command.arguments;

import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
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
            results.add(handler.parse(arg));
        }
        if (results.size() == 0 || results.stream().allMatch(PronounHandler.ParseResult::failure)) {
            return null;
        }
        return results;
    }

    @Override
    public @Nullable List<String> tabComplete(Queue<String> args, CommandContext context) {
        // FIXME
        return List.of("FIXME tabcomplete PronounSetArgument");
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
