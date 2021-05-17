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

package me.lucyy.pronouns.command;

import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;


public class ListPronounsNode implements CommandNode<PermissionHolder> {
	private final PronounHandler pronounHandler;

	public ListPronounsNode(PronounHandler pronounHandler) {
		this.pronounHandler = pronounHandler;
	}

	@Override
	public @NotNull String getName() {
		return "list";
	}

	public String getDescription() {
		return "Show all predefined pronoun sets.";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public Component execute(CommandContext<PermissionHolder> context) {
		final FormatProvider fmt = context.getFormat();

		Component out = TextFormatter.formatTitle("All Predefined Pronoun Sets:", fmt);

		StringBuilder listBuilder = new StringBuilder();
		for (PronounSet set : pronounHandler.getAllPronouns()) {
			listBuilder.append(set.toString()).append("\n");
		}

		out = out.append(Component.text(listBuilder.toString()))
				.append(TextFormatter.formatTitle("*", fmt));
		return out;
	}
}
