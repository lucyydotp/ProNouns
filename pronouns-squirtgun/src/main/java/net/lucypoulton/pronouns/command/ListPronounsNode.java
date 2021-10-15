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

import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.kyori.adventure.text.Component;


public class ListPronounsNode extends AbstractNode<PermissionHolder> {
	private final PronounHandler pronounHandler;

	public ListPronounsNode(PronounHandler pronounHandler) {
		super("list", "Shows all predefined pronoun sets.", Condition.alwaysTrue());
		this.pronounHandler = pronounHandler;
	}

	@Override
	public Component execute(CommandContext context) {
		final FormatProvider fmt = context.getFormat();

		Component out = Component.empty()
				.append(fmt.formatTitle("All Predefined Pronoun Sets:"))
				.append(Component.newline());

		StringBuilder listBuilder = new StringBuilder();
		for (OldPronounSet set : pronounHandler.getAllPronouns()) {
			listBuilder.append(set.toString()).append("\n");
		}

		listBuilder.append("\n");

		out = out.append(Component.text(listBuilder.toString()))
				.append(fmt.formatFooter("*"));
		return out;
	}
}
