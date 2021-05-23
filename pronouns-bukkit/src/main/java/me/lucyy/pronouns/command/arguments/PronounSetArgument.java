package me.lucyy.pronouns.command.arguments;

import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class PronounSetArgument implements CommandArgument<Set<PronounSet>> {

	private final PronounHandler handler;

	public PronounSetArgument(PronounHandler handler) {
		this.handler = handler;
	}

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getDescription() {
		return "A list of pronoun sets.";
	}

	@Override
	public Set<PronounSet> getValue(Queue<String> args) {
		try {
			Set<PronounSet> out =  handler.parseSets(args.toArray(new String[0]));
			return out == null || out.size() == 0 ? null : out;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public @Nullable List<String> tabComplete(Queue<String> args) {
		List<String> allPronouns = new ArrayList<>();

		allPronouns.add("<custom>");

		for (PronounSet set : handler.getAllPronouns()) {
			allPronouns.add(set.getName().toLowerCase());
		}

		while (!args.isEmpty()) {
			String arg = args.poll();
			if (arg.contains("/") && !allPronouns.contains(arg)) {
				List<String> pronounsSoFar = Arrays.asList(arg.split("/"));
				String soFarJoined = String.join("/", pronounsSoFar);
				for (PronounSet set : handler.getAllPronouns()) {
					if (!pronounsSoFar.contains(set.getSubjective()))
						allPronouns.add(soFarJoined + "/" + set.getSubjective());
				}
				allPronouns.add(soFarJoined);
			}
		}

		return allPronouns;
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public String toString() {
		return "<pronouns> [pronouns]...\n" +
				"If you're trying to use neopronouns, that's ok! You just need to write the set out in full." +
				"See https://docs.lucyy.me/pronouns/";
	}
}
