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

package net.lucypoulton.pronouns.command;

import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PreviewNode extends AbstractNode<PermissionHolder> {
	private final ProNouns pl;

	public PreviewNode(ProNouns plugin) {
		super("preview", "Test out your pronoun selection", Condition.alwaysTrue());
		pl = plugin;
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return Collections.emptyList();
	}

	private String capitalise(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	@Override
	public Component execute(final CommandContext context) {
		final FormatProvider fmt = context.getFormat();
		if (!(context.getTarget() instanceof SquirtgunPlayer)) {
			return fmt.getPrefix()
					.append(fmt.formatMain("Only players can use this command."));
		}

		final SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();

		final Collection<OldPronounSet> sets = pl.getPronounHandler().getPronouns(player.getUuid());
		if (sets.size() == 0) {
			return fmt.getPrefix()
					.append(fmt.formatMain("You haven't set any pronouns yet!"));
		}

		final OldPronounSet set = sets.iterator().next();

		// yes, this is messy, but java compiles it to a stringbuilder so its all good
		String builder = player.getUsername() + " is testing " + set.getPossessiveAdjective() +
				" pronoun selection.\n" +
				"Have you seen " + player.getUsername() + "? " +
				capitalise(set.getSubjective()) + " asked me to help with " +
				set.getObjective() + " build.\n" +
				player.getUsername() + " has been spending all " + set.getPossessiveAdjective() +
				" time on this server. I hope " + set.getProgressive() + " doing okay.";
		return Component.text(builder);
	}
}
